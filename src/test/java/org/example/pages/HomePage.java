package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page object representing the Periplus homepage
 * Contains methods to interact with elements on the home page
 */
public class HomePage extends BasePage {
    public final String url = "https://www.periplus.com/";
    private final By logoLocator = By.cssSelector(".logo.logo-new");
    private final By signInButtonLocator = By.xpath("//a[normalize-space()='Sign In']");

    private final By addToCartButtonLocator = By.cssSelector(".product-action .addtocart");
    private final By cartCountLocator = By.id("cart_total");
    private final By cartIconLocator = By.id("show-your-cart");
    private final By shoppingItemLocator = By.className("shopping-item");
    private final By shoppingListLocator = By.className("shopping-list");

    /**
     * Constructor for HomePage
     *
     * @param driver The WebDriver instance to use for this page
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the Periplus homepage
     * Waits for the logo to be visible before proceeding
     */
    public void navigateTo() {
        driver.navigate().to(url);
        waitForElementVisible(logoLocator);
        logger.info("Navigated to Periplus homepage");
    }

    /**
     * Checks if the browser is currently on the homepage
     *
     * @return true if the current URL matches the homepage URL, false otherwise
     */
    public boolean isAtHomePage() {
        return isAtExpectedPage(url);
    }

    /**
     * Clicks on the sign-in button to navigate to the login page
     *
     * @return A new LoginPage object representing the page after clicking sign in
     */
    public LoginPage clickSignIn() {
        click(signInButtonLocator);
        logger.info("Clicked on Sign In button");
        return new LoginPage(driver);
    }

    /**
     * Finds a product on the page by its title
     *
     * @param productTitle The title of the product to find
     * @return The WebElement representing the product
     * @throws org.openqa.selenium.TimeoutException if the product is not found within the timeout period
     */
    public WebElement findProduct(String productTitle) {
        By productLocator = By.xpath("//h3/a[contains(text(), '" + productTitle + "')]/ancestor::div[@class='single-product']");
        WebElement product = waitForElementVisible(productLocator);
        logger.info("Found product: {}", productTitle);
        return product;
    }

    /**
     * Adds a specific product to the cart with improved hover handling
     *
     * @param productTitle The title of the product to add to the cart
     */
    public void addProductToCart(String productTitle) {
        WebElement product = findProduct(productTitle);

        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", product);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        hoverOverElement(product);

        WebElement addToCartButton = product.findElement(addToCartButtonLocator);
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));

        new Actions(driver).moveToElement(addToCartButton).click().perform();
        wait.until(ExpectedConditions.textToBe(cartCountLocator, "1"));

        logger.info("Added product to cart: {}", productTitle);
    }

    /**
     * Gets the current cart count displayed on the page
     *
     * @return The cart count as a String
     */
    public String getCartCount() {
        return getText(cartCountLocator);
    }

    /**
     * Opens the cart dropdown by hovering over the cart icon
     */
    public void openCartDropdown() {
        WebElement cartIcon = waitForElementClickable(cartIconLocator);
        hoverOverElement(cartIcon);
        logger.info("Opened cart dropdown");
    }

    /**
     * Checks if the cart dropdown is displayed
     *
     * @return true if the cart dropdown is displayed, false otherwise
     */
    public boolean isCartDropdownDisplayed() {
        openCartDropdown();
        return isElementDisplayed(shoppingItemLocator);
    }

    /**
     * Checks if a specific product is in the cart by verifying its price and ID/link
     *
     * @param productPrice The expected price of the product
     * @param productId The expected ID of the product in the URL
     * @param productTitleLink The expected title link of the product in the URL
     * @return true if the product is in the cart with matching price and ID/link, false otherwise
     */
    public boolean isProductInCart(String productPrice, String productId, String productTitleLink) {
        openCartDropdown();

        WebElement shoppingItem = waitForElementVisible(shoppingItemLocator);
        WebElement shoppingList = shoppingItem.findElement(shoppingListLocator);
        String cartItem = shoppingList.getText();

        WebElement productLink = shoppingList.findElement(By.cssSelector("a.cart-img"));
        String hrefAttribute = productLink.getAttribute("href");

        boolean priceMatch = cartItem.contains(productPrice);
        boolean idMatch = hrefAttribute.contains(productId) || hrefAttribute.contains(productTitleLink);

        logger.info("Price match: {}, ID match: {}", priceMatch, idMatch);

        return priceMatch && idMatch;
    }
}
