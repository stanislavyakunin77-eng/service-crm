package com.servicecrm.service;

import com.servicecrm.model.dto.OrderDTO;
import com.servicecrm.model.entity.Client;
import com.servicecrm.model.entity.Order;
import com.servicecrm.model.entity.OrderStatus;
import com.servicecrm.model.entity.User;
import com.servicecrm.repository.ClientRepository;
import com.servicecrm.repository.OrderRepository;
import com.servicecrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Client client = clientRepository.findById(orderDTO.getClientId())
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
        
        String orderNumber = generateOrderNumber();
        
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .client(client)
                .deviceType(orderDTO.getDeviceType())
                .deviceModel(orderDTO.getDeviceModel())
                .deviceSerial(orderDTO.getDeviceSerial())
                .description(orderDTO.getDescription())
                .status(OrderStatus.CREATED)
                .build();
        
        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }
    
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + id));
        return convertToDTO(order);
    }
    
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    public Page<OrderDTO> getOrdersByClientId(Long clientId, Pageable pageable) {
        return orderRepository.findByClientId(clientId, pageable)
                .map(this::convertToDTO);
    }
    
    public Page<OrderDTO> getOrdersByManagerId(Long managerId, Pageable pageable) {
        return orderRepository.findByManagerId(managerId, pageable)
                .map(this::convertToDTO);
    }
    
    public Page<OrderDTO> getOrdersByMasterId(Long masterId, Pageable pageable) {
        return orderRepository.findByMasterId(masterId, pageable)
                .map(this::convertToDTO);
    }
    
    public Page<OrderDTO> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable)
                .map(this::convertToDTO);
    }
    
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + id));
        
        if (orderDTO.getDeviceType() != null) {
            order.setDeviceType(orderDTO.getDeviceType());
        }
        if (orderDTO.getDeviceModel() != null) {
            order.setDeviceModel(orderDTO.getDeviceModel());
        }
        if (orderDTO.getDeviceSerial() != null) {
            order.setDeviceSerial(orderDTO.getDeviceSerial());
        }
        if (orderDTO.getDescription() != null) {
            order.setDescription(orderDTO.getDescription());
        }
        if (orderDTO.getEstimatedCost() != null) {
            order.setEstimatedCost(orderDTO.getEstimatedCost());
        }
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    
    public OrderDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + id));
        
        order.setStatus(status);
        if (status == OrderStatus.COMPLETED) {
            order.setCompletedAt(LocalDateTime.now());
        }
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    
    public OrderDTO assignManager(Long orderId, Long managerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new NoSuchElementException("Manager not found"));
        
        order.setManager(manager);
        order.setStatus(OrderStatus.RECEIVED);
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    
    public OrderDTO assignMaster(Long orderId, Long masterId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        
        User master = userRepository.findById(masterId)
                .orElseThrow(() -> new NoSuchElementException("Master not found"));
        
        order.setMaster(master);
        order.setStatus(OrderStatus.DIAGNOSTICS);
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    
    public OrderDTO updateDiagnostics(Long orderId, String diagnostics) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        
        order.setDiagnostics(diagnostics);
        order.setStatus(OrderStatus.AWAITING_APPROVAL);
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    
    public OrderDTO approveDiagnostics(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        
        order.setStatus(OrderStatus.APPROVED);
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    
    public OrderDTO rejectRepair(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        
        order.setStatus(OrderStatus.REJECTED);
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    
    public OrderDTO completeRepair(Long orderId, String repairNotes) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        
        order.setRepairNotes(repairNotes);
        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedAt(LocalDateTime.now());
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    
    public OrderDTO markAsPaid(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        
        order.setIsPaid(true);
        order.setStatus(OrderStatus.PAID);
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    
    public OrderDTO returnToClient(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        
        order.setStatus(OrderStatus.RETURNED);
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }
    
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new NoSuchElementException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
    
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private OrderDTO convertToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .clientId(order.getClient().getId())
                .clientName(order.getClient().getFirstName() + " " + order.getClient().getLastName())
                .managerId(order.getManager() != null ? order.getManager().getId() : null)
                .managerName(order.getManager() != null ? order.getManager().getFirstName() + " " + order.getManager().getLastName() : null)
                .masterId(order.getMaster() != null ? order.getMaster().getId() : null)
                .masterName(order.getMaster() != null ? order.getMaster().getFirstName() + " " + order.getMaster().getLastName() : null)
                .deviceType(order.getDeviceType())
                .deviceModel(order.getDeviceModel())
                .deviceSerial(order.getDeviceSerial())
                .description(order.getDescription())
                .status(order.getStatus())
                .estimatedCost(order.getEstimatedCost())
                .finalCost(order.getFinalCost())
                .diagnostics(order.getDiagnostics())
                .repairNotes(order.getRepairNotes())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .completedAt(order.getCompletedAt())
                .isPaid(order.getIsPaid())
                .build();
    }
}
