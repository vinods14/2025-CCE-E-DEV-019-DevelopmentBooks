package com.bookstore.infrastructure.adapter.out.persistence.repository;

import com.bookstore.application.domain.mapper.BookMapper;
import com.bookstore.application.domain.pojo.BasketResponse;
import com.bookstore.application.domain.pojo.BookDto;
import com.bookstore.application.domain.pojo.GroupSummary;
import com.bookstore.application.exception.BooksNotFoundException;
import com.bookstore.application.port.out.LoadBooksPort;
import com.bookstore.infrastructure.adapter.out.persistence.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookRepositoryAdapter implements LoadBooksPort {

    private static final double BOOK_PRICE = 50.0;
    private static final Map<Integer, Double> DISCOUNTS = Map.of(
            1, 0.0,
            2, 0.05,
            3, 0.10,
            4, 0.20,
            5, 0.25
    );
    private final BookRepository bookRepository;

    @Override
    public List<BookDto> loadAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new BooksNotFoundException("Books not found");
        }
        return books
                .stream()
                .map(this::mapToBookDto)
                .collect(Collectors.toList());
    }

    @Override
    public BasketResponse getBasketPrice(Map<Long, Integer> basket) {
        List<Integer> bookCounts = new ArrayList<>(basket.values());
        double totalPrice = 0.0;
        List<GroupSummary> groups = new ArrayList<>();

        while (bookCounts.stream().anyMatch(count -> count > 0)) {
            int distinctBooks = (int) bookCounts.stream().filter(c -> c > 0).count();
            if (distinctBooks > 5) distinctBooks = 5;

            double discount = DISCOUNTS.get(distinctBooks);
            double groupPrice = distinctBooks * BOOK_PRICE * (1 - discount);
            totalPrice += groupPrice;

            groups.add(GroupSummary.builder()
                    .differentBooks(distinctBooks)
                    .discount((int) (discount * 100) + "%")
                    .discountedPrice(groupPrice)
                    .build());

            for (int i = 0; i < bookCounts.size() && distinctBooks > 0; i++) {
                if (bookCounts.get(i) > 0) {
                    bookCounts.set(i, bookCounts.get(i) - 1);
                    distinctBooks--;
                }
            }
        }

        // --- FIX: handle 5+3 group edge case ---
        long groupOf5 = groups.stream().filter(g -> g.getDifferentBooks() == 5).count();
        long groupOf3 = groups.stream().filter(g -> g.getDifferentBooks() == 3).count();

        if (groupOf5 > 0 && groupOf3 > 0) {
            // Replace one 5-group + one 3-group with two 4-groups
            totalPrice -= (5 * BOOK_PRICE * (1 - 0.25)) + (3 * BOOK_PRICE * (1 - 0.10));
            totalPrice += 2 * (4 * BOOK_PRICE * (1 - 0.20));

            // Replace entries in the group list for accuracy
            groups.removeIf(g -> g.getDifferentBooks() == 5 || g.getDifferentBooks() == 3);
            groups.add(GroupSummary.builder().differentBooks(4).discount("20%").discountedPrice(160.0).build());
            groups.add(GroupSummary.builder().differentBooks(4).discount("20%").discountedPrice(160.0).build());
        }
        return BasketResponse.builder().total(totalPrice).groups(groups).build();
    }

    private BookDto mapToBookDto(Book book) {
        return BookMapper.INSTANCE.map(book);
    }
}
