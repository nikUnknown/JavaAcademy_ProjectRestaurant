package com.engeto.ja.restaurant;

import java.util.ArrayList;
import java.util.List;

public class Orders {

    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public void clearOrders() {
        this.orders.clear();
    }

    public void printOrders() {
        for (Order order : orders) {
            System.out.println(order.toString());
        }
    }

}
