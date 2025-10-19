package com.bookstore.application.port.in;

import com.bookstore.application.domain.pojo.BasketResponse;
import com.bookstore.application.domain.pojo.BookDto;

import java.util.List;
import java.util.Map;

public interface BookUseCase {

    List<BookDto> findAllBooks();

    BasketResponse calculateTotal(Map<Long, Integer> basket);
}
