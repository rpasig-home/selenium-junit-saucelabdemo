package com.example.extensions;

import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, TestWatcher {

  private static final Logger log = LoggerFactory.getLogger(LoggingExtension.class);
  private static final ExtensionContext.Namespace NS = ExtensionContext.Namespace.create("logging");
  private static final String START_NANOS = "startNanos";

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    context.getStore(NS).put(START_NANOS, System.nanoTime());
    log.info("=== START {} ===", context.getDisplayName());
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    // Store is still valid here
    Long start = context.getStore(NS).remove(START_NANOS, Long.class);
    long ms = (start == null) ? -1 : (System.nanoTime() - start) / 1_000_000;
    // Save duration on the root store so TestWatcher can read safely later (or just log here)
    context.getRoot().getStore(NS).put(context.getUniqueId(), ms);
  }

  @Override
  public void testSuccessful(ExtensionContext context) {
    Long ms = context.getRoot().getStore(NS).remove(context.getUniqueId(), Long.class);
    log.info("=== PASS  {} ({} ms) ===", context.getDisplayName(), ms == null ? "?" : ms);
  }

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    Long ms = context.getRoot().getStore(NS).remove(context.getUniqueId(), Long.class);
    log.error("=== FAIL  {} ({} ms) ===", context.getDisplayName(), ms == null ? "?" : ms, cause);
  }

  @Override
  public void testAborted(ExtensionContext context, Throwable cause) {
    Long ms = context.getRoot().getStore(NS).remove(context.getUniqueId(), Long.class);
    log.warn("=== SKIP  {} ({} ms) ===", context.getDisplayName(), ms == null ? "?" : ms, cause);
  }
}
