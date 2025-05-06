# Periplus Web Automation Test Framework

## Overview

This project is an automated test framework for the Periplus e-commerce website. It uses Selenium WebDriver with Java, TestNG, and follows the Page Object Model design pattern to create maintainable and readable UI tests.

## Installation

1. Clone the repository:
   ```
   git clone https://github.com/fahmisf19/Periplus-Test-Java.git
   ```

2. Install dependencies:
   ```
   cd Periplus-Test-Java
   mvn clean install
   ```

3. Update the `test.properties` file with your test data:
   ```
   # src/main/resources/test.properties
   user.email=your_test_email@example.com
   user.password=your_test_password
   book.title=Your Test Book Title
   book.price=100.000
   book.id=123456
   book.title.link=test-book-title
   ```

## Running Tests

Run all tests:
```
mvn clean test
```

Run a specific test:
```
mvn clean test -Dtest=PeriplusTest
```