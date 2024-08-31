package com.library;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class BookServiceTest {

    private BookService bookService;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private PreparedStatement mockCheckPreparedStatement;

    @Mock
    private PreparedStatement mockInsertPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private MockedStatic<DbConnectivity> mockedStatic;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        mockedStatic = mockStatic(DbConnectivity.class);
        mockedStatic.when(DbConnectivity::getConnection).thenReturn(mockConnection);
        when(mockConnection.prepareStatement("SELECT * FROM book WHERE isbn = ?")).thenReturn(mockCheckPreparedStatement);
        when(mockCheckPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockConnection.prepareStatement("INSERT INTO book(isbn,title,author,publication_year) VALUES (?,?,?,?)"))
                .thenReturn(mockInsertPreparedStatement);
//        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
//        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        bookService = new BookService();
    }

    @AfterEach
    void closeMockConn() {
        mockedStatic.close();
    }

    @Test
    public void testAddBookWithSameISBN() throws SQLException
    {
        Book book = new Book(1,"Java Programming","Cay S. Horstmann",2001);

        when(mockResultSet.next()).thenReturn(true);
        //when(mockResultSet.getInt(1)).thenReturn(1);

        bookService.addBook(book);

        verify(mockConnection,times(1)).prepareStatement("SELECT * FROM book WHERE isbn = ?");
        verify(mockCheckPreparedStatement,times(1)).setInt(1,book.getIsbn());
        verify(mockCheckPreparedStatement,times(1)).executeQuery();
        //verify(mockCheckPreparedStatement,times(1)).executeUpdate();
        //verify(mockInsertPreparedStatement, never()).executeUpdate();

        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testAddBook() throws SQLException
    {
        Book book = new Book(1,"Java Programming","Cay S. Horstmann",2001);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(0);
        bookService.addBook(book);

        verify(mockConnection,times(1)).prepareStatement("SELECT * FROM book WHERE isbn = ?");
        verify(mockCheckPreparedStatement,times(1)).setInt(1,book.getIsbn());
        verify(mockCheckPreparedStatement,times(1)).executeQuery();

        verify(mockConnection,times(1)).prepareStatement("INSERT INTO book(isbn,title,author,publication_year) VALUES (?,?,?,?)");
        verify(mockInsertPreparedStatement,times(1)).setInt(1,book.getIsbn());
        verify(mockInsertPreparedStatement,times(1)).setString(2,book.getTitle());
        verify(mockInsertPreparedStatement,times(1)).setString(3,book.getAuthor());
        verify(mockInsertPreparedStatement,times(1)).setInt(4,book.getPublicationYear());
        verify(mockInsertPreparedStatement,times(1)).executeUpdate();
        verify(mockInsertPreparedStatement,times(1)).close();
        verify(mockConnection,times(1)).close();
    }
}

