# Bookstore Discount API

A simple Spring Boot project demonstrating **Test Driven Development (TDD)** for a discount-based pricing system.

---

## Features
- REST API built with Spring Boot
- Pricing logic for book bundles with tiered discounts
- In-memory H2 database
- Swagger UI documentation
- JUnit 5 tests (TDD approach)

---

## Discount Rules
| Different Books | Discount | Example Total (50 EUR each) |
|------------------|----------|-----------------------------|
| 1                | 0%       | 50.00 EUR                  |
| 2                | 5%       | 95.00 EUR                  |
| 3                | 10%      | 135.00 EUR                 |
| 4                | 20%      | 160.00 EUR                 |
| 5                | 25%      | 187.50 EUR                 |

---

## Setup & Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Run
```bash
mvn spring-boot:run
