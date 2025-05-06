package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object representing the Periplus login page
 * Contains methods to interact with elements on the login page
 */
public class LoginPage extends BasePage {
    public final String loginUrlFragment = "/account/Login";
    private final By loginFormLocator = By.cssSelector(".login-content");
    private final By emailFieldLocator = By.xpath("//input[@name='email']");
    private final By passwordFieldLocator = By.xpath("//input[@id='ps']");
    private final By loginButtonLocator = By.xpath("//input[@id='button-login']");

    /**
     * Constructor for LoginPage
     *
     * @param driver The WebDriver instance to use for this page
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Checks if the browser is currently on the login page
     *
     * @return true if the current URL contains the login page fragment, false otherwise
     */
    public boolean isAtLoginPage() {
        return isAtExpectedPage(loginUrlFragment);
    }

    /**
     * Checks if the login form is displayed on the page
     *
     * @return true if the login form is displayed, false otherwise
     */
    public boolean isLoginFormDisplayed() {
        return isElementDisplayed(loginFormLocator);
    }

    /**
     * Checks if the email field is displayed on the login form
     *
     * @return true if the email field is displayed, false otherwise
     */
    public boolean isEmailFieldDisplayed() {
        return isElementDisplayed(emailFieldLocator);
    }

    /**
     * Checks if the password field is displayed on the login form
     *
     * @return true if the password field is displayed, false otherwise
     */
    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(passwordFieldLocator);
    }

    /**
     * Checks if the login button is displayed on the login form
     *
     * @return true if the login button is displayed, false otherwise
     */
    public boolean isLoginButtonDisplayed() {
        return isElementDisplayed(loginButtonLocator);
    }

    /**
     * Performs the login actions
     *
     * @param email The email address to use for login
     * @param password The password to use for login
     */
    public void performLogin(String email, String password) {
        type(emailFieldLocator, email);
        type(passwordFieldLocator, password);
        click(loginButtonLocator);
        logger.info("Performed login with: {}", email);
    }

    /**
     * Checks if login warning message is displayed
     *
     * @return true if warning message is displayed, false otherwise
     */
    public boolean isLoginWarningDisplayed() {
        return isElementDisplayed(By.cssSelector(".warning"));
    }
}
