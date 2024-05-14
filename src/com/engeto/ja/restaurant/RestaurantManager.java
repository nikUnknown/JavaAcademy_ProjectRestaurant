package com.engeto.ja.restaurant;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RestaurantManager {

    public int getUnfinishedOrdersCount(List<Order> orders) {
        int unfinishedOrders = 0;
        for (Order order : orders) {
            if (order.getFulfilmentTime() == LocalDateTime.MAX) {
                unfinishedOrders++;
            }
        }
        return unfinishedOrders;
    }

    public StringBuilder getOrdersSummarySortedByOrderedTime(List<Order> orders) throws RestaurantException{
        if (orders.isEmpty()) {
            throw new RestaurantException("Seznam objednávek je prázdný.");
        }
        List<Order> sortedOrders = new ArrayList<>(orders);
        sortedOrders.sort(Comparator.comparing(Order::getOrderedTime));
        StringBuilder result = new StringBuilder();
        for (Order order : sortedOrders) {
            result.append(order.toString()).append("\n");
        }
        return result;
    }

    public int getAverageOrderProcessingTime(List<Order> orders) {
        int totalProcessingTime = 0;
        int processedOrders = 0;
        for (Order order : orders) {
            if (order.getFulfilmentTime() != LocalDateTime.MAX) {
                totalProcessingTime += order.getProcessingTime();
                processedOrders++;
            }
        }
        if (processedOrders == 0) {
            return 0;
        }
        int averageProcessingTime = totalProcessingTime / processedOrders;
        return processedOrders > 0 ? averageProcessingTime : 0;
    }

    public StringBuilder getUniqueDishesOrderedToday(List<Order> orders) {
        ArrayList<Dish> uniqueDishes = new ArrayList<>();
        for (Order order : orders) {
            if (order.getOrderedTime().toLocalDate().equals(LocalDate.now())) {
                if (!uniqueDishes.contains(order.getDish())) {
                    uniqueDishes.add(order.getDish());
                }
            }
        }
        StringBuilder result = new StringBuilder();
        for (Dish dish : uniqueDishes) {
            result.append(dish.getTitle()).append("\n");
        }
        return result;
    }

    public BigDecimal getTotalPriceForTable(List<Order> orders, int tableNumber) throws RestaurantException{
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (tableNumber < 1 || tableNumber > Settings.getMaxTableNumber()) {
            throw new RestaurantException("Zadané číslo stolu \"" + tableNumber + "\" je mimo rozsah povolených hodnot.");
        }
        for (Order order : orders) {
            if (order.getTableNumber() == tableNumber) {
                totalPrice = totalPrice.add(order.getOrderPrice());
            }
        }
        return totalPrice;
    }

    public StringBuilder getOrdersForTable (List<Order> orders, int tableNumber) throws RestaurantException {
        if (tableNumber < 1 || tableNumber > Settings.getMaxTableNumber()) {
            throw new RestaurantException("Zadané číslo stolu \"" + tableNumber + "\" je mimo rozsah povolených hodnot.");
        }
        StringBuilder result = new StringBuilder();
        result.append("** Objednávky pro stůl č. ").append(String.format("%02d", tableNumber)).append(" **\n");
        result.append("****\n");
        for (Order order : orders) {
            if (order.getTableNumber() == tableNumber) {
                result.append(order.toString()).append("\n");
            }
        }
        result.append("******");
        return result;
    }

}
