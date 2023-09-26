# Automated Testing Framework with Java, Playwright, BDD, and Page Object Model

## Table of Contents
- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setting Up the Environment](#setting-up-the-environment)
- [Writing Test Scenarios](#writing-test-scenarios)
- [Page Object Model](#page-object-model)
- [Best Practices](#best-practices)
- [Code Reusability](#code-reusability)
- [Running Tests](#running-tests)
- [Conclusion](#conclusion)

## Introduction

This README provides a comprehensive guide to building an automated testing framework using Java, Playwright, Behavior-Driven Development (BDD), and Page Object Model (POM). The framework aims to maintain code quality, promote reusability, and improve test automation efficiency.

## Prerequisites

Before getting started, ensure you have the following prerequisites installed:

- Java Development Kit (JDK)
- Maven
- Playwright (Java)
- Your preferred IDE (e.g., IntelliJ IDEA, Eclipse)

## Project Structure

```
- src
  - main
    - java
      - com
        - yourcompany
          - yourapp
            - pages
              - BasePage.java
              - LoginPage.java
            - tests
              - BaseTest.java
              - LoginTest.java
  - test
    - resources
      - features
        - login.feature
    - java
      - com
        - yourcompany
          - yourapp
            - stepdefinitions
              - LoginStepDefinitions.java
- pom.xml
- README.md
```

## Setting Up the Environment

1. Create a Maven project in your IDE and add the Playwright dependency to your `pom.xml`:

   ```xml
    <dependencies>
        <!-- https://mvnrepository.com/artifact/com.deque.html.axe-core/playwright -->
        <dependency>
            <groupId>com.deque.html.axe-core</groupId>
            <artifactId>playwright</artifactId>
            <version>${playwright.axe.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-scala -->
        <dependency>
            <groupId>com.fasterxml.jackson.module</groupId>
            <artifactId>jackson-module-scala_3</artifactId>
            <version>2.15.2</version>
        </dependency>


        <!-- https://mvnrepository.com/artifact/com.microsoft.playwright/playwright -->
        <dependency>
            <groupId>com.microsoft.playwright</groupId>
            <artifactId>playwright</artifactId>
            <version>${playwright.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.apache.commons/commons-lang3 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>${common.lang3.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.testng/testng -->
        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <version>${testng.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/io.cucumber/cucumber-java8 -->
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java8</artifactId>
            <version>${cucumber.version}</version>
        </dependency>


        <!-- https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple -->
        <dependency>
            <groupId>com.googlecode.json-simple</groupId>
            <artifactId>json-simple</artifactId>
            <version>${json.simple.version}</version>
        </dependency>


        <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/ch.qos.logback/logback-classic -->
<!--        <dependency>-->
<!--            <groupId>ch.qos.logback</groupId>-->
<!--            <artifactId>logback-classic</artifactId>-->
<!--            <version>${log.back.version}</version>-->
<!--        </dependency>-->

        <!-- https://mvnrepository.com/artifact/io.cucumber/cucumber-java -->
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>${cucumber.version}</version>
<!--            <scope>test</scope>-->
        </dependency>

        <!-- https://mvnrepository.com/artifact/io.cucumber/cucumber-testng -->
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-testng</artifactId>
            <version>${cucumber.version}</version>
<!--            <scope>test</scope>-->
        </dependency>

        <!-- https://mvnrepository.com/artifact/io.cucumber/cucumber-picocontainer -->
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-picocontainer</artifactId>
            <version>${cucumber.version}</version>
<!--            <scope>test</scope>-->
        </dependency>



        <!-- https://mvnrepository.com/artifact/junit/junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>${junit.version}</version>
<!--            <scope>test</scope>-->
        </dependency>

        <!-- https://mvnrepository.com/artifact/io.cucumber/cucumber-junit -->
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-junit</artifactId>
            <version>${cucumber.version}</version>
<!--            <scope>test</scope>-->
        </dependency>

        <!-- https://mvnrepository.com/artifact/io.cucumber/cucumber-core -->
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-core</artifactId>
            <version>${cucumber.version}</version>
        </dependency>


        <dependency>
            <groupId>net.masterthought</groupId>
            <artifactId>cucumber-reporting</artifactId>
            <version>${cucumber.reporting.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/io.qameta.allure/allure-testng -->
        <dependency>
            <groupId>io.qameta.allure</groupId>
            <artifactId>allure-testng</artifactId>
            <version>${allure.reporting.version}</version>
        </dependency>
        <dependency>
            <groupId>com.deque.html.axe-core</groupId>
            <artifactId>dequeutilites</artifactId>
            <version>${playwright.axe.version}</version>
        </dependency>

    </dependencies>

   ```

2. Install the necessary WebDriver binaries for Playwright. You can use the Playwright CLI:

   ```
   npx playwright install
   ```

3. Set up your IDE to work with Maven and ensure you have a compatible Java SDK installed.

## Writing Test Scenarios

1. Create feature files in the `src/test/resources/features` directory using Gherkin syntax. For example, `login.feature`:

   ```gherkin
   Feature: Login Functionality
   
     Scenario: Successful login
       Given I navigate to the login page
       When I enter valid credentials
       And I click the login button
       Then I should be logged in successfully
   ```

2. Create step definitions in the `src/test/java/com/yourcompany/yourapp/stepdefinitions` directory. Map each step to Java methods.

## Page Object Model

Implement the Page Object Model (POM) in the `src/main/java/com/yourcompany/yourapp/pages` directory:

1. Create a `BasePage` class to define common methods and elements.

2. Create individual page classes (e.g., `LoginPage`) that extend `BasePage`. Each page class should encapsulate page-specific methods and elements.

## Best Practices

Follow these best practices to maintain code quality:

- Use meaningful method and variable names.
- Keep your code DRY (Don't Repeat Yourself).
- Use comments and Javadoc to document your code.
- Implement appropriate waits for element visibility and interactions.
- Use constants for selectors and configuration.

## Code Reusability

Promote code reusability through the following techniques:

- Inherit common functionality from a `BasePage` class.
- Create utility classes for repetitive tasks (e.g., handling alerts).
- Implement data-driven testing using external data sources (e.g., Excel or CSV files).
- Utilize parameterization for test scenarios.

## Running Tests

You can run tests using Maven from the command line or within your IDE. For example, to run tests using Maven:

```shell
mvn clean test
```

## Conclusion

This README provides a structured approach to building an automated testing framework using Java, Playwright, BDD, and the Page Object Model. By following best practices and promoting code reusability, you can create a robust and maintainable test suite for your application. Happy testing!



Author: Chinemelum Felicitas Unachukwu