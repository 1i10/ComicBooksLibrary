package com.app.comicslibrary.MenuFragments.ExpandableList;

import com.app.comicslibrary.Model.Author;
import com.app.comicslibrary.Model.CollectingComicNumbers;
import com.app.comicslibrary.Model.IncludedComicBook;

import java.io.Serializable;
import java.util.List;

public class ModelItemView implements Serializable {
    private IncludedComicBook incBook;
    private List<Author> authors;
    private List<CollectingComicNumbers> collects;

    public ModelItemView() {
    }

    public ModelItemView(IncludedComicBook incBook, List<Author> authors,
                         List<CollectingComicNumbers> collects) {
        this.incBook = incBook;
        this.authors = authors;
        this.collects = collects;
    }

    public IncludedComicBook getIncBook() {
        return incBook;
    }

    public void setIncBook(IncludedComicBook incBook) {
        this.incBook = incBook;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<CollectingComicNumbers> getCollects() {
        return collects;
    }

    public void setCollects(List<CollectingComicNumbers> collects) {
        this.collects = collects;
    }
}
