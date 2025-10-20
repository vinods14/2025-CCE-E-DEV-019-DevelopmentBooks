# Bookstore Discount API

A simple **Spring Boot** project demonstrating **Test Driven Development
(TDD)** for a discount-based pricing system.

------------------------------------------------------------------------

## Features

-   REST API built with Spring Boot\
-   Pricing logic for book bundles with tiered discounts\
-   In-memory H2 database\
-   Swagger UI documentation\
-   JUnit 5 tests (TDD approach)\
-   Request--Response JSON pairs included for testing

------------------------------------------------------------------------

##  Discount Rules

Different Books   Discount   Example Total (50 EUR each)
  ----------------- ---------- -----------------------------
1                 0%         50.00 EUR
2                 5%         95.00 EUR
3                 10%        135.00 EUR
4                 20%        160.00 EUR
5                 25%        187.50 EUR

------------------------------------------------------------------------

##️ Setup & Run

### Prerequisites

-   Java 17+
-   Maven 3.8+

### Run the Application

``` bash
mvn spring-boot:run
```

### Run Tests

``` bash
mvn test
```

------------------------------------------------------------------------

##  Request--Response JSON Pairs

For easy API testing, **sample request and response JSON files** are
provided in the\
`rr-pair/` directory located at the root of the **book-store**
application.

These files can be used with tools like **Postman**, **Insomnia**, or
any automated test runner to validate API behavior.

------------------------------------------------------------------------

##  API Documentation

Once the app is running, you can access the **Swagger UI** at:\
👉 <http://localhost:8080/swagger-ui.html>

This provides a visual interface to explore and test API endpoints.

------------------------------------------------------------------------

##  Example Usage

**Request:**\
`POST /api/discount/calculate`

**Body (request.json):**

``` json
{
  "books": ["Book A", "Book B", "Book C"]
}
```

**Response (response.json):**

``` json
{
  "totalPrice": 135.0,
  "discountApplied": "10%"
}
```


##  Test-Driven Development (TDD)

This project follows a **TDD workflow**: 1. Write a failing test (Red)
2. Implement just enough code to make it pass (Green) 3. Refactor while
   keeping tests green (Refactor)

All pricing and discount logic is verified through **JUnit 5 unit
tests**.


## Database

The project uses an **in-memory H2 database** for simplicity and quick
testing.

You can access the H2 console at:\
👉 <http://localhost:8080/h2-console>

**Default credentials:** - JDBC URL: `jdbc:h2:mem:bookstore` - Username:
`sa` - Password: *(empty)*


## 🏗️ Project Structure

    book-store/
    ├── src/
    │   ├── main/
    │   │   ├── java/com/bookstore/
    │   │   │   ├── application/
    │   │   │   │   ├── config/            # Spring configuration classes (e.g., Swagger, Beans)
    │   │   │   │   ├── domain/            # Entities, DTOs, and domain models
    │   │   │   │   ├── exception/         # Custom exception classes
    │   │   │   │   ├── port/              # Hexagonal architecture ports (inbound/outbound)
    │   │   │   │   └── service/           # Core business logic and services
    │   │   │   │
    │   │   │   └── infrastructure/
    │   │   │       └── adapter/
    │   │   │           ├── in/
    │   │   │           │   └── web/       # REST controllers and request handling
    │   │   │           └── out/
    │   │   │               └── persistence/
    │   │   │                   ├── entity/     # JPA entities mapped to DB tables
    │   │   │                   └── repository/ # Spring Data repositories
    │   │   │
    │   │   └── resources/
    │   │       ├── application.yml       # Spring Boot configuration file
    │   │       ├── data.sql              # Initial data for H2 in-memory database
    │   │       └── schema.sql            # (Optional) schema creation script
    │   └── test/
    │       └── java/com/bookstore/       # Unit and integration tests (JUnit 5)
    │
    ├── rr-pair/                          # Request–Response JSONs for API testing
    ├── pom.xml                           # Maven project file
    └── README.md
