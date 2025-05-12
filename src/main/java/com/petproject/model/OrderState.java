package com.petproject.model;

public enum OrderState {
    NEW("Новый"),
    PROCESSING("В обработке"),
    COMPLETED("Завершён"),
    CANCELLED("Отменён");

    private final String description;

    OrderState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
