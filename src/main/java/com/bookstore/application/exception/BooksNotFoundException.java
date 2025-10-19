package com.bookstore.application.exception;

public class BooksNotFoundException extends RuntimeException {

    public BooksNotFoundException(String message) {
        super(message);
    }
}
