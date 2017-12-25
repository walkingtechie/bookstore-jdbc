package com.walking.techie.service;

import com.walking.techie.model.Book;

public interface BookStore {
    public void persistObjectGraph(Book book);

    public Book retrieveObjectGraph(String isbn);
}
