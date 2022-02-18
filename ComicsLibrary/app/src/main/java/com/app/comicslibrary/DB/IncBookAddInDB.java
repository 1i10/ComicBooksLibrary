package com.app.comicslibrary.DB;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.app.comicslibrary.ImageSaveAndLoad;
import com.app.comicslibrary.MenuFragments.ExpandableList.UpdatePosition;
import com.app.comicslibrary.MenuFragments.ListActions.ModelCollectView;
import com.app.comicslibrary.Model.Author;
import com.app.comicslibrary.Model.CollectingComicNumbers;
import com.app.comicslibrary.Model.IncludedComicBook;
import com.app.comicslibrary.StringConverter;

import java.util.List;

public class IncBookAddInDB {
    private Context context;

    public IncBookAddInDB(Context context) {
        this.context = context;
    }

    public void addAllInfoIncBook(ModelCollectView collect, Long idBook){
        DBHandler databaseHelper = DBHandler.getInstance(context);

        //get Included Comic book
        String incName = collect.getName();
        String description = collect.getDescription();
        String date = collect.getDate();

        IncludedComicBook addIncBook = new IncludedComicBook();
        addIncBook.setComicBook(idBook);
        addIncBook.setNameIncBook(incName);
        addIncBook.setDescription(description);
        addIncBook.setPublishedDate(date);

        // Add included comic book to the database
        long rowIndexIncComicBook = databaseHelper.addIncludedComicBook(addIncBook);
        addIncBook.setIdIncBook(rowIndexIncComicBook);
        //image set is null
        //and now need set path for image
        //store and save image path
        ImageSaveAndLoad saveImage = new ImageSaveAndLoad(context);
        Bitmap bmpImage = ((BitmapDrawable)collect.getImage().getDrawable()).getBitmap();

        String pathImage = saveImage.saveToInternalStorage(saveImage.resizeImage(bmpImage), rowIndexIncComicBook);
        addIncBook.setPathImage(pathImage);

        //update with new path image
        databaseHelper.updateIncludedComicBook(addIncBook);

        //get authors
        String authors = collect.getAuthors();
        String[] authorsName = authors.split(",");
        StringConverter convert = new StringConverter();

        for(String authorFullName : authorsName){
            String[] fullName = convert.getFirstAndLastName(authorFullName);

            Author addAuthor = new Author();
            addAuthor.setFirstName(fullName[0]);
            addAuthor.setLastName(fullName[1]);
            addAuthor.setIncComicBook(rowIndexIncComicBook);
            long rowIndexAuthor = databaseHelper.addAuthor(addAuthor);
            addAuthor.setIdAuthor(rowIndexAuthor);
        }
        //get collecting comic numbers
        String collectComicsNumbers = collect.getCollectBooks();
        String[] collectNames = collectComicsNumbers.split(",");
        for(String collectName : collectNames){
            CollectingComicNumbers addCollectBook = new CollectingComicNumbers();
            addCollectBook.setNameCollectBook(collectName);
            addCollectBook.setIncComicBook(rowIndexIncComicBook);
            long rowIndexCollectBook = databaseHelper.addCollectingComicNumbers(addCollectBook);
            addCollectBook.setIdCollecting(rowIndexCollectBook);
        }
    }

    public void addAuthorIncBook(String[] fullName, Long idIncBook){
        DBHandler databaseHelper = DBHandler.getInstance(context);

        Author addAuthor = new Author();
        addAuthor.setFirstName(fullName[0]);
        addAuthor.setLastName(fullName[1]);
        addAuthor.setIncComicBook(idIncBook);
        long rowIndexAuthor = databaseHelper.addAuthor(addAuthor);
        addAuthor.setIdAuthor(rowIndexAuthor);
    }

    public void addCollectingNumbersIncBook(String collectName, Long idIncBook){
        DBHandler databaseHelper = DBHandler.getInstance(context);

        CollectingComicNumbers addCollectBook = new CollectingComicNumbers();
        addCollectBook.setNameCollectBook(collectName);
        addCollectBook.setIncComicBook(idIncBook);
        long rowIndexCollectBook = databaseHelper.addCollectingComicNumbers(addCollectBook);
        addCollectBook.setIdCollecting(rowIndexCollectBook);
    }

    public List<Author> checkAndDeleteExtraAuthors(List<Author> list, int currentSize){
        DBHandler databaseHelper = DBHandler.getInstance(context);

        //check and delete extra authors
        if(list.size() > currentSize){
            //delete extra
            while(list.size() != currentSize){
                //delete extra in db
                databaseHelper.deleteAuthor(list.get(list.size()-1).getIdAuthor());
                //delete extra in authors list
                list.remove(list.size()-1);
            }
        }
        return list;
    }

    public List<CollectingComicNumbers> checkAndDeleteExtraCollecting(List<CollectingComicNumbers> list, int currentSize){
        DBHandler databaseHelper = DBHandler.getInstance(context);

        //check and delete extra collecting
        if(list.size() > currentSize){
            //delete extra
            while(list.size() != currentSize){
                //delete extra in db
                databaseHelper.deleteCollectingComicNumbers(list.get(list.size()-1).getIdCollecting());
                //delete extra in collecting list
                list.remove(list.size()-1);
            }
        }
        return list;
    }
}
