package com.library;

import java.util.Objects;

public class Book {
    private int isbn;
    private String title;
    private String author;
    private int publication_year;
    private int no_of_copies;

    public Book(int isbn,String title, String author, int publication_year,int no_of_copies)
    {
        this.isbn=isbn;
        this.title=title;
        this.author=author;
        this.publication_year=publication_year;
        this.no_of_copies=no_of_copies;
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

    public int getNo_of_copies(){ return no_of_copies; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn == book.isbn &&
                publication_year == book.publication_year &&
                no_of_copies == book.no_of_copies &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, title, author, publication_year, no_of_copies);
    }
}
