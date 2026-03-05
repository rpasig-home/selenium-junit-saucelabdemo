package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.List;

public class InventoryPage {

  private final WebDriver driver;

  private final By inventoryItems = By.cssSelector(".inventory_item");
  private final By itemPrice = By.cssSelector(".inventory_item_price");
  private final By itemName = By.cssSelector(".inventory_item_name");
  private final By addToCartButton = By.cssSelector("button.btn_inventory");

  private final By cartBadge = By.cssSelector(".shopping_cart_badge");
  private final By cartLink = By.cssSelector(".shopping_cart_link");

  public InventoryPage(WebDriver driver) {
    this.driver = driver;
  }

  public boolean hasItems() {
    return !driver.findElements(inventoryItems).isEmpty();
  }

  public String addHighestPricedItemToCart() {

    List<WebElement> items = driver.findElements(inventoryItems);

    BigDecimal maxPrice = null;
    WebElement maxItem = null;

    for (WebElement item : items) {

      String priceText = item.findElement(itemPrice)
          .getText()
          .replace("$", "")
          .trim();

      BigDecimal price = new BigDecimal(priceText);

      if (maxPrice == null || price.compareTo(maxPrice) > 0) {
        maxPrice = price;
        maxItem = item;
      }
    }

    if (maxItem == null) {
      throw new IllegalStateException("No max item found");
    }

    String name = maxItem.findElement(itemName).getText();
    maxItem.findElement(addToCartButton).click();

    return name;
  }

  public String cartCountOrNull() {
    List<WebElement> badges = driver.findElements(cartBadge);
    return badges.isEmpty() ? null : badges.get(0).getText();
  }

  public ShoppingCartPage goToCart() {
    driver.findElement(cartLink).click();
    return new ShoppingCartPage(driver);
  }
}