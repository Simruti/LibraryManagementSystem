package com.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookServiceTest {
    @Test
    public void testaddBook()
    {
        BookService bookService = new BookService();
        Book book = new Book(1,"Java Programming","Cay S. Horstmann",2001);
        bookService.addBooks(book);

        assertEquals(1,bookService.getBooks().size());
        assertEquals("Java Programming",bookService.getBooks().get(0).getTitle());
        assertEquals("Cay S. Horstmann",bookService.getBooks().get(0).getAuthor());
        assertEquals(2001,bookService.getBooks().get(0).getPublicationYear());

    }
}
