package org.example.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base page class that contains common methods and functionality
 * for all page objects in the test framework.
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);

    /**
     * Constructor for BasePage that initializes WebDriver, WebDriverWait, and JavascriptExecutor
     *
     * @param driver The WebDriver instance to use for this page
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    /**
     * Waits for an element to be visible in the DOM
     *
     * @param locator The By locator to find the element
     * @return The WebElement once it is visible
     * @throws org.openqa.selenium.TimeoutException if element is not visible within the timeout period
     */
    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for an element to be visible in the DOM
     *
     * @param element The WebElement to wait for
     * @return The WebElement once it is visible
     * @throws org.openqa.selenium.TimeoutException if element is not visible within the timeout period
     */
    protected WebElement waitForElementVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits for an element to be clickable
     *
     * @param locator The By locator to find the element
     * @return The WebElement once it is clickable
     * @throws org.openqa.selenium.TimeoutException if element is not clickable within the timeout period
     */
    protected WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for an element to be clickable
     *
     * @param element The WebElement to wait for
     * @return The WebElement once it is clickable
     * @throws org.openqa.selenium.TimeoutException if element is not clickable within the timeout period
     */
    protected WebElement waitForElementClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Clicks on an element after waiting for it to be clickable
     *
     * @param locator The By locator to find the element to click
     * @throws org.openqa.selenium.TimeoutException if element is not clickable within the timeout period
     */
    protected void click(By locator) {
        waitForElementClickable(locator).click();
        logger.info("Clicked on element: {}", locator);
    }

    /**
     * Clicks on an element using JavaScript
     * Useful for elements that are difficult to click using regular Selenium click
     *
     * @param element The WebElement to click
     */
    protected void jsClick(WebElement element) {
        js.executeScript("arguments[0].click();", element);
        logger.info("Clicked on element: {}", element);
    }

    /**
     * Types text into an element after waiting for it to be visible
     *
     * @param locator The By locator to find the element
     * @param text The text to type into the element
     * @throws org.openqa.selenium.TimeoutException if element is not visible within the timeout period
     */
    protected void type(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
        logger.info("Typed '{}' into element: {}", text, locator);
    }

    /**
     * Hovers over an element
     *
     * @param element The WebElement to hover over
     */
    protected void hoverOverElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        logger.info("Hover over element: {}", element);
    }

    /**
     * Gets the text of an element after waiting for it to be visible
     *
     * @param locator The By locator to find the element
     * @return The text content of the element
     * @throws org.openqa.selenium.TimeoutException if element is not visible within the timeout period
     */
    protected String getText(By locator) {
        return waitForElementVisible(locator).getText();
    }

    /**
     * Gets the current URL of the browser
     *
     * @return The current URL as a String
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Checks if the current page URL contains the expected fragment
     *
     * @param expectedUrlFragment A distinctive part of the URL that should be present
     * @return true if the current URL contains the expected fragment, false otherwise
     */
    protected boolean isAtExpectedPage(String expectedUrlFragment) {
        String currentUrl = getCurrentUrl();
        boolean isAtCorrectPage = currentUrl.contains(expectedUrlFragment);

        logger.info("Page verification: Current URL '{}' {} the expected fragment '{}'",
                currentUrl,
                isAtCorrectPage ? "contains" : "does not contain",
                expectedUrlFragment);

        return isAtCorrectPage;
    }

    /**
     * Checks if an element is displayed after waiting for it to be visible
     * Returns false instead of throwing an exception if the element is not found
     *
     * @param locator The By locator to find the element
     * @return true if the element is displayed, false if not displayed or not found
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return waitForElementVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if an element is displayed after waiting for it to be visible
     * Returns false instead of throwing an exception if the element is not found
     *
     * @param element The WebElement to check
     * @return true if the element is displayed, false if not displayed or not found
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return waitForElementVisible(element).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
