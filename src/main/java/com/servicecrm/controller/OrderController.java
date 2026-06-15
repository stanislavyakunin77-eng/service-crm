package com.servicecrm.controller;

import com.servicecrm.model.dto.OrderDTO;
import com.servicecrm.model.entity.OrderStatus;
import com.servicecrm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(Pageable pageable) {
        Page<OrderDTO> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByClient(@PathVariable Long clientId, Pageable pageable) {
        Page<OrderDTO> orders = orderService.getOrdersByClientId(clientId, pageable);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByManager(@PathVariable Long managerId, Pageable pageable) {
        Page<OrderDTO> orders = orderService.getOrdersByManagerId(managerId, pageable);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/master/{masterId}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByMaster(@PathVariable Long masterId, Pageable pageable) {
        Page<OrderDTO> orders = orderService.getOrdersByMasterId(masterId, pageable);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByStatus(@PathVariable OrderStatus status, Pageable pageable) {
        Page<OrderDTO> orders = orderService.getOrdersByStatus(status, pageable);
        return ResponseEntity.ok(orders);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @PathVariable OrderStatus status) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/assign-manager/{managerId}")
    public ResponseEntity<OrderDTO> assignManager(@PathVariable Long id, @PathVariable Long managerId) {
        OrderDTO updatedOrder = orderService.assignManager(id, managerId);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/assign-master/{masterId}")
    public ResponseEntity<OrderDTO> assignMaster(@PathVariable Long id, @PathVariable Long masterId) {
        OrderDTO updatedOrder = orderService.assignMaster(id, masterId);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/diagnostics")
    public ResponseEntity<OrderDTO> updateDiagnostics(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String diagnostics = body.get("diagnostics");
        OrderDTO updatedOrder = orderService.updateDiagnostics(id, diagnostics);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/approve")
    public ResponseEntity<OrderDTO> approveDiagnostics(@PathVariable Long id) {
        OrderDTO updatedOrder = orderService.approveDiagnostics(id);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/reject")
    public ResponseEntity<OrderDTO> rejectRepair(@PathVariable Long id) {
        OrderDTO updatedOrder = orderService.rejectRepair(id);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<OrderDTO> completeRepair(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String repairNotes = body.get("repairNotes");
        OrderDTO updatedOrder = orderService.completeRepair(id, repairNotes);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/pay")
    public ResponseEntity<OrderDTO> markAsPaid(@PathVariable Long id) {
        OrderDTO updatedOrder = orderService.markAsPaid(id);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/return")
    public ResponseEntity<OrderDTO> returnToClient(@PathVariable Long id) {
        OrderDTO updatedOrder = orderService.returnToClient(id);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
