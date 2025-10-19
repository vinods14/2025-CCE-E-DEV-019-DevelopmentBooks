package com.bookstore.infrastructure.adapter.out.persistence.repository;

import com.bookstore.infrastructure.adapter.out.persistence.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
