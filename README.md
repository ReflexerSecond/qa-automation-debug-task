# QA Automation Debugging Task

This repository contains a small UI automation project built with:

- Java 17
- Selenide
- TestNG
- Maven

## Requirements

To run the project locally, make sure you have:

- JDK 17+
- Maven
- Google Chrome

## Running the tests

From the project root, run:

```bash
mvn clean test -Dselenide.headless=true -Dselenide.browserSize=1920x1080 -Dselenide.timeout=6000
```