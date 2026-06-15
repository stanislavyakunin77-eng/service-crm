package com.servicecrm.repository;

import com.servicecrm.model.entity.Order;
import com.servicecrm.model.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    Page<Order> findByClientId(Long clientId, Pageable pageable);
    Page<Order> findByManagerId(Long managerId, Pageable pageable);
    Page<Order> findByMasterId(Long masterId, Pageable pageable);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    Page<Order> findAll(Pageable pageable);
    List<Order> findByStatus(OrderStatus status);
}
