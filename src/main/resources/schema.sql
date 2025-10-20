DROP TABLE IF EXISTS discount;
CREATE TABLE discount (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    different_books INT NOT NULL,
    discount_rate DOUBLE
);

-- Drop table if it already exists
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS book;

-- Create table again
CREATE TABLE books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    price DOUBLE NOT NULL
);