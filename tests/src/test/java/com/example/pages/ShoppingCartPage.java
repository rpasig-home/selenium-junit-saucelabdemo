package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ShoppingCartPage {

  private final WebDriver driver;

  private final By cartItems = By.cssSelector(".cart_item");
  private final By itemName = By.cssSelector(".inventory_item_name");

  public ShoppingCartPage(WebDriver driver) {
    this.driver = driver;
  }

  public int itemCount() {
    return driver.findElements(cartItems).size();
  }

  public boolean containsItemNamed(String name) {

    List<WebElement> items = driver.findElements(cartItems);

    for (WebElement item : items) {
      String cartName = item.findElement(itemName).getText();
      if (name.equals(cartName)) {
        return true;
      }
    }

    return false;
  }
}