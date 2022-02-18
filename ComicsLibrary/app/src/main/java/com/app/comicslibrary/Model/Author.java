package com.app.comicslibrary.Model;

import java.io.Serializable;

public class Author implements Serializable {
    private long idAuthor;
    private long incComicBook;
    private String firstName;
    private String lastName;

    public Author() {
    }

    public Author(long idAuthor, long incComicBook, String firstName,
                  String lastName) {
        this.idAuthor = idAuthor;
        this.incComicBook = incComicBook;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getIncComicBook() {
        return incComicBook;
    }

    public void setIncComicBook(long incComicBook) {
        this.incComicBook = incComicBook;
    }

    public long getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(long idAuthor) {
        this.idAuthor = idAuthor;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
