package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
  private final WebDriver driver;

  private final By username = By.id("user-name");
  private final By password = By.id("password");
  private final By loginButton = By.id("login-button");

  public LoginPage(WebDriver driver) {
    this.driver = driver;
  }

  public LoginPage open(String baseUrl) {
    driver.get(baseUrl);
    return this;
  }

  public InventoryPage loginAs(String user, String pass) {
    driver.findElement(username).clear();
    driver.findElement(username).sendKeys(user);

    driver.findElement(password).clear();
    driver.findElement(password).sendKeys(pass);

    driver.findElement(loginButton).click();
    return new InventoryPage(driver);
  }
}