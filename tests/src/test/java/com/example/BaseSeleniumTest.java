package com.example;

import com.example.extensions.AllureScreenshotExtension;
import com.example.extensions.LoggingExtension;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Duration;


@ExtendWith({LoggingExtension.class, AllureScreenshotExtension.class})
public abstract class BaseSeleniumTest implements AllureScreenshotExtension.HasDriver{
  protected final Logger log = LoggerFactory.getLogger(getClass());
  protected WebDriver driver;
  protected String baseUrl;
  
  @Override
  public WebDriver getDriver() {
    return driver;
  }
  
  @BeforeEach
  void setUp() throws Exception {
    baseUrl = System.getenv().getOrDefault("BASE_URL", "http://localhost:3000");
    String seleniumUrl = System.getenv().getOrDefault("SELENIUM_URL", "http://localhost:4444");

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless=new");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");

    driver = new RemoteWebDriver(new URL(seleniumUrl), options);
    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
  }

  @AfterEach
  void tearDown() {
    if (driver != null) driver.quit();
  }
}