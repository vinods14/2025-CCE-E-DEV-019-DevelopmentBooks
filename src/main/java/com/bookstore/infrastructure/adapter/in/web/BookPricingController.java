package com.bookstore.infrastructure.adapter.in.web;

import com.bookstore.application.domain.pojo.BasketItem;
import com.bookstore.application.domain.pojo.BasketRequest;
import com.bookstore.application.domain.pojo.BasketResponse;
import com.bookstore.application.domain.pojo.BookDto;
import com.bookstore.application.port.in.BookUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookPricingController {

    private final BookUseCase bookUseCase;

    @GetMapping(value = "/books",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookDto> getBooks() {
        return bookUseCase.findAllBooks();
    }

    @PostMapping(value = "/books/calculate-price",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public BasketResponse calculatePrice(@RequestBody BasketRequest basketRequest) {
        Map<Long, Integer> basketMap = basketRequest.getItems().stream()
                .collect(Collectors.toMap(BasketItem::getBookId, BasketItem::getQuantity));
        return bookUseCase.calculateTotal(basketMap);
    }
}
