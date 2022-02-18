package com.app.comicslibrary.Model;

import java.io.Serializable;

public class CollectingComicNumbers implements Serializable {
    private long idCollecting;
    private long incComicBook;
    private String nameCollectBook;

    public CollectingComicNumbers() {
    }

    public CollectingComicNumbers(long idCollecting, long incComicBook,
                                  String nameCollectBook) {
        this.idCollecting = idCollecting;
        this.incComicBook = incComicBook;
        this.nameCollectBook = nameCollectBook;
    }

    public long getIdCollecting() {
        return idCollecting;
    }

    public void setIdCollecting(long idCollecting) {
        this.idCollecting = idCollecting;
    }

    public long getIncComicBook() {
        return incComicBook;
    }

    public void setIncComicBook(long incComicBook) {
        this.incComicBook = incComicBook;
    }

    public String getNameCollectBook() {
        return nameCollectBook;
    }

    public void setNameCollectBook(String nameCollectBook) {
        this.nameCollectBook = nameCollectBook;
    }
}
