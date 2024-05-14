package com.engeto.ja.restaurant;

public class Settings {

    private static final String DEFAULT_IMAGE = "blank";
    private static final String DATE_FORMAT = "d.M. H:m";
    private static final String TIME_FORMAT = "HH:mm";
    private static final String DELIMITER = "\t";
    private static final int MAX_TABLE_NUMBER = 19;
    private static final String FILE_NAME_ORDERS = "resources/orders.txt";
    private static final String FILE_NAME_COOKBOOK = "resources/cookbook.txt";
    private static final String FILE_NAME_SUMMARY = "resources/summaryTable";

    public static String getDefaultImage() {
        return DEFAULT_IMAGE;
    }

    public static String getDelimiter() {
        return DELIMITER;
    }

    public static String getDateFormat() {
        return DATE_FORMAT;
    }

    public static String getTimeFormat() {
        return TIME_FORMAT;
    }

    public static int getMaxTableNumber() {
        return MAX_TABLE_NUMBER;
    }

    public static String getFileNameOrders() {
        return FILE_NAME_ORDERS;
    }

    public static String getFileNameCookBook() {
        return FILE_NAME_COOKBOOK;
    }

    public static String getFileNameSummary() {
        return FILE_NAME_SUMMARY;
    }

}