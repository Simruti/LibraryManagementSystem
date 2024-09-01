package com.library;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.sql.Types.NULL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private PreparedStatement mockSelectPreparedStatement;

    @Mock
    private PreparedStatement mockUpdatePreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private Scanner mockScanner;

    private MockedStatic<DbConnectivity> mockedStatic;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        mockedStatic = mockStatic(DbConnectivity.class);
        mockedStatic.when(DbConnectivity::getConnection).thenReturn(mockConnection);

        mockScanner = new Scanner("1\n1");

        // For Add Book
        when(mockConnection.prepareStatement("SELECT * FROM book WHERE isbn = ?")).thenReturn(mockCheckPreparedStatement);
        when(mockCheckPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockConnection.prepareStatement("INSERT INTO book(isbn,title,author,publication_year,copies_owned) VALUES (?,?,?,?,?)"))
                .thenReturn(mockInsertPreparedStatement);
//        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
//        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // For Get Books
        when(mockConnection.prepareStatement("SELECT * FROM book")).thenReturn(mockSelectPreparedStatement);
        when(mockSelectPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        bookService = new BookService();

        bookService = new BookService(mockScanner);
    }

    @AfterEach
    void closeMockConn() {
        mockedStatic.close();
    }

    @Test
    public void testAddBookWithSameISBN() throws SQLException {
        Book book = new Book(1, "Java Programming", "Cay S. Horstmann", 2001, 20);

        when(mockResultSet.next()).thenReturn(true);
        //when(mockResultSet.getInt(1)).thenReturn(1);

        bookService.addBook(book);

        verify(mockConnection, times(1)).prepareStatement("SELECT * FROM book WHERE isbn = ?");
        verify(mockCheckPreparedStatement, times(1)).setInt(1, book.getIsbn());
        verify(mockCheckPreparedStatement, times(1)).executeQuery();
        //verify(mockInsertPreparedStatement, never()).executeUpdate();

        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testAddBook() throws SQLException {
        Book book = new Book(1, "Java Programming", "Cay S. Horstmann", 2001, 20);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(0);
        bookService.addBook(book);

        verify(mockConnection, times(1)).prepareStatement("SELECT * FROM book WHERE isbn = ?");
        verify(mockCheckPreparedStatement, times(1)).setInt(1, book.getIsbn());
        verify(mockCheckPreparedStatement, times(1)).executeQuery();

        verify(mockConnection, times(1)).prepareStatement("INSERT INTO book(isbn,title,author,publication_year,copies_owned) VALUES (?,?,?,?,?)");
        verify(mockInsertPreparedStatement, times(1)).setInt(1, book.getIsbn());
        verify(mockInsertPreparedStatement, times(1)).setString(2, book.getTitle());
        verify(mockInsertPreparedStatement, times(1)).setString(3, book.getAuthor());
        verify(mockInsertPreparedStatement, times(1)).setInt(4, book.getPublicationYear());
        verify(mockInsertPreparedStatement, times(1)).setInt(5, book.getCopies_owned());
        verify(mockInsertPreparedStatement, times(1)).executeUpdate();
        verify(mockInsertPreparedStatement, times(1)).close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testGetBook() throws SQLException{

        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(new Book(1,"Java Programming","Herbert Schildt",2001,20));
        mockBooks.add(new Book(2,"Core Java An Integrated Approach (Black Book)","Dr. R. Nageswara Rao",2005,15));

        when(mockResultSet.next()).thenReturn(true,true,false);
        when(mockResultSet.getInt("isbn")).thenReturn(1,2);
        when(mockResultSet.getString("title")).thenReturn("Java Programming","Core Java An Integrated Approach (Black Book)");
        when(mockResultSet.getString("author")).thenReturn("Herbert Schildt","Dr. R. Nageswara Rao");
        when(mockResultSet.getInt("publication_year")).thenReturn(2001,2005);
        when(mockResultSet.getInt("copies_owned")).thenReturn(20,15);

        List<Book> books = bookService.getBooks();

        verify(mockConnection).prepareStatement("SELECT * FROM book");
        verify(mockSelectPreparedStatement).executeQuery();
        verify(mockResultSet,times(3)).next();
        verify(mockResultSet,times(1)).close();
        verify(mockSelectPreparedStatement,times(1)).close();
        verify(mockConnection,times(1)).close();


        assertEquals(2,books.size());
        assertEquals(mockBooks.get(0),books.get(0));
        assertEquals(mockBooks.get(1),books.get(1));
    }

    @Test
    public void testIssueBook() throws SQLException
    {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("isbn")).thenReturn(1);
        when(mockResultSet.getString("title")).thenReturn("Java Programming");
        when(mockResultSet.getString("author")).thenReturn("Herbert Schildt");
        when(mockResultSet.getInt("publication_year")).thenReturn(2001);
        when(mockResultSet.getInt("copies_owned")).thenReturn(2);

        //To show available books
        when(mockConnection.prepareStatement("SELECT b.isbn, b.title, b.author, b.copies_owned - COUNT(CASE WHEN bi.return_status = 0 THEN bi.isbn ELSE NULL END) AS availableCopies FROM book b LEFT JOIN book_issue bi ON b.isbn = bi.isbn GROUP BY b.isbn, b.title, b.author, b.copies_owned HAVING availableCopies > 0")).thenReturn(mockSelectPreparedStatement);
        when(mockSelectPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        //Issue Book
        when(mockConnection.prepareStatement("INSERT INTO book_issue (isbn, member_id, issue_date, return_status) VALUES (?, ?, ?, 0)")).thenReturn(mockInsertPreparedStatement);
        when(mockInsertPreparedStatement.executeUpdate()).thenReturn(1);

        List<Book> books = bookService.issueBook();
        int isbn = 1;
        int memberId = 1;

        verify(mockConnection,times(1)).prepareStatement("SELECT b.isbn, b.title, b.author, b.copies_owned - COUNT(CASE WHEN bi.return_status = 0 THEN bi.isbn ELSE NULL END) AS availableCopies FROM book b LEFT JOIN book_issue bi ON b.isbn = bi.isbn GROUP BY b.isbn, b.title, b.author, b.copies_owned HAVING availableCopies > 0");
        verify(mockSelectPreparedStatement,times(1)).executeQuery();
        verify(mockResultSet,times(2)).next();

        verify(mockResultSet).getInt("isbn");
        verify(mockResultSet).getString("title");
        verify(mockResultSet).getString("author");
        verify(mockResultSet).getInt("publication_year");
        verify(mockResultSet).getInt("copies_owned");



        verify(mockConnection,times(1)).prepareStatement("INSERT INTO book_issue (isbn, member_id, issue_date, return_status) VALUES (?, ?, ?, 0)");
        verify(mockInsertPreparedStatement, times(1)).setInt(1,isbn);
        verify(mockInsertPreparedStatement,times(1)).setInt(2,memberId);
        verify(mockInsertPreparedStatement,times(1)).setDate(3, Date.valueOf(LocalDate.now()));
        verify(mockInsertPreparedStatement, times(1)).executeUpdate();

//        verify(mockResultSet,times(2)).next();
//        verify(mockResultSet,times(1)).close();
//        verify(mockSelectPreparedStatement,times(1)).close();
//        verify(mockInsertPreparedStatement,times(1)).close();
//        verify(mockConnection,times(1)).close();
    }

    @Test
    public void testReturnBook() throws SQLException
    {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("isbn")).thenReturn(1);
        when(mockResultSet.getString("title")).thenReturn("Java Programming");
        when(mockResultSet.getString("author")).thenReturn("Herbert Schildt");
        when(mockResultSet.getInt("publication_year")).thenReturn(2001);
        when(mockResultSet.getInt("copies_owned")).thenReturn(2);

        when(mockConnection.prepareStatement("SELECT b.isbn, b.title, b.author, b.copies_owned - COUNT(CASE WHEN bi.return_status = 0 THEN bi.isbn ELSE NULL END) AS availableCopies FROM book b LEFT JOIN book_issue bi ON b.isbn = bi.isbn GROUP BY b.isbn HAVING availableCopies > 0")).thenReturn(mockSelectPreparedStatement);
        when(mockSelectPreparedStatement.executeQuery()).thenReturn(mockResultSet);


        when(mockConnection.prepareStatement("UPDATE book_issue SET return_status = 1 WHERE isbn = ? AND member_id = ? AND return_status = 0")).thenReturn(mockUpdatePreparedStatement);
        when(mockUpdatePreparedStatement.executeUpdate()).thenReturn(1);

        int isbn = 1;
        int memberId = 1;
        boolean isReturned = bookService.returnBook(isbn, memberId);

        verify(mockConnection, times(1)).prepareStatement("UPDATE book_issue SET return_status = 1 WHERE isbn = ? AND member_id = ? AND return_status = 0");
        verify(mockUpdatePreparedStatement, times(1)).setInt(1, isbn);
        verify(mockUpdatePreparedStatement, times(1)).setInt(2, memberId);
        verify(mockUpdatePreparedStatement, times(1)).executeUpdate();

        assertEquals(true, isReturned);

        //verify(mockResultSet, times(1)).close();
        //verify(mockSelectPreparedStatement, times(1)).close();
        //verify(mockUpdatePreparedStatement, times(1)).close();
        //verify(mockConnection, times(1)).close();


    }
}

