package com.app.comicslibrary.MenuFragments.ListActions;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import java.io.Serializable;


public class ModelCollectView implements Parcelable {
    private Bitmap image;
    private String name;
    private String description;
    private String authors;
    private String collectBooks;
    private String date;

    public ModelCollectView(Bitmap image, String name, String description, String authors,
                            String collectBooks, String date) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.authors = authors;
        this.collectBooks = collectBooks;
        this.date = date;
    }

    public ModelCollectView(Parcel source) {
        image = source.readParcelable(Bitmap.class.getClassLoader());
        name = source.readString();
        description = source.readString();
        authors = source.readString();
        collectBooks = source.readString();
        date = source.readString();
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image){
        this.image = image;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(image);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(authors);
        dest.writeString(collectBooks);
        dest.writeString(date);
    }

    public static final Creator<ModelCollectView> CREATOR = new Creator<ModelCollectView>() {

        @Override
        public ModelCollectView createFromParcel(Parcel source) {
            return new ModelCollectView(source);
        }

        @Override
        public ModelCollectView[] newArray(int size) {
            return new ModelCollectView[size];
        }
    };
}
