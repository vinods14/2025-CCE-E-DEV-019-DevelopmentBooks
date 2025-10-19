package com.bookstore.infrastructure.adapter.out.persistence.repository;

import com.bookstore.application.domain.pojo.BasketResponse;
import com.bookstore.application.domain.pojo.BookDto;
import com.bookstore.application.domain.pojo.GroupSummary;
import com.bookstore.application.exception.BooksNotFoundException;
import com.bookstore.infrastructure.adapter.out.persistence.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookRepositoryAdapterTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookRepositoryAdapter adapter;

    private List<Book> mockBooks;

    @BeforeEach
    void setup() {
        mockBooks = List.of(
                new Book(1L, "Clean Code", "Robert C. Martin", 50.0),
                new Book(2L, "The Clean Coder", "Robert C. Martin", 50.0),
                new Book(3L, "Clean Architecture", "Robert C. Martin", 50.0),
                new Book(4L, "TDD by Example", "Kent Beck", 50.0),
                new Book(5L, "Working Effectively with Legacy Code", "Michael Feathers", 50.0)
        );
    }

    @Test
    @DisplayName("loadAllBooks() should return list of BookDto when books exist")
    void testLoadAllBooks_success() {
        when(bookRepository.findAll()).thenReturn(mockBooks);

        List<BookDto> result = adapter.loadAllBooks();

        assertThat(result).hasSize(5);
        assertThat(result.get(0).getTitle()).isEqualTo("Clean Code");
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("loadAllBooks() should throw BooksNotFoundException when no books in repository")
    void testLoadAllBooks_empty() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> adapter.loadAllBooks())
                .isInstanceOf(BooksNotFoundException.class)
                .hasMessageContaining("Books not found");

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getBasketPrice() should calculate correct price for 2x3 + 1x2 case = 320.0")
    void testGetBasketPrice_expected320() {
        Map<Long, Integer> basket = Map.of(
                1L, 2,
                2L, 2,
                3L, 2,
                4L, 1,
                5L, 1
        );

        BasketResponse response = adapter.getBasketPrice(basket);

        assertThat(response).isNotNull();
        assertThat(response.getTotal()).isEqualTo(320.0);
        assertThat(response.getGroups()).hasSize(2);

        List<GroupSummary> groups = response.getGroups();
        assertThat(groups).allSatisfy(g -> {
            assertThat(g.getDifferentBooks()).isEqualTo(4);
            assertThat(g.getDiscount()).isEqualTo("20%");
            assertThat(g.getDiscountedPrice()).isEqualTo(160.0);
        });
    }

    @Test
    @DisplayName("getBasketPrice() should apply 25% discount for 5 different books")
    void testGetBasketPrice_allDifferent() {
        Map<Long, Integer> basket = Map.of(
                1L, 1,
                2L, 1,
                3L, 1,
                4L, 1,
                5L, 1
        );

        BasketResponse response = adapter.getBasketPrice(basket);

        assertThat(response.getTotal()).isEqualTo(187.5);
        assertThat(response.getGroups()).hasSize(1);
        assertThat(response.getGroups().get(0).getDiscount()).isEqualTo("25%");
    }

    @Test
    @DisplayName("getBasketPrice() should return 0 for empty basket")
    void testGetBasketPrice_empty() {
        BasketResponse response = adapter.getBasketPrice(Collections.emptyMap());
        assertThat(response.getTotal()).isEqualTo(0.0);
        assertThat(response.getGroups()).isEmpty();
    }

    @Test
    @DisplayName("getBasketPrice() should handle duplicate-only basket with no discounts")
    void testGetBasketPrice_duplicatesOnly() {
        Map<Long, Integer> basket = Map.of(1L, 3);

        BasketResponse response = adapter.getBasketPrice(basket);

        assertThat(response.getTotal()).isEqualTo(150.0);
        assertThat(response.getGroups()).hasSize(3);
        assertThat(response.getGroups()).allSatisfy(g ->
                assertThat(g.getDiscount()).isEqualTo("0%")
        );
    }

    @Test
    @DisplayName("getBasketPrice() should correctly handle 5+3 optimization case (convert to 4+4 groups)")
    void testGetBasketPrice_5plus3Optimization() {
        // Simulate a basket that would normally yield 5 + 3 groups
        Map<Long, Integer> basket = Map.of(
                1L, 2,
                2L, 2,
                3L, 2,
                4L, 1,
                5L, 1
        );

        BasketResponse response = adapter.getBasketPrice(basket);

        assertThat(response.getGroups()).hasSize(2);
        assertThat(response.getTotal()).isEqualTo(320.0);
        assertThat(response.getGroups().get(0).getDifferentBooks()).isEqualTo(4);
        assertThat(response.getGroups().get(1).getDifferentBooks()).isEqualTo(4);
    }
}
