package com.bookstore.application.service;

import com.bookstore.application.domain.pojo.BasketResponse;
import com.bookstore.application.domain.pojo.BookDto;
import com.bookstore.application.domain.pojo.GroupSummary;
import com.bookstore.application.exception.BooksNotFoundException;
import com.bookstore.application.port.out.LoadBooksPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private LoadBooksPort loadBooksPort;

    @InjectMocks
    private BookService bookService;

    private List<BookDto> mockBooks;
    private BasketResponse mockBasketResponse;

    @BeforeEach
    void setup() {
        mockBooks = List.of(
                new BookDto(1L, "Clean Code", "Robert Martin", 50.0),
                new BookDto(2L, "The Clean Coder", "Robert Martin", 50.0)
        );

        mockBasketResponse = BasketResponse.builder()
                .total(100.0)
                .groups(List.of(
                        GroupSummary.builder()
                                .differentBooks(2)
                                .discount("5%")
                                .discountedPrice(95.0)
                                .build()
                ))
                .build();
    }

    @Test
    @DisplayName("findAllBooks() should return list of BookDto when books are available")
    void testFindAllBooks_success() {
        when(loadBooksPort.loadAllBooks()).thenReturn(mockBooks);

        List<BookDto> result = bookService.findAllBooks();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Clean Code");
        verify(loadBooksPort, times(1)).loadAllBooks();
    }


    @Test
    @DisplayName("findAllBooks() should throw BooksNotFoundException when no books found")
    void testFindAllBooks_throwsException() {
        when(loadBooksPort.loadAllBooks()).thenThrow(new BooksNotFoundException("Books not found"));

        assertThatThrownBy(() -> bookService.findAllBooks())
                .isInstanceOf(BooksNotFoundException.class)
                .hasMessageContaining("Books not found");

        verify(loadBooksPort, times(1)).loadAllBooks();
    }

    @Test
    @DisplayName("calculateTotal() should return BasketResponse when basket provided")
    void testCalculateTotal_success() {
        Map<Long, Integer> basket = Map.of(1L, 1, 2L, 1);
        when(loadBooksPort.getBasketPrice(basket)).thenReturn(mockBasketResponse);

        BasketResponse result = bookService.calculateTotal(basket);

        assertThat(result.getTotal()).isEqualTo(100.0);
        assertThat(result.getGroups()).hasSize(1);
        assertThat(result.getGroups().get(0).getDiscount()).isEqualTo("5%");
        verify(loadBooksPort, times(1)).getBasketPrice(basket);
    }

    @Test
    @DisplayName("calculateTotal() should return non-null BasketResponse even for empty basket")
    void testCalculateTotal_emptyBasket() {
        Map<Long, Integer> basket = Map.of();
        BasketResponse emptyResponse = BasketResponse.builder().total(0.0).groups(List.of()).build();
        when(loadBooksPort.getBasketPrice(basket)).thenReturn(emptyResponse);

        BasketResponse result = bookService.calculateTotal(basket);

        assertThat(result.getTotal()).isEqualTo(0.0);
        assertThat(result.getGroups()).isEmpty();
        verify(loadBooksPort, times(1)).getBasketPrice(basket);
    }
}
