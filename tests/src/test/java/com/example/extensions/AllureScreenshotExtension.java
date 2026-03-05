package com.example.extensions;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class AllureScreenshotExtension implements TestWatcher {

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    Object testInstance = context.getRequiredTestInstance();
    if (!(testInstance instanceof HasDriver)) return;

    WebDriver driver = ((HasDriver) testInstance).getDriver();
    if (driver == null) return;

    // 1) URL
    try {
      Allure.addAttachment("URL", "text/plain", driver.getCurrentUrl());
    } catch (Exception ignored) {}

    // 2) Screenshot
    try {
      if (driver instanceof TakesScreenshot) {
        byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(png));
      }
    } catch (Exception ignored) {}

    // 3) Page source
    try {
      String html = driver.getPageSource();
      Allure.addAttachment(
          "Page Source",
          "text/html",
          new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)),
          ".html"
      );
    } catch (Exception ignored) {}

    // 4) Browser console logs (may not be available in all remote/grid setups)
    try {
      LogEntries entries = driver.manage().logs().get(LogType.BROWSER);
      StringBuilder sb = new StringBuilder();
      entries.forEach(e -> sb.append(e.getLevel()).append(" ").append(e.getMessage()).append("\n"));
      if (sb.length() > 0) {
        Allure.addAttachment("Browser Console Logs", "text/plain", sb.toString());
      }
    } catch (Exception ignored) {
      // Some drivers/grids don't support logs; ignore.
    }
  }

  /** Small interface so the extension can access driver safely */
  public interface HasDriver {
    WebDriver getDriver();
  }
}
