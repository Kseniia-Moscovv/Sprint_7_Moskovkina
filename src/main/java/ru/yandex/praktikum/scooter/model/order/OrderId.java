package ru.yandex.praktikum.scooter.model.order;

public class OrderId {
    private int id;

    public OrderId(int id) {
        this.id = id;
    }

    public OrderId() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
