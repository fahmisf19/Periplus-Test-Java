package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static final Logger logger = LogManager.getLogger(PropertyReader.class);
    private static Properties properties = new Properties();
    private static boolean isInitialized = false;

    private PropertyReader() {
    }

    /**
     * Initializes the properties if not already loaded
     */
    private static void initialize() {
        if (!isInitialized) {
            try (InputStream input = PropertyReader.class.getClassLoader().getResourceAsStream("test.properties")) {
                if (input == null) {
                    logger.error("Unable to find test.properties file");
                    throw new RuntimeException("Unable to find test.properties file");
                }
                properties.load(input);
                isInitialized = true;
                logger.info("Properties file loaded successfully");
            } catch (IOException e) {
                logger.error("Failed to load properties file", e);
                throw new RuntimeException("Failed to load properties file", e);
            }
        }
    }

    /**
     * Gets a property value by key
     *
     * @param key The property key
     * @return The property value or null if not found
     */
    public static String getProperty(String key) {
        initialize();
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found: {}", key);
        }
        return value;
    }

    /**
     * Gets a property value by key with a default value
     *
     * @param key The property key
     * @param defaultValue The default value to return if key not found
     * @return The property value or defaultValue if not found
     */
    public static String getProperty(String key, String defaultValue) {
        initialize();
        return properties.getProperty(key, defaultValue);
    }
}
