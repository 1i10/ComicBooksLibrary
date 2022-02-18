package com.app.comicslibrary.Model;

public class ComicBook {
    private long idBook;
    private String nameBook;
    private String status;
    private int price;

    public ComicBook() {
    }

    public ComicBook(long idBook, String nameBook, String status, int price) {
        this.idBook = idBook;
        this.nameBook = nameBook;
        this.status = status;
        this.price = price;
    }

    public long getIdBook() {
        return idBook;
    }

    public void setIdBook(long idBook) {
        this.idBook = idBook;
    }

    public String getNameBook() {
        return nameBook;
    }

    public void setNameBook(String nameBook) {
        this.nameBook = nameBook;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
