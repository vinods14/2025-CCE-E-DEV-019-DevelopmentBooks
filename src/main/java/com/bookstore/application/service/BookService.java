package com.bookstore.application.service;

import com.bookstore.application.domain.pojo.BasketResponse;
import com.bookstore.application.domain.pojo.BookDto;
import com.bookstore.application.port.in.BookUseCase;
import com.bookstore.application.port.out.LoadBooksPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookService implements BookUseCase {
    
    private final LoadBooksPort loadBooksPort;

    public List<BookDto> findAllBooks() {
       return loadBooksPort.loadAllBooks();
    }

    public BasketResponse calculateTotal(Map<Long, Integer> basket) {
        return loadBooksPort.getBasketPrice(basket);
    }
}
