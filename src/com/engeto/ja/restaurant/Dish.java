package com.engeto.ja.restaurant;

import java.math.BigDecimal;

public class Dish {
    private String title;
    private BigDecimal price;
    private int preparationTime;
    private String image;

    public Dish(String title, BigDecimal price, int preparationTime, String image) throws RestaurantException {
        setTitle(title);
        setPrice(price);
        setPreparationTime(preparationTime);
        setImage(image);
    }

    public Dish(String title, BigDecimal price, int preparationTime) throws RestaurantException {
        this(title, price, preparationTime, Settings.getDefaultImage());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(BigDecimal price) throws RestaurantException  {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new RestaurantException("Zadaná chybná cena. Zadaná cena: \"" + price + "\" Kč.");
        }
        this.price = price;
    }

    public void setPreparationTime(int preparationTime) throws RestaurantException{
        if (preparationTime < 0) {
            throw new RestaurantException("Zadaná chybná doba přípravy. Zadaná doba: \"" + preparationTime + "\" minut.");
        }
        this.preparationTime = preparationTime;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Jídlo: " + title + ", cena: " + price + " Kč, doba přípravy: " + preparationTime + " minut, obrázek: " + image;
    }
}
