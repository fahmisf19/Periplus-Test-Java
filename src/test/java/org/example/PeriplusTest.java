package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class PeriplusTest {
    private static final Logger logger = LogManager.getLogger(PeriplusTest.class);

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    private final String productTitle = "Cover Story";
    private final String productTitleLink = "cover-story";
    private final String productPrice = "Rp 218,000";
    private final String productId = "67821264";

    private final String email = "testperiplus.fahmi@gmail.com";
    private final String password = "bdd2fe4b82b342cb";

    @BeforeTest
    public void setup() {
        logger.info("====== PERIPLUS TEST EXECUTION STARTING ======");

        logger.info("Setting up browser and WebDriver");

        driver = new ChromeDriver();
        Assert.assertNotNull(driver, "WebDriver should be initialized");

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Assert.assertNotNull(wait, "WebDriverWait should be initialized");

        js = (JavascriptExecutor) driver;
        Assert.assertNotNull(js, "JavascriptExecutor should be initialized");
    }

    @AfterTest
    public void tearDown() {
        logger.info("Cleaning up - closing browser and releasing resources");
        if (driver != null) {
            driver.quit();
        }

        logger.info("====== PERIPLUS TEST EXECUTION COMPLETED ======");
    }

    @Test
    void testPeriplus() {
        try {
            // Step 1: Navigate to website
            logger.info("STEP 1: Navigate to Periplus website");

            driver.navigate().to("https://www.periplus.com");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".logo.logo-new")));

            String pageTitle = driver.getTitle();
            Assert.assertTrue(pageTitle.contains("Periplus Online Bookstore"),
                    "Page title should contain 'Periplus Online Bookstore' but was: " + pageTitle);

            String currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl, "https://www.periplus.com/",
                    "URL should be the Periplus homepage but was: " + currentUrl);

            logger.info("Navigate to website completed");

            // Step 2: Navigate to login page
            logger.info("STEP 2: Navigate to login page");

            WebElement signInButton = driver.findElement(By.xpath("//a[normalize-space()='Sign In']"));
            Assert.assertTrue(signInButton.isDisplayed(),
                    "Sign In button should be visible");

            signInButton.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".login-content")));

            WebElement loginForm = driver.findElement(By.cssSelector(".login-content"));
            Assert.assertTrue(loginForm.isDisplayed(),
                    "Login form should be displayed");

            WebElement emailField = driver.findElement(By.xpath("//input[@name='email']"));
            WebElement passwordField = driver.findElement(By.xpath("//input[@id='ps']"));
            WebElement loginButton = driver.findElement(By.xpath("//input[@id='button-login']"));

            Assert.assertTrue(emailField.isDisplayed(),
                    "Email field should be visible on the login page");
            Assert.assertTrue(passwordField.isDisplayed(),
                    "Password field should be visible on the login page");
            Assert.assertTrue(loginButton.isDisplayed(),
                    "Login button should be visible on the login page");

            logger.info("Go to login page completed");

            // Step 3: Perform login
            logger.info("STEP 3: Perform login with test credentials");

            emailField.sendKeys(email);
            passwordField.sendKeys(password);
            loginButton.click();

            wait.until(ExpectedConditions.urlToBe("https://www.periplus.com/account/Your-Account"));

            currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl,
                    "https://www.periplus.com/account/Your-Account",
                    "After login, should redirect to account page but was: " + currentUrl);

            logger.info("Successfully logged in and verified account page URL");

            // Step 4: Navigate back to homepage
            logger.info("STEP 4: Navigate back to homepage");

            WebElement logoAnchor = driver.findElement(By.cssSelector("div.logo.logo-new > a"));
            js.executeScript("arguments[0].click();", logoAnchor);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".logo.logo-new")));

            currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl,
                    "https://www.periplus.com/",
                    "URL should be the Periplus homepage");

            logger.info("Navigate to website completed");

            // Step 5: Add specific book to cart
            logger.info("STEP 5: Add specific book to cart via JavaScript");

            WebElement targetProduct = driver.findElement(
                    By.xpath("//h3/a[contains(text(), '" + productTitle + "')]/ancestor::div[@class='single-product']"));

            Actions actions = new Actions(driver);
            actions.moveToElement(targetProduct).perform();

            WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                    targetProduct.findElement(By.cssSelector(".addtocart"))));
            actions.click(addToCartButton).perform();

            wait.until(ExpectedConditions.textToBe(By.id("cart_total"), "1"));

            logger.info("Add specific book completed");

            // Step 6: Verify cart contents
            logger.info("STEP 6: Verify cart contents");

            WebElement cartCount = driver.findElement(By.id("cart_total"));
            Assert.assertEquals(cartCount.getText(),
                    "1",
                    "Cart count should be 1 but was: " + cartCount.getText());

            WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(By.id("show-your-cart")));
            new Actions(driver).moveToElement(cartIcon).perform();

            WebElement shoppingItem = wait.until(ExpectedConditions.elementToBeClickable(By.className("shopping-item")));
            Assert.assertTrue(shoppingItem.isDisplayed(),
                    "Shopping cart dropdown should be displayed");

            WebElement dropdownHeader = shoppingItem.findElement(By.className("dropdown-cart-header"));
            Assert.assertTrue(dropdownHeader.getText().contains("1 ITEM(S)"),
                    "Cart header should show '1 item' but showed: " + dropdownHeader.getText());

            WebElement shoppingList = shoppingItem.findElement(By.className("shopping-list"));
            String cartItem = shoppingList.getText();

            Assert.assertTrue(cartItem.contains(productPrice),
                    "Cart should contain the product price '" + productPrice + "' but contained: " + cartItem);

            WebElement productLink = shoppingList.findElement(By.cssSelector("a.cart-img"));
            String hrefAttribute = productLink.getAttribute("href");
            Assert.assertTrue(hrefAttribute.contains(productId) || hrefAttribute.contains(productTitleLink),
                    "Product link should point to " + productTitleLink + " but was: " + hrefAttribute);

            logger.info("Verify cart completed");

            logger.info("Test completed successfully");
        }
       catch (Exception e) {
           logger.error("Test failed with exception: " + e.getMessage());
           Assert.fail("Test failed with exception: " + e.getMessage());
       }
    }
}