-- Drop table if it already exists
DROP TABLE IF EXISTS books;

-- Create table again
CREATE TABLE books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    price DOUBLE NOT NULL
);