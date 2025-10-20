package com.bookstore.infrastructure.adapter.out.persistence.repository;

import com.bookstore.infrastructure.adapter.out.persistence.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long>
{
}
