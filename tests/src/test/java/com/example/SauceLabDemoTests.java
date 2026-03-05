package com.example;

import com.example.pages.InventoryPage;
import com.example.pages.LoginPage;
import com.example.pages.ShoppingCartPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("external")
public class SauceLabDemoTests extends BaseSeleniumTest {

  private static final String URL = "https://www.saucedemo.com/";
  private static final String USER = "standard_user";
  private static final String PASS = "secret_sauce";

  private InventoryPage loginAndWaitForInventory() {
    InventoryPage inventory = new LoginPage(driver)
        .open(URL)
        .loginAs(USER, PASS);

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(d -> inventory.hasItems());

    return inventory;
  }

  private void assertCartHasExactlyOneItem(InventoryPage inventory, String expectedName) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    wait.until(d -> inventory.cartCountOrNull() != null);

    assertEquals("1", inventory.cartCountOrNull(), "Cart badge should show 1 item");

    ShoppingCartPage cart = inventory.goToCart();
    wait.until(d -> cart.itemCount() >= 1);

    assertEquals(1, cart.itemCount(), "Expected exactly one item in cart");
    assertTrue(cart.containsItemNamed(expectedName), "Cart should contain: " + expectedName);
  }

  @Test
  void highestPricedItem_selectedAndAddedToCart() {
    InventoryPage inventory = loginAndWaitForInventory();

    String itemName = inventory.addHighestPricedItemToCart();
    log.info("Highest priced item selected: {}", itemName);

    assertCartHasExactlyOneItem(inventory, itemName);
  }

  @Test
  void lowestPricedItem_selectedAndAddedToCart() {
    InventoryPage inventory = loginAndWaitForInventory();

    String itemName = inventory.addLowestPricedItemToCart();
    log.info("Lowest priced item selected: {}", itemName);

    assertCartHasExactlyOneItem(inventory, itemName);
  }
}
