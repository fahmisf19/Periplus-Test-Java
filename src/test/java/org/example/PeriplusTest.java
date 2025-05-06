package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class PeriplusTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    private final String productTitle = "Good Girl";
    private final String productPrice = "Rp 325,000";
    private final String productId = "67886371";

    @Test(priority=1)
    void setupBrowser() {
        System.out.println("// 1. Setup browser");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;

        System.out.println("Setup browser completed");
    }

    @Test(priority=2)
    void navigateToWebsite() {
        System.out.println("// 2. Navigate to website");

        driver.navigate().to("https://www.periplus.com");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".logo.logo-new")));

        System.out.println("Navigate to website completed");
    }

    @Test(priority=3)
    void goToLoginPage() {
        System.out.println("// 3. Go to login page");

        WebElement signInButton = driver.findElement(By.xpath("//a[normalize-space()='Sign In']"));
        signInButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".login-content")));

        System.out.println("Go to login page completed");
    }

    @Test(priority=4)
    void performLogin() {
        System.out.println("// 4. Perform login");

        WebElement emailField = driver.findElement(By.xpath("//input[@name='email']"));
        emailField.sendKeys("fahmi.sfatalnuck@gmail.com");

        WebElement passwordField = driver.findElement(By.xpath("//input[@id='ps']"));
        passwordField.sendKeys("TestAccount123!");

        WebElement loginButton = driver.findElement(By.xpath("//input[@id='button-login']"));
        loginButton.click();

        wait.until(ExpectedConditions.urlToBe("https://www.periplus.com/account/Your-Account"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "https://www.periplus.com/account/Your-Account",
                "Login failed or redirected to wrong page");

        System.out.println("Successfully logged in and verified account page URL");

        WebElement logoAnchor = driver.findElement(By.cssSelector("div.logo.logo-new > a"));
        js.executeScript("arguments[0].click();", logoAnchor);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".logo.logo-new")));

        System.out.println("Perform login completed");
    }

    @Test(priority=5)
    void addSpecificBookToCart() {
        System.out.println("// 5. Add specific book");

        String addToCartScript = "update_total(" + productId + ")";
        js.executeScript(addToCartScript);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Add specific book completed");
    }

    @Test(priority=6)
    void verifyCart() {
        System.out.println("// 6. Verify cart");

        String countText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cart_total"))).getText();
        Assert.assertEquals(countText, "1", "Cart count should be 1");

        WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(By.id("show-your-cart")));
        new Actions(driver).moveToElement(cartIcon).perform();

        WebElement shoppingItem = wait.until(ExpectedConditions.elementToBeClickable(By.className("shopping-item")));

        String cartItem = shoppingItem.findElement(By.className("shopping-list")).getText();

        Assert.assertTrue(cartItem.contains(productTitle), "Cart should contain the product title: " + productTitle);
        Assert.assertTrue(cartItem.contains(productPrice), "Cart should contain the product price: " + productPrice);

        System.out.println("Verify cart completed");
    }

    @Test(priority=7)
    public void tearDown() {
        System.out.println("// 7. Cleanup");

        if (driver != null) {
            driver.quit();
            System.out.println("Cleanup completed");
        }
    }
}