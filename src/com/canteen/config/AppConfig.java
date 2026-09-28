package com.canteen.config;

import java.io.File;

public class AppConfig {
    public static final String APP_NAME = "CampusBite College Canteen";
    public static final int DEFAULT_PORT = 8080;
    public static final String CURRENCY_SYMBOL = "₹";
    public static final String DATA_DIR = System.getProperty("user.dir") + File.separator + "data";
    public static final String USERS_FILE = DATA_DIR + File.separator + "users.dat";
    public static final String MENU_FILE = DATA_DIR + File.separator + "menu.dat";
    public static final String ORDERS_FILE = DATA_DIR + File.separator + "orders.dat";
    public static final String SESSION_COOKIE_NAME = "CAMPUS_CANTEEN_SESSION";
    public static final int SESSION_MAX_AGE_SECONDS = 3600 * 24; // 24 hours
}
