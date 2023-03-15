package ru.yandex.praktikum.scooter.model.order;

import java.util.ArrayList;

public class OrderListResponse {
    ArrayList<OrderResponse> orders;

    public OrderListResponse(ArrayList<OrderResponse> orders) {
        this.orders = orders;
    }

    public OrderListResponse() {}

    public ArrayList<OrderResponse> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<OrderResponse> orders) {
        this.orders = orders;
    }
}
