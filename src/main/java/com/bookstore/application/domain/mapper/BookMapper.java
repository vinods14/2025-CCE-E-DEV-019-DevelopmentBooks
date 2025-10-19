package com.bookstore.application.domain.mapper;

import com.bookstore.application.domain.pojo.BookDto;
import com.bookstore.infrastructure.adapter.out.persistence.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);
    BookDto map(Book book);
}
