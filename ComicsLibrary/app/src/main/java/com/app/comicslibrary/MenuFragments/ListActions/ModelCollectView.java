package com.app.comicslibrary.MenuFragments.ListActions;

import android.widget.ImageView;

public class ModelCollectView {
    private ImageView image;
    private String name;
    private String description;
    private String authors;
    private String collectBooks;
    private String date;

    public ModelCollectView(ImageView image, String name, String description, String authors,
                            String collectBooks, String date) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.authors = authors;
        this.collectBooks = collectBooks;
        this.date = date;
    }



    public String getName() {
        return name;
    }

    public ImageView getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthors() {
        return authors;
    }

    public String getCollectBooks() {
        return collectBooks;
    }

    public String getDate() {
        return date;
    }
}
