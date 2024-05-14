// Vytvoř třídu CookBook, která bude reprezentovat zásobník jídel v naší restauraci.
//Kuchaři mají připraven zásobník jídel (dish + cook book).
//Kuchaři mají možnost některá jídla ze zásobníku vyřadit, přidat, nebo upravit.

package com.engeto.ja.restaurant;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CookBook {

    private Map<Integer, Dish> dishes = new HashMap<>();
    private int nextId = 1;

    public void addDish(Dish dish) throws RestaurantException {
        try {
            dishes.put(nextId, dish);
            nextId++;
        } catch (Exception e) {
            throw new RestaurantException("Nastala chyba při přidávání jídla:\n" + e.getLocalizedMessage());
        }
    }

    public Dish getDish(int id) throws RestaurantException {
        Dish dish = dishes.get(id);
        if (dish == null) {
            throw new RestaurantException("Jídlo s ID \"" + id + "\" nenalezeno.");
        }
        return dish;
    }

    public void removeDish(int id) throws RestaurantException {
        if (dishes.containsKey(id)) {
            dishes.remove(id);
        } else {
            throw new RestaurantException("Jídlo s ID \"" + id + "\" nenalezeno.");
        }
    }

    public void updateDish(int id, Dish dish) throws RestaurantException {
        if (dishes.containsKey(id)) {
            try {
                dishes.put(id, dish);
            } catch (Exception e) {
                throw new RestaurantException("Chyba při aktualizaci jídla s ID \"" + id + "\":\n" + e.getLocalizedMessage());
            }
        } else {
            throw new RestaurantException("Jídlo s ID \"" + id + "\" nenalezeno.");
        }
    }

    public void updateDishTitle(int id, String title) throws RestaurantException {
        if (dishes.containsKey(id)) {
            try {
                dishes.get(id).setTitle(title);
            } catch (Exception e) {
                throw new RestaurantException("Chyba při aktualizaci názvu jídla s ID \"" + id + "\":\n" + e.getLocalizedMessage());
            }
        } else {
            throw new RestaurantException("Jídlo s ID \"" + id + "\" nenalezeno.");
        }
    }

    public void updateDishPrice(int id, BigDecimal price) throws RestaurantException {
        if (dishes.containsKey(id)) {
            try {
                dishes.get(id).setPrice(price);
            } catch (Exception e) {
                throw new RestaurantException("Chyba při aktualizaci ceny jídla s ID \"" + id + "\":\n" + e.getLocalizedMessage());
            }
        } else {
            throw new RestaurantException("Jídlo s ID \"" + id + "\" nenalezeno.");
        }
    }

    public void updateDishPreparationTime(int id, int preparationTime) throws RestaurantException {
        if (dishes.containsKey(id)) {
            try {
                dishes.get(id).setPreparationTime(preparationTime);
            } catch (Exception e) {
                throw new RestaurantException("Chyba při aktualizaci doby přípravy jídla s ID \"" + id + "\":\n" + e.getLocalizedMessage());
            }
        } else {
            throw new RestaurantException("Jídlo s ID \"" + id + "\" nenalezeno.");
        }
    }

    public void updateDishImage(int id, String image) throws RestaurantException {
        if (dishes.containsKey(id)) {
            try {
                dishes.get(id).setImage(image);
            } catch (Exception e) {
                throw new RestaurantException("Chyba při aktualizaci obrázku jídla s ID \"" + id + "\":\n" + e.getLocalizedMessage());
            }
        } else {
            throw new RestaurantException("Jídlo s ID \"" + id + "\" nenalezeno.");
        }
    }

    public Map<Integer, Dish> getDishes() {
        return new HashMap<>(dishes);
    }

    public int getDishesCount() {
        return dishes.size();
    }

    public void clearDishes() {
        dishes.clear();
        nextId = 1;
    }

    public Dish getDishById(int id) throws RestaurantException{
        if (!dishes.containsKey(id)) {
            throw new RestaurantException("Jídlo s ID \"" + id + "\" nenalezeno.");
        }
        return dishes.get(id);
    }

    public int getDishId(Dish dish) throws RestaurantException{
        try {
            for (Map.Entry<Integer, Dish> entry : dishes.entrySet()) {
                if (entry.getValue().equals(dish)) {
                    return entry.getKey();
                }
            }
            throw new RestaurantException("Jídlo nenalezeno.");
        } catch (Exception e) {
            throw new RestaurantException("Nastala chyba při hledání jídla:\n" + e.getLocalizedMessage());
        }
    }

    public Boolean containsDish(Dish dish) {
        return dishes.containsValue(dish);
    }

    public String getDishInfo(int id) throws RestaurantException {
        Dish dish = dishes.get(id);
        if (dish == null) {
            throw new RestaurantException("Jídlo s ID \"" + id + "\" nenalezeno.");
        }
        return "Informace o jídle s ID \"" + id + "\": " + dish.getTitle() + ", cena: " + dish.getPrice() + ", doba přípravy: " + dish.getPreparationTime() + " minut, obrázek: " + dish.getImage();
    }

    public String getDishesInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        if (dishes.isEmpty()) {
            return "Seznam jídel je prázdný.";
        } else {
            for (Map.Entry<Integer, Dish> entry : dishes.entrySet()) {
                stringBuilder.append("Jídlo s ID ").append(entry.getKey()).append(": ").append(entry.getValue().getTitle()).append(", cena: ")
                        .append(entry.getValue().getPrice()).append(" Kč, doba přípravy: ").append(entry.getValue().getPreparationTime())
                        .append(" minut, obrázek: ").append(entry.getValue().getImage()).append("\n");
            }
            return stringBuilder.toString();
        }
    }
}
