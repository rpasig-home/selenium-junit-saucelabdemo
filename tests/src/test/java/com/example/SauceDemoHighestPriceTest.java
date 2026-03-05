package com.example;

import com.example.pages.LoginPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@Tag("external")
public class SauceDemoHighestPriceTest extends BaseSeleniumTest {

  @Test
  void addHighestPricedItemToCart_withoutSorting() {

    var inventory = new LoginPage(driver)
        .open("https://www.saucedemo.com/")
        .loginAs("standard_user", "secret_sauce");

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(d -> inventory.hasItems());

    String itemName = inventory.addHighestPricedItemToCart();
    log.info("Highest priced item selected: {}", itemName);

    assertEquals("1", inventory.cartCountOrNull());

    var cart = inventory.goToCart();

    wait.until(d -> cart.itemCount() >= 1);

    assertTrue(cart.containsItemNamed(itemName));
  }
}