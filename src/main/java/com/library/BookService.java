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
            String add_book = "INSERT INTO book(isbn,title,author,publication_year,no_of_copies) VALUES (?,?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(add_book);
            statement.setInt(1,book.getIsbn());
            statement.setString(2,book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setInt(4, book.getPublicationYear());
            statement.setInt(5,book.getNo_of_copies());

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
                int noOfCopies = resultSetBooks.getInt("no_of_copies");

                books.add(new Book(isbn, title, author, publicationYear, noOfCopies));

                System.out.println("ISBN: "+ isbn+ "\nTitle: "+ title +"\nAuthor: "+ author+ "\nPublication Year: "+ publicationYear +"\nNo of Copies: "+ noOfCopies);
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
}
