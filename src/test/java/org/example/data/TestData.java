package org.example.data;

import org.example.utils.PropertyReader;

public class TestData {
    public static final String USER_EMAIL = PropertyReader.getProperty("user.email");
    public static final String USER_PASSWORD = PropertyReader.getProperty("user.password");

    public static final String PRODUCT_TITLE = PropertyReader.getProperty("book.title");
    public static final String PRODUCT_PRICE = PropertyReader.getProperty("book.price");
    public static final String PRODUCT_ID = PropertyReader.getProperty("book.id");
    public static final String PRODUCT_TITLE_LINK = PropertyReader.getProperty("book.title_link");
}
