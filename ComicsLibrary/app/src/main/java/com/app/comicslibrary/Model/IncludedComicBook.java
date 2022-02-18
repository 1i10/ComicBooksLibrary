package com.app.comicslibrary.Model;

import java.io.Serializable;

public class IncludedComicBook implements Serializable {
    private long idIncBook;
    private long comicBook;
    private String nameIncBook;
    private String description;
    private String publishedDate;
    private String pathImage;

    public IncludedComicBook() {
    }

    public IncludedComicBook(long idIncBook, long comicBook, String nameIncBook,
                             String description, String publishedDate, String pathImage) {
        this.idIncBook = idIncBook;
        this.comicBook = comicBook;
        this.nameIncBook = nameIncBook;
        this.description = description;
        this.publishedDate = publishedDate;
        this.pathImage = pathImage;
    }

    public long getComicBook() {
        return comicBook;
    }

    public void setComicBook(long comicBook) {
        this.comicBook = comicBook;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getNameIncBook() {
        return nameIncBook;
    }

    public void setNameIncBook(String nameIncBook) {
        this.nameIncBook = nameIncBook;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getIdIncBook() {
        return idIncBook;
    }

    public void setIdIncBook(long idIncBook) {
        this.idIncBook = idIncBook;
    }
}
