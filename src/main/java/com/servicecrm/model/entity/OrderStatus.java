package com.servicecrm.model.entity;

public enum OrderStatus {
    CREATED("Создана"),
    RECEIVED("Принята в сервис"),
    DIAGNOSTICS("На диагностике"),
    AWAITING_APPROVAL("Ожидает подтверждения"),
    APPROVED("Одобрена к ремонту"),
    REJECTED("Отклонена"),
    IN_PROGRESS("В процессе ремонта"),
    COMPLETED("Завершена"),
    PAID("Оплачена"),
    RETURNED("Выдана клиенту");
    
    private final String displayName;
    
    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
