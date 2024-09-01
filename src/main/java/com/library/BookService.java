package com.library;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class BookService {

    private Scanner scanner = new Scanner(System.in);

    // Constructor for real usage
    public BookService() {
        this.scanner = new Scanner(System.in);
    }

    // Constructor for testing
    public BookService(Scanner scanner) {
        this.scanner = scanner;
    }
    public void addBook(Book book)
    {
        try
        {
            Connection con = DbConnectivity.getConnection();
            String checkQry = "SELECT * FROM book WHERE isbn = ?";
            PreparedStatement checkStatement = con.prepareStatement(checkQry);
            ResultSet resultSet = null;
            if(book == null)
            {
                return;
            }
            else{
                checkStatement.setInt(1,book.getIsbn());
                resultSet = checkStatement.executeQuery();
            }
            if(resultSet.next() && resultSet.getInt(1)>0)
            {
                System.out.println("Book already exists in the database");
                return;
            }
            String add_book = "INSERT INTO book(isbn,title,author,publication_year,copies_owned) VALUES (?,?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(add_book);
            statement.setInt(1,book.getIsbn());
            statement.setString(2,book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setInt(4, book.getPublicationYear());
            statement.setInt(5,book.getCopies_owned());

            statement.executeUpdate();
            System.out.println("Book added to database Successfully");

            statement.close();
            con.close();


        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Book> getBooks() throws SQLException
    {
        List<Book> books = new ArrayList<>();
        try{
            Connection con = DbConnectivity.getConnection();
            String getQuery = "SELECT * FROM book";
            PreparedStatement selectStatement = con.prepareStatement(getQuery);
            ResultSet resultSetBooks = selectStatement.executeQuery();

            System.out.println("\nList of Books: \n");
            while(resultSetBooks.next())
            {
                int isbn = resultSetBooks.getInt("isbn");
                String title = resultSetBooks.getString("title");
                String author = resultSetBooks.getString("author");
                int publicationYear = resultSetBooks.getInt("publication_year");
                int copiesOwned = resultSetBooks.getInt("copies_owned");

                books.add(new Book(isbn, title, author, publicationYear, copiesOwned));

                System.out.println("ISBN: "+ isbn+ "\nTitle: "+ title +"\nAuthor: "+ author+ "\nPublication Year: "+ publicationYear +"\nCopies Owned: "+ copiesOwned);
                System.out.println("------------------------------------------------------------------------------------------------------------\n");
            }

            resultSetBooks.close();
            selectStatement.close();
            con.close();

        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return books;
    }

    public List<Book> issueBook() throws SQLException
    {
        List<Book> books = new ArrayList<>();

        try {
            Connection con = DbConnectivity.getConnection();
            String getQuery = "SELECT b.isbn, b.title, b.author, b.copies_owned - COUNT(CASE WHEN bi.return_status = 0 THEN bi.isbn ELSE NULL END) AS availableCopies FROM book b LEFT JOIN book_issue bi ON b.isbn = bi.isbn GROUP BY b.isbn, b.title, b.author, b.copies_owned HAVING availableCopies > 0";
            PreparedStatement selectStatement = con.prepareStatement(getQuery);
            ResultSet resultSetBooks = selectStatement.executeQuery();

            System.out.println("\nList of Available Books: \n");
            while (resultSetBooks.next()) {
                int isbn = resultSetBooks.getInt("isbn");
                String title = resultSetBooks.getString("title");
                String author = resultSetBooks.getString("author");
                int publicationYear = resultSetBooks.getInt("publication_year");
                int available_copies = resultSetBooks.getInt("copies_owned");

                books.add(new Book(isbn, title, author, publicationYear, available_copies));

                System.out.println("ISBN: " + isbn + "Title: " + title + "Author: " + author + "Publication Year: " + publicationYear + "Copies Owned: " + available_copies);
            }

            System.out.println("Enter ISBN of a book which you want to issue: ");
            int selectedIsbn = scanner.nextInt();

            boolean checkIsbn = books.stream().anyMatch(book -> book.getIsbn() == selectedIsbn);

            if (checkIsbn)
            {
                System.out.println("Enter your Member ID: ");
                int memberId = scanner.nextInt();

                String issueQuery = "INSERT INTO book_issue (isbn, member_id, issue_date, return_status) VALUES (?, ?, ?, 0)";
                PreparedStatement issueStatement = con.prepareStatement(issueQuery);

                if (issueStatement == null) {
                    throw new SQLException("PreparedStatement creation failed.");
                }

                BookIssue bookIssue = new BookIssue(selectedIsbn,memberId);

                issueStatement.setInt(1, selectedIsbn);
                issueStatement.setInt(2, memberId);
                issueStatement.setDate(3, Date.valueOf(LocalDate.now()));

                if(issueStatement.executeUpdate() > 0){
                    System.out.println("Book issued successfully!");
                }
            }
            else{
                System.out.println("Invalid ISBN!! Please enter valid ISBN number.");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return books;
    }

    public boolean returnBook(int isbn, int memberId) throws SQLException {
        try (Connection con = DbConnectivity.getConnection()) {
            String updateQuery = "UPDATE book_issue SET return_status = 1 WHERE isbn = ? AND member_id = ? AND return_status = 0";
            PreparedStatement updateStatement = con.prepareStatement(updateQuery);
            updateStatement.setInt(1, isbn);
            updateStatement.setInt(2, memberId);

            int rowsAffected = updateStatement.executeUpdate();
            updateStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("There is some Error: " + e.getMessage());
            return false;
        }
    }

}
