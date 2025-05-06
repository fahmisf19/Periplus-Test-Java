package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page object representing the Periplus account page
 * Contains methods to interact with elements on the account page
 */
public class AccountPage extends BasePage {
    public final String accountUrlFragment = "/account/Your-Account";
    private final By logoLocator = By.cssSelector("div.logo.logo-new > a");

    /**
     * Constructor for AccountPage
     *
     * @param driver The WebDriver instance to use for this page
     */
    public AccountPage(WebDriver driver) {
        super(driver);
        waitForElementVisible(logoLocator);
    }

    /**
     * Checks if the browser is currently on the account page
     *
     * @return true if the current URL contains the account page fragment, false otherwise
     */
    public boolean isAtAccountPage() {
        return isAtExpectedPage(accountUrlFragment);
    }

    /**
     * Navigates from the account page to the homepage by clicking the site logo
     *
     * @return A new HomePage object representing the homepage after navigation
     */
    public HomePage navigateToHomePage() {
        WebElement logoAnchor = driver.findElement(logoLocator);
        logoAnchor.click();
        logger.info("Clicked on logo to navigate to homepage");

        return new HomePage(driver);
    }
}
