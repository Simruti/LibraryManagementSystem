package com.library;

public class Book {
    private int isbn;
    private String title;
    private String author;
    private int publication_year;

    public Book(int isbn,String title, String author, int publication_year)
    {
        this.isbn=isbn;
        this.title=title;
        this.author=author;
        this.publication_year=publication_year;
    }

    public int getIsbn(){
        return isbn;
    }

    public String getTitle()
    {
        return title;
    }

    public String getAuthor()
    {
        return author;
    }
    public int getPublicationYear()
    {
        return publication_year;
    }
}
