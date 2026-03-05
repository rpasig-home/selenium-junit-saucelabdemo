package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
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

  public String cartCountOrNull() {
    List<WebElement> badges = driver.findElements(cartBadge);
    return badges.isEmpty() ? null : badges.get(0).getText();
  }

  public ShoppingCartPage goToCart() {
    driver.findElement(cartLink).click();
    return new ShoppingCartPage(driver);
  }

  // Internal model for inventory items

  private static class Item {
    final WebElement root;
    final String name;
    final BigDecimal price;

    Item(WebElement root, String name, BigDecimal price) {
      this.root = root;
      this.name = name;
      this.price = price;
    }
  }

  private List<Item> readItems() {

    List<WebElement> roots = driver.findElements(inventoryItems);

    if (roots.isEmpty()) {
      throw new IllegalStateException("No inventory items found");
    }

    List<Item> items = new ArrayList<>();

    for (WebElement root : roots) {

      String name = root.findElement(itemName).getText().trim();

      String rawPrice = root.findElement(itemPrice)
          .getText()
          .replace("$", "")
          .trim();

      BigDecimal price = new BigDecimal(rawPrice);

      items.add(new Item(root, name, price));
    }

    return items;
  }

  private void addToCart(Item item) {
    item.root.findElement(addToCartButton).click();
  }

  // Price selection logic for inventory itemm

  public String addHighestPricedItemToCart() {

    Item max = readItems().stream()
        .max(Comparator.comparing(i -> i.price))
        .orElseThrow();

    addToCart(max);
    return max.name;
  }

  public String addLowestPricedItemToCart() {

    Item min = readItems().stream()
        .min(Comparator.comparing(i -> i.price))
        .orElseThrow();

    addToCart(min);
    return min.name;
  }
}
