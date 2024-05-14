import com.engeto.ja.restaurant.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws RestaurantException {

        CookBook cookBook = new CookBook();
        Orders orders = new Orders();

//  1. Nacteni stavu evidence z disku
       loadDataFromDisk(cookBook, orders);

//  2. Priprava testovacich dat
        addDishToCookBook(cookBook,"Kuřecí řízek obalovaný 150 g", BigDecimal.valueOf(150), 10);
        addDishToCookBook(cookBook,"Hranolky 150 g", BigDecimal.valueOf(50), 10);
        addDishToCookBook(cookBook,"Pstruh na víně 200 g", BigDecimal.valueOf(180), 30);
        addDishToCookBook(cookBook,"Kofola 0,5 l", BigDecimal.valueOf(30), 5);

/*
        addDishToCookBook(cookBook, "Svíčková na smetaně", BigDecimal.valueOf(180), 30);
        addDishToCookBook(cookBook, "Guláš", BigDecimal.valueOf(160), 25);
        addDishToCookBook(cookBook, "Rajská omáčka", BigDecimal.valueOf(150), 20);
        addDishToCookBook(cookBook, "Řízek", BigDecimal.valueOf(140), 15);
        addDishToCookBook(cookBook, "Palačinky", BigDecimal.valueOf(120), 10);
        addDishToCookBook(cookBook, "Pizza", BigDecimal.valueOf(200), 20);
        addDishToCookBook(cookBook, "Hamburger", BigDecimal.valueOf(150), 15);
        addDishToCookBook(cookBook, "Hot Dog", BigDecimal.valueOf(100), 10);
        addDishToCookBook(cookBook, "Salát Caesar", BigDecimal.valueOf(130), 10);
        addDishToCookBook(cookBook, "Tiramisu", BigDecimal.valueOf(110), 15);
        addDishToCookBook(cookBook, "Cheesecake", BigDecimal.valueOf(120), 20);
        addDishToCookBook(cookBook, "Brownie", BigDecimal.valueOf(100), 15);
        addDishToCookBook(cookBook, "Coca Cola", BigDecimal.valueOf(30), 0);
        addDishToCookBook(cookBook, "Pepsi", BigDecimal.valueOf(30), 0);
        addDishToCookBook(cookBook, "Sprite", BigDecimal.valueOf(30), 0);
        addDishToCookBook(cookBook, "Fanta", BigDecimal.valueOf(30), 0);
        addDishToCookBook(cookBook, "Minerálka", BigDecimal.valueOf(25), 0);
        addDishToCookBook(cookBook, "Espresso", BigDecimal.valueOf(40), 5);
        addDishToCookBook(cookBook, "Cappuccino", BigDecimal.valueOf(50), 5);
        addDishToCookBook(cookBook, "Čaj", BigDecimal.valueOf(30), 5);
        addDishToCookBook(cookBook, "Vegetariánská pizza", BigDecimal.valueOf(200), 20);
        addDishToCookBook(cookBook, "Vegetariánský burger", BigDecimal.valueOf(150), 15);
        addDishToCookBook(cookBook, "Salát s avokádem", BigDecimal.valueOf(130), 10);
        addDishToCookBook(cookBook, "Tofu s rýží", BigDecimal.valueOf(120), 15);
        addDishToCookBook(cookBook, "Vegetariánské lasagne", BigDecimal.valueOf(160), 30);
*/
        createNewOrder(cookBook, orders, 15, 1, 2);
        createNewOrder(cookBook, orders, 15, 2, 2);
        createNewOrder(cookBook, orders, 15, 4, 2);

/*
        createNewOrder(cookBook, orders, 1, 1, 2,LocalDateTime.of(2024, 4, 12, 12, 37,25));
        createNewOrder(cookBook, orders, 1, 2, 1,LocalDateTime.of(2024, 4, 12, 12, 39,25));
        createNewOrder(cookBook, orders, 2, 3, 3,LocalDateTime.of(2024, 4, 12, 12, 40,25));
        createNewOrder(cookBook, orders, 2, 4, 2,LocalDateTime.of(2024, 4, 12, 13, 47,25));
        createNewOrder(cookBook, orders, 3, 5, 1);
        createNewOrder(cookBook, orders, 4, 6, 2);
        createNewOrder(cookBook, orders, 4, 7, 3);
        createNewOrder(cookBook, orders, 4, 8, 1);
        createNewOrder(cookBook, orders, 5, 9, 2);
        createNewOrder(cookBook, orders, 6, 10, 3);
*/


        setOrderFulfilmentTime(orders.getOrders(), 2);
        createNewOrder(cookBook, orders, 2, 3, 1);

        orders.printOrders();

//  3. Vypis celkove ceny konzumace pro stul c.15
        printTotalPriceForTable(orders, 15);

        saveDataToDisk(cookBook, orders);

        printUnfinishedOrdersCount(orders);
        printOrdersSummarySortedByOrderedTime(orders);
        printAverageOrderProcessingTime (orders);
        printUniqueDishesOrderedToday(orders);
        printOrdersForTable(orders,15);


        setOrderFulfilmentTime(orders.getOrders(), 3);
        orders.getOrders().get(3).setAsPaid();

        saveTableSummaryToFile (orders, 15);
        saveTableSummaryToFile (orders, 4);

    }

    public static void addDishToCookBook(CookBook cookBook, String title, BigDecimal price, int preparationTime) {
        System.out.println("Přidávám jídlo: " + title);
        try {
            cookBook.addDish(new Dish(title, price, preparationTime));
        } catch (RestaurantException e) {
            System.err.println("Nastala chyba při přidávání jídla:\n" + e.getLocalizedMessage());
        }
    }

    public static void createNewOrder(CookBook cookBook, Orders orders, int tableNumber, int dishId, int quantity) {
        System.out.println("Vytvářím novou objednávku pro stůl číslo " + tableNumber + ": " + quantity + "x jídlo s ID " + dishId);
        try {
            orders.addOrder(new Order(cookBook.getDishById(dishId), quantity,tableNumber));
        } catch (RestaurantException e) {
            System.err.println("Nastala chyba při vytváření objednávky:\n" + e.getLocalizedMessage());
        }
    }

    public static void createNewOrder(CookBook cookBook, Orders orders, int tableNumber, int dishId, int quantity, LocalDateTime orderedTime) {
        System.out.println("Vytvářím novou objednávku pro stůl číslo " + tableNumber + ": " + quantity + "x jídlo s ID " + dishId + " s časem objednání " + orderedTime);
        try {
            orders.addOrder(new Order(cookBook.getDishById(dishId), quantity, orderedTime,tableNumber));
        } catch (RestaurantException e) {
            System.err.println("Nastala chyba při vytváření objednávky:\n" + e.getLocalizedMessage());
        }
    }

    private static void setOrderFulfilmentTime(List<Order> orders, int i){
        try {
            orders.get(i).setFulfilmentTime();
        } catch (RestaurantException e) {
            System.err.println("Nastala chyba při nastavování času splnění objednávky:\n" + e.getLocalizedMessage());
        }
    }

    public static void printOrdersSummarySortedByOrderedTime(Orders orders) {
        RestaurantManager restaurantManager = new RestaurantManager();
        try {
            System.out.println("Seznam objednávek seřazený podle času objednání:\n" + restaurantManager.getOrdersSummarySortedByOrderedTime(orders.getOrders()));
        } catch (RestaurantException e) {
            System.err.println("Nastala chyba při zpracování objednávek:\n" + e.getLocalizedMessage());
        }
    }

    public static void printTotalPriceForTable (Orders orders, int tableNumber) {
        RestaurantManager restaurantManager = new RestaurantManager();
        try {
            System.out.println("Celková cena konzumace pro stůl číslo " + tableNumber + ": " + restaurantManager.getTotalPriceForTable(orders.getOrders(), tableNumber) + " Kč");
        } catch (RestaurantException e) {
            System.err.println("Nastala chyba při výpočtu ceny konzumace:\n" + e.getLocalizedMessage());
        }
    }

