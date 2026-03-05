package com.example;

import com.example.extensions.AllureScreenshotExtension;
import com.example.extensions.LoggingExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith({LoggingExtension.class, AllureScreenshotExtension.class})
public abstract class BaseSeleniumTest implements AllureScreenshotExtension.HasDriver {

  protected final Logger log = LoggerFactory.getLogger(getClass());
  protected WebDriver driver;
  protected String baseUrl;

  @Override
  public WebDriver getDriver() {
    return driver;
  }

  @BeforeEach
  void setUp() throws Exception {
    baseUrl = System.getenv().getOrDefault("BASE_URL", "https://www.saucedemo.com/");
    String seleniumUrl = System.getenv("SELENIUM_URL"); // optional

    ChromeOptions options = new ChromeOptions();

    // Allow turning headless on/off via env var (default headless=true)
    boolean headless = !"false".equalsIgnoreCase(System.getenv().getOrDefault("HEADLESS", "true"));
    if (headless) {
      options.addArguments("--headless=new");
    }

    // Good defaults for CI/Docker; harmless locally
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");

    if (seleniumUrl != null && !seleniumUrl.isBlank()) {
      // Remote run (Docker/CI)
      driver = new RemoteWebDriver(new URL(seleniumUrl), options);
    } else {
      // Local run (ChromeDriver managed by Selenium Manager)
      driver = new ChromeDriver(options);
    }

    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
  }

  @AfterEach
  void tearDown() {
    if (driver != null) driver.quit();
  }
}
