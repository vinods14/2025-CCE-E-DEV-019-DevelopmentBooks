package com.bookstore.application.domain.pojo;

import java.util.List;

//@Getter
//@Setter
public class BasketRequest {
    public List<BasketItem> getItems() {
        return items;
    }

    public void setItems(List<BasketItem> items) {
        this.items = items;
    }

    private List<BasketItem> items;
}