//  4. Vypis vsech udaju ziskanych z metod
    public static void printUnfinishedOrdersCount (Orders orders) {
        RestaurantManager restaurantManager = new RestaurantManager();
        System.out.println("Počet nevyřízených objednávek: " + restaurantManager.getUnfinishedOrdersCount(orders.getOrders()));
    }

    public static void printAverageOrderProcessingTime (Orders orders) {
        RestaurantManager restaurantManager = new RestaurantManager();
        System.out.println("Průměrná doba zpracování objednávek: " + restaurantManager.getAverageOrderProcessingTime(orders.getOrders()) + " minut");
    }

    public static void printUniqueDishesOrderedToday (Orders orders) {
        RestaurantManager restaurantManager = new RestaurantManager();
        System.out.println("Seznam jídel, která byla dnes objednána:\n" + restaurantManager.getUniqueDishesOrderedToday(orders.getOrders()));
    }

    public static void printOrdersForTable (Orders orders, int tableNumber) {
        RestaurantManager restaurantManager = new RestaurantManager();
        try {
            System.out.println("Seznam objednávek pro stůl číslo " + tableNumber + ":\n" + restaurantManager.getOrdersForTable(orders.getOrders(), tableNumber));
        } catch (RestaurantException e) {
            System.err.println("Nastala chyba při zpracování objednávek:\n" + e.getLocalizedMessage());
        }
    }

