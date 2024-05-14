package com.engeto.ja.restaurant;

import java.io.*;
import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.*;

import java.util.Map;

import static com.engeto.ja.restaurant.Settings.getDelimiter;

public class FileOperations {

    public void saveOrdersToFile(List<Order> orders, CookBook cookBook, String fileName) throws RestaurantException {
        System.out.println("Ukládám objednávky do souboru " + fileName + "...");
        if (orders.isEmpty()) {
            throw new RestaurantException("Seznam objednávek je prázdný - nic k uložení!");
        }
        int lineCounter = 0;
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            for (Order order : orders) {
                if (cookBook.containsDish(order.getDish())) {
                    writer.println(
                        order.getTableNumber() + getDelimiter() +
                        cookBook.getDishId(order.getDish()) + getDelimiter() +
                        order.getQuantity() + getDelimiter() +
                        order.getOrderedTime() + getDelimiter() +
                        (order.getFulfilmentTime() != null ? order.getFulfilmentTime() : "null") + getDelimiter() +
                        (order.isPaid() ? "zaplaceno" : "nezaplaceno"));
                    lineCounter++;
                    } else {
                        throw new RestaurantException("Objednávka obsahuje neexistující jídlo!");
                    }
                }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("Soubor " + fileName + " nebyl nalezen!\n" + e.getLocalizedMessage());
        } catch (IOException e) {
            throw new RestaurantException("Nastala chyba při zápisu do souboru " + fileName + "!\n" + e.getLocalizedMessage());
        } finally {
            System.out.println(saveToFileStatusMsg(lineCounter));
        }
    }

    public void saveCookBookToFile(CookBook cookBook, String fileName) throws RestaurantException {
        System.out.println("Ukládám jídelníček do souboru " + fileName + "...");
        int lineCounter = 0;
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            for (Map.Entry<Integer, Dish> entry : cookBook.getDishes().entrySet()) {
                writer.println(
                        entry.getKey() + getDelimiter() +
                        entry.getValue().getTitle() + getDelimiter() +
                        entry.getValue().getPrice() + getDelimiter() +
                        entry.getValue().getPreparationTime() + getDelimiter() +
                        entry.getValue().getImage());
                lineCounter++;
            }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("Soubor " + fileName + " nebyl nalezen!\n" + e.getLocalizedMessage());
        } catch (IOException e) {
            throw new RestaurantException("Nastala chyba při zápisu do souboru " + fileName + "!\n" + e.getLocalizedMessage());
        } finally {
            System.out.println(saveToFileStatusMsg(lineCounter));
        }
    }

    public void loadOrdersFromFile(List<Order> orders, CookBook cookBook, String fileName) throws RestaurantException {
        System.out.println("Načítám objednávky ze souboru " + fileName + "...");
        orders.clear();
        int lineCounter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(getDelimiter());
                if (parts.length != 6) {
                    throw new RestaurantException("Chybný formát řádku " + (lineCounter + 1) + " v souboru " + fileName + "!");
                }
                int tableNumber = Integer.parseInt(parts[0]);
                int dishId = Integer.parseInt(parts[1]);
                int quantity = Integer.parseInt(parts[2]);
                LocalDateTime orderedTime = LocalDateTime.parse(parts[3]);
                LocalDateTime fulfilmentTime;
                if (parts[4].equals("null")) {
                    fulfilmentTime = null;
                } else {
                    fulfilmentTime = LocalDateTime.parse(parts[4]);
                }
                if (fulfilmentTime.isBefore(orderedTime) && fulfilmentTime != null) {
                    throw new RestaurantException("Čas splnění nesmí být dřív než datum objednání.");
                }
                boolean paid;
                if (parts[5].equals("zaplaceno")) {
                    paid = true;
                } else if (parts[5].equals("nezaplaceno")) {
                    paid = false;
                } else {
                    throw new RestaurantException("Chybný formát platby na řádku " + (lineCounter + 1) + " v souboru " + fileName + "!");
                }
                if (cookBook.getDishes().containsKey(dishId)) {
                    if (fulfilmentTime == null) {
                        orders.add(new Order(cookBook.getDishById(dishId), quantity, orderedTime, tableNumber, paid));
                    } else {
                        orders.add(new Order(cookBook.getDishById(dishId), quantity, orderedTime, fulfilmentTime, tableNumber, paid));
                    }
                    lineCounter++;
                } else {
                    throw new RestaurantException("Objednávka obsahuje neexistující jídlo!");
                }
            }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("Soubor " + fileName + " nebyl nalezen!\n" + e.getLocalizedMessage());
        } catch (NumberFormatException e) {
            throw new RestaurantException("Nesprávný formát čísla na řádku číslo: " + lineCounter + "!\n" + e.getLocalizedMessage());
        } catch (DateTimeParseException e) {
            throw new RestaurantException("Nesprávný formát data na řádku číslo: " + lineCounter + "!\n" + e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RestaurantException("Nastala chyba při načítání seznamu objednávek ze souboru " + fileName + " (na řádku číslo " + lineCounter + ")!\n" + e.getLocalizedMessage());
        } finally {
            System.out.println(loadFromFileStatusMsg(lineCounter));
        }
    }

    public void loadCookBookFromFile(CookBook cookBook, String fileName) throws RestaurantException {
        System.out.println("Načítám jídelníček ze souboru " + fileName + "...");
        int lineCounter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(getDelimiter());
                if (parts.length != 5) {
                    throw new RestaurantException("Chybný formát řádku " + (lineCounter + 1) + " v souboru " + fileName + "!");
                }
                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                BigDecimal price = new BigDecimal(parts[2]);
                int preparationTime = Integer.parseInt(parts[3]);
                String image = parts[4];
                cookBook.addDish(new Dish(title, price, preparationTime, image));
                lineCounter++;
            }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("Soubor " + fileName + " nebyl nalezen!\n" + e.getLocalizedMessage());
        } catch (NumberFormatException e) {
            throw new RestaurantException("Nesprávný formát čísla na řádku číslo: " + lineCounter + "!\n" + e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RestaurantException("Nastala chyba při načítání jídelníčku ze souboru " + fileName + " (na řádku číslo " + lineCounter + ")!\n" + e.getLocalizedMessage());
        } finally {
            System.out.println(loadFromFileStatusMsg(lineCounter));
        }
    }

    public void saveTableSummaryToFile (StringBuilder tableSummary, String fileName) throws RestaurantException {
        System.out.println("Ukládám souhrn objednávek pro stůl do souboru " + fileName + "...");
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            writer.println(tableSummary);
        } catch (FileNotFoundException e) {
            throw new RestaurantException("Soubor " + fileName + " nebyl nalezen!\n" + e.getLocalizedMessage());
        } catch (IOException e) {
            throw new RestaurantException("Nastala chyba při zápisu do souboru " + fileName + "!\n" + e.getLocalizedMessage());
        }
    }

    private String loadFromFileStatusMsg (int listSize) {
        String loadedStr, itemStr;
        switch (listSize) {
            case 0 -> {
                return "Žádné položky nebyly načteny.";
            }case 1 -> {
                loadedStr = "Načtena ";
                itemStr = " položka.";
            } case 2, 3, 4 -> {
                loadedStr = "Načteny ";
                itemStr = " položky.";
            }
            default -> {
                loadedStr = "Načteno ";
                itemStr = " položek.";
            }
        }
        return loadedStr + listSize + itemStr;
    }

    private String saveToFileStatusMsg (int listSize) {
        String savedStr, itemStr;
        switch (listSize) {
            case 0 -> {
                return "Žádné položky nebyly uloženy - soubor bude prázdný.";
            }case 1 -> {
                savedStr = "Uložena ";
                itemStr = " položka.";
            } case 2, 3, 4 -> {
                savedStr = "Uloženy ";
                itemStr = " položky.";
            }
            default -> {
                savedStr = "Uloženo ";
                itemStr = " položek.";
            }
        }
        return savedStr + listSize + itemStr;
    }

}
