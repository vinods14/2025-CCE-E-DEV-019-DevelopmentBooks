package com.bookstore.infrastructure.adapter.in.web;

import com.bookstore.application.domain.pojo.BasketResponse;
import com.bookstore.application.domain.pojo.BookDto;
import com.bookstore.application.domain.pojo.GroupSummary;
import com.bookstore.application.port.in.BookUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookPricingController.class)
@Import(BookPricingControllerTest.MockConfig.class)
class BookPricingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookUseCase bookUseCase;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public BookUseCase bookUseCase() {
            return Mockito.mock(BookUseCase.class);
        }
    }

    @Test
    @DisplayName("GET /api/books should return list of books")
    void testGetBooks() throws Exception {
        List<BookDto> mockBooks = List.of(
                new BookDto(1L, "Clean Code", "Robert Martin", 50.0),
                new BookDto(2L, "The Clean Coder", "Robert Martin", 50.0)
        );

        when(bookUseCase.findAllBooks()).thenReturn(mockBooks);

        mockMvc.perform(get("/api/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[1].title").value("The Clean Coder"));

        verify(bookUseCase, times(1)).findAllBooks();
    }

    @Test
    @DisplayName("POST /api/books/calculate-price should return total basket price")
    void testCalculatePrice() throws Exception {
        BasketResponse mockResponse = BasketResponse.builder()
                .total(320.0)
                .groups(List.of(
                        GroupSummary.builder()
                                .differentBooks(4)
                                .discount("20%")
                                .discountedPrice(160.0)
                                .build(),
                        GroupSummary.builder()
                                .differentBooks(4)
                                .discount("20%")
                                .discountedPrice(160.0)
                                .build()
                ))
                .build();

        when(bookUseCase.calculateTotal(anyMap())).thenReturn(mockResponse);

        String jsonRequest = """
                {
                    "items": [
                        { "bookId": 1, "quantity": 2 },
                        { "bookId": 2, "quantity": 2 },
                        { "bookId": 3, "quantity": 2 },
                        { "bookId": 4, "quantity": 1 },
                        { "bookId": 5, "quantity": 1 }
                    ]
                }
                """;

        mockMvc.perform(post("/api/books/calculate-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(320.0))
                .andExpect(jsonPath("$.groups[0].differentBooks").value(4))
                .andExpect(jsonPath("$.groups[0].discount").value("20%"))
                .andExpect(jsonPath("$.groups[1].differentBooks").value(4))
                .andExpect(jsonPath("$.groups[1].discountedPrice").value(160.0));

    }

    @Test
    @DisplayName("POST /api/books/calculate-price with empty items should return 0 total")
    void testCalculatePriceEmpty() throws Exception {
        BasketResponse emptyResponse = BasketResponse.builder()
                .total(0.0)
                .groups(List.of())
                .build();

        when(bookUseCase.calculateTotal(anyMap())).thenReturn(emptyResponse);

        String jsonRequest = """
                { "items": [] }
                """;

        mockMvc.perform(post("/api/books/calculate-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0.0))
                .andExpect(jsonPath("$.groups").isEmpty());

        verify(bookUseCase, times(1)).calculateTotal(anyMap());
    }
}