//  5. Zmenena data - ulozeni na disk
    public static void saveDataToDisk(CookBook cookBook, Orders orders) {
        FileOperations fileOperations = new FileOperations();
        String fileNameOrders = Settings.getFileNameOrders();
        String fileNameCookBook = Settings.getFileNameCookBook();

        if (cookBook.getDishesCount() == 0) {
            System.err.println("Nelze uložit prázdný jídelníček.");
        }  else {
            try {
                fileOperations.saveCookBookToFile(cookBook, fileNameCookBook);
            } catch (RestaurantException e) {
                System.err.println("Nastala chyba při ukládání jídelníčku:\n" + e.getLocalizedMessage());
            }
        }

        if (orders.getOrders().isEmpty()) {
            System.err.println("Nelze uložit prázdný seznam objednávek.");
        }  else {
            try {
                fileOperations.saveOrdersToFile(orders.getOrders(), cookBook, fileNameOrders);
            } catch (RestaurantException e) {
                System.err.println("Nastala chyba při ukládání objednávek:\n" + e.getLocalizedMessage());
            }
        }
    }

//  6. Opetovne nacteni dat z disku
    public static void loadDataFromDisk(CookBook cookBook, Orders orders) {
        FileOperations fileOperations = new FileOperations();
        String fileNameOrders = Settings.getFileNameOrders();
        String fileNameCookBook = Settings.getFileNameCookBook();
        try {
            fileOperations.loadCookBookFromFile(cookBook, fileNameCookBook);
        } catch (RestaurantException e) {
            System.err.println("Nastala chyba při načítání jídelníčku:\n" + e.getLocalizedMessage());
        }
        try {
            fileOperations.loadOrdersFromFile(orders.getOrders(), cookBook, fileNameOrders);
        } catch (RestaurantException e) {
            System.err.println("Nastala chyba při načítání objednávek:\n" + e.getLocalizedMessage());
        }
    }

    public static void saveTableSummaryToFile(Orders orders, int tableNumber) {
        RestaurantManager restaurantManager = new RestaurantManager();
        FileOperations fileOperations = new FileOperations();
        String fileNameSummary = Settings.getFileNameSummary() + String.format("%02d", tableNumber) + ".txt";
        try {
            fileOperations.saveTableSummaryToFile(restaurantManager.getOrdersForTable(orders.getOrders(), tableNumber), fileNameSummary);
        } catch (RestaurantException e) {
            System.err.println("Nastala chyba při zpracování objednávek:\n" + e.getLocalizedMessage());
        }
    }
}