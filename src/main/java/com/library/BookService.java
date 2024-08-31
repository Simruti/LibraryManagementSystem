package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookService {

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
            String add_book = "INSERT INTO book(isbn,title,author,publication_year) VALUES (?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(add_book);
            statement.setInt(1,book.getIsbn());
            statement.setString(2,book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setInt(4, book.getPublicationYear());

            statement.executeUpdate();
            System.out.println("Book added to database Successfully");

            statement.close();
            con.close();


        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<Book> books = new ArrayList<>();



    public List<Book> getBooks()
    {
        return books;
    }
}
