# Saucelab Demo Test using Selenium + JUnit 5 + Docker Compose 

This repo demonstrates a CI-ready UI automation setup:
- Selenium Standalone Chrome
- JUnit 5 tests (RemoteWebDriver)
- Docker Compose orchestration + healthchecks
- GitHub Actions CI

# Dependencies
- Install Docker
- install java (java 17)
- If using VS Code, install Java Extension Pack
- Install Maven for project management
- install Allure

## Run tests with Docker
```bash
make test

## View allure report post test run
http://localhost:5050/allure-docker-service/latest-report