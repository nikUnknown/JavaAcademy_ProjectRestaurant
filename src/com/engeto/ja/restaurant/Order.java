package com.engeto.ja.restaurant;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Order implements Comparable<Order>  {
    private Dish dish;
    private int quantity;
    private LocalDateTime orderedTime;
    private LocalDateTime fulfilmentTime;
    private int tableNumber;
    private boolean paid;

    public Order(Dish dish, int quantity, LocalDateTime orderedTime, LocalDateTime fulfilmentTime, int tableNumber, boolean paid) throws RestaurantException{
        this.dish = dish;
        setQuantity(quantity);
        setOrderedTime(orderedTime);
        this.fulfilmentTime = fulfilmentTime;
        setTableNumber(tableNumber);
        this.paid = paid;
    }

    public Order(Dish dish, int quantity, LocalDateTime orderedTime, int tableNumber, boolean paid) throws RestaurantException{
        this(dish, quantity, orderedTime, LocalDateTime.MAX, tableNumber, paid);
    }

    public Order(Dish dish, int quantity, LocalDateTime orderedTime, int tableNumber) throws RestaurantException {
        this(dish, quantity, orderedTime, LocalDateTime.MAX, tableNumber, false);
    }

    public Order(Dish dish, int quantity,int tableNumber) throws RestaurantException {
        this(dish, quantity, LocalDateTime.now(), tableNumber);
    }

    public Order(Dish dish, int tableNumber) throws RestaurantException {
        this(dish, 1, tableNumber);
    }

    private void setQuantity(int quantity) throws RestaurantException {
        if (quantity < 1) {
            throw new RestaurantException("Zadané množství \"" + quantity + "\" je menší než minimální povolené množství 1.");
        }
        this.quantity = quantity;
    }

    private void setOrderedTime(LocalDateTime orderedTime) throws RestaurantException {
        if (orderedTime == null) {
            throw new RestaurantException("Zadaný čas objednávky je neplatný.");
        }
        this.orderedTime = orderedTime;
    }

    public void setFulfilmentTime(LocalDateTime fulfilmentTime) throws RestaurantException {
        if (fulfilmentTime.isBefore(orderedTime)) {
            throw new RestaurantException("Čas splnění nesmí být dřív než datum objednání. Zadaný čas: " + fulfilmentTime.format(DateTimeFormatter.ofPattern(Settings.getDateFormat())) + ".");
        }
        this.fulfilmentTime = fulfilmentTime;
    }

    public void setFulfilmentTime() throws RestaurantException {
        this.setFulfilmentTime(LocalDateTime.now());
    }

    private void setTableNumber (int tableNumber) throws RestaurantException {
        if (tableNumber < 1 || tableNumber > Settings.getMaxTableNumber()) {
            throw new RestaurantException("Zadané číslo \"" + tableNumber + "\" není v rozsahu platných stolů.");
        }
        this.tableNumber = tableNumber;
    }

    public void setAsPaid() {
        paid = true;
    }

    public Dish getDish() {
        return dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public LocalDateTime getFulfilmentTime() {
        return fulfilmentTime;
    }
    public int getTableNumber() {
        return tableNumber;
    }

    public boolean isPaid() {
        return paid;
    }

    public BigDecimal getOrderPrice() {
        return dish.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public int getProcessingTime() {
        return fulfilmentTime != null ? (int) ChronoUnit.MINUTES.between(orderedTime, fulfilmentTime) : 0;
    }

    public Order getOrder() {
        return this;
    }

    @Override
    public String toString() {
        return dish.getTitle() + " "
                + (quantity > 1 ? quantity + "x " : "") + "(" + getOrderPrice() + " Kč):\t"
                + orderedTime.format(DateTimeFormatter.ofPattern(Settings.getTimeFormat())) + " - "
                + (fulfilmentTime != LocalDateTime.MAX ? fulfilmentTime.format(DateTimeFormatter.ofPattern(Settings.getTimeFormat())) : "\t")
                + (paid ? "zaplaceno" : "");
    }

    @Override
    public int compareTo(Order otherOrder) {
        return this.orderedTime.compareTo(otherOrder.orderedTime);
    }

}
