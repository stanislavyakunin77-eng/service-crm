package com.servicecrm.model.dto;

import com.servicecrm.model.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    
    private Long id;
    
    private String orderNumber;
    
    @NotNull(message = "Client ID is required")
    private Long clientId;
    
    private String clientName;
    
    private Long managerId;
    
    private String managerName;
    
    private Long masterId;
    
    private String masterName;
    
    @NotBlank(message = "Device type is required")
    private String deviceType;
    
    private String deviceModel;
    
    private String deviceSerial;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    private OrderStatus status;
    
    private BigDecimal estimatedCost;
    
    private BigDecimal finalCost;
    
    private String diagnostics;
    
    private String repairNotes;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private LocalDateTime completedAt;
    
    private Boolean isPaid;
}
