package com.bookstore.application.port.out;

import com.bookstore.application.domain.pojo.BasketResponse;
import com.bookstore.application.domain.pojo.BookDto;

import java.util.List;
import java.util.Map;

public interface LoadBooksPort {

    List<BookDto> loadAllBooks();

    BasketResponse getBasketPrice(Map<Long, Integer> basket);
}
