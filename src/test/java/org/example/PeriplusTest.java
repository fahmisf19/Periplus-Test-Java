package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.data.TestData;
import org.example.pages.AccountPage;
import org.example.pages.HomePage;
import org.example.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class PeriplusTest {
    private static final Logger logger = LogManager.getLogger(PeriplusTest.class);

    private WebDriver driver;

    @BeforeTest
    public void setup() {
        logger.info("====== PERIPLUS TEST EXECUTION STARTING ======");

        logger.info("Setting up browser and WebDriver");
        driver = new ChromeDriver();
        Assert.assertNotNull(driver, "WebDriver should be initialized");

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
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

            HomePage homePage = new HomePage(driver);
            homePage.navigateTo();

            Assert.assertTrue(homePage.isAtHomePage(),
                    "Should be on the homepage with URL: " + homePage.url);

            // Step 2: Navigate to login page
            logger.info("STEP 2: Navigate to login page");

            LoginPage loginPage = homePage.clickSignIn();

            Assert.assertTrue(loginPage.isLoginFormDisplayed(),
                    "Login form should be displayed");
            Assert.assertTrue(loginPage.isEmailFieldDisplayed(),
                    "Email field should be visible on the login page");
            Assert.assertTrue(loginPage.isPasswordFieldDisplayed(),
                    "Password field should be visible on the login page");
            Assert.assertTrue(loginPage.isLoginButtonDisplayed(),
                    "Login button should be visible on the login page");

            // Step 3: Perform login
            logger.info("STEP 3: Perform login with test credentials");

            loginPage.performLogin(TestData.USER_EMAIL, TestData.USER_PASSWORD);

            Assert.assertFalse(loginPage.isLoginWarningDisplayed(),
                    "Login warning message should not be displayed when using valid credentials");

            // Step 4: Navigate back to homepage
            logger.info("STEP 4: Navigate back to homepage");

            AccountPage accountPage = new AccountPage(driver);
            homePage = accountPage.navigateToHomePage();

            Assert.assertTrue(homePage.isAtHomePage(),
                    "After navigating from account page, should be on homepage with URL: " + homePage.url);

            // Step 5: Add specific book to cart
            logger.info("STEP 5: Add specific book to cart");

            homePage.addProductToCart(TestData.PRODUCT_TITLE);

            // Step 6: Verify cart contents
            logger.info("STEP 6: Verify cart contents");

            Assert.assertEquals(homePage.getCartCount(), "1",
                    "Cart count should be 1 but was: " + homePage.getCartCount());

            Assert.assertTrue(homePage.isCartDropdownDisplayed(),
                    "Shopping cart dropdown should be displayed");

            Assert.assertTrue(homePage.isProductInCart(
                            TestData.PRODUCT_PRICE,
                            TestData.PRODUCT_ID,
                            TestData.PRODUCT_TITLE_LINK),
                    "Product should be in cart with correct price and ID");

            logger.info("Test completed successfully");
        }
       catch (Exception e) {
           logger.error("Test failed with exception: " + e.getMessage());
           Assert.fail("Test failed with exception: " + e.getMessage());
       }
    }
}