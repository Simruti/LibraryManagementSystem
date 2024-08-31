package com.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class BookServiceTest {

    private BookService bookService;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setUp()throws SQLException
    {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService();

        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testaddBook() throws SQLException
    {
        BookService bookService = new BookService();
        Book book = new Book(1,"Java Programming","Cay S. Horstmann",2001);
        bookService.addBook(book);

        verify(mockPreparedStatement,times(1)).setInt(1,book.getIsbn());
        verify(mockPreparedStatement,times(1)).setString(2,book.getTitle());
        verify(mockPreparedStatement,times(1)).setString(3,book.getAuthor());
        verify(mockPreparedStatement,times(1)).setInt(4,book.getPublicationYear());
        verify(mockPreparedStatement,times(1)).executeUpdate();
        verify(mockPreparedStatement,times(1)).close();
        verify(mockConnection,times(1));


    }
}
