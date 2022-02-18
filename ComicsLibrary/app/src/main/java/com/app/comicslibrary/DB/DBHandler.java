package com.app.comicslibrary.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.comicslibrary.Model.Author;
import com.app.comicslibrary.Model.CollectingComicNumbers;
import com.app.comicslibrary.Model.ComicBook;
import com.app.comicslibrary.Model.IncludedComicBook;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "comicsLibraryDB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_COMIC_BOOK = "comic_book";
    private static final String TABLE_INC_COMIC_BOOK = "inc_comic_book";
    private static final String TABLE_AUTHOR = "author";
    private static final String TABLE_COLLECT_COMIC_NUMBERS = "collect_comic_numbers";

    // Comic book Table Columns
    private static final String KEY_ID_BOOK = "id_book";
    private static final String KEY_NAME_BOOK = "name_book";
    private static final String KEY_STATUS = "status";
    private static final String KEY_PRICE = "price";

    // Included Comic book Table Columns
    private static final String KEY_ID_INC_BOOK = "id_inc_book";
    private static final String KEY_ID_BOOK_FK = "id_book";
    private static final String KEY_NAME_INC_BOOK = "name_inc_book";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PUBLISHED_DATE = "published_date";
    private static final String KEY_PATH_IMAGE = "path_image";

    // Author Table Columns
    private static final String KEY_ID_AUTHOR = "id_author";
    private static final String KEY_ID_INC_BOOK_FK = "id_inc_book";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";

    // Collecting comic numbers Table Columns
    private static final String KEY_ID_COLLECTING = "id_collecting";
    private static final String KEY_ID_INC_BOOK_FK2 = "id_inc_book";
    private static final String KEY_NAME_COLLECT_BOOK = "name_collect_book";

    private static DBHandler sInstance;

    public static synchronized DBHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DBHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COMIC_BOOK_TABLE = "CREATE TABLE " + TABLE_COMIC_BOOK +
                "(" +
                KEY_ID_BOOK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME_BOOK + " TEXT," +
                KEY_STATUS + " TEXT," +
                KEY_PRICE + " INTEGER" +
                ")";

        String CREATE_INC_COMIC_BOOK_TABLE = "CREATE TABLE " + TABLE_INC_COMIC_BOOK +
                "(" +
                KEY_ID_INC_BOOK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ID_BOOK_FK + " INTEGER REFERENCES " + TABLE_COMIC_BOOK + "," +
                KEY_NAME_INC_BOOK + " TEXT," +
                KEY_DESCRIPTION + " TEXT," +
                KEY_PUBLISHED_DATE + " TEXT," +
                KEY_PATH_IMAGE + " TEXT" +
                ")";

        String CREATE_AUTHOR_TABLE = "CREATE TABLE " + TABLE_AUTHOR +
                "(" +
                KEY_ID_AUTHOR + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ID_INC_BOOK_FK + " INTEGER REFERENCES " + TABLE_INC_COMIC_BOOK + "," +
                KEY_FIRST_NAME + " TEXT," +
                KEY_LAST_NAME + " TEXT" +
                ")";

        String CREATE_COLLECT_COMIC_NUMBERS_TABLE = "CREATE TABLE " + TABLE_COLLECT_COMIC_NUMBERS +
                "(" +
                KEY_ID_COLLECTING + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ID_INC_BOOK_FK2 + " INTEGER REFERENCES " + TABLE_INC_COMIC_BOOK + "," +
                KEY_NAME_COLLECT_BOOK + " TEXT" +
                ")";

        db.execSQL(CREATE_COMIC_BOOK_TABLE);
        db.execSQL(CREATE_INC_COMIC_BOOK_TABLE);
        db.execSQL(CREATE_AUTHOR_TABLE);
        db.execSQL(CREATE_COLLECT_COMIC_NUMBERS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMIC_BOOK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INC_COMIC_BOOK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHOR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECT_COMIC_NUMBERS);
            onCreate(db);
        }
    }

    // Insert a comicBook into the database
    public long addComicBook(ComicBook comicBook) {
        long row = 0;
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(KEY_NAME_BOOK, comicBook.getNameBook());
        values.put(KEY_STATUS, comicBook.getStatus());
        values.put(KEY_PRICE, comicBook.getPrice());

        // after adding all values we are passing
        // content values to our table.
        row = db.insert(TABLE_COMIC_BOOK, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();

        return row;
    }

    // Insert a includedComicBook into the database
    public long addIncludedComicBook(IncludedComicBook incComicBook) {
        long row = 0;
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(KEY_ID_BOOK_FK, incComicBook.getComicBook());
        values.put(KEY_NAME_INC_BOOK, incComicBook.getNameIncBook());
        values.put(KEY_DESCRIPTION, incComicBook.getDescription());
        values.put(KEY_PUBLISHED_DATE, incComicBook.getPublishedDate());
        values.put(KEY_PATH_IMAGE, incComicBook.getPathImage());

        // after adding all values we are passing
        // content values to our table.
        row = db.insert(TABLE_INC_COMIC_BOOK, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();

        return row;
    }

    // Insert a author into the database
    public long addAuthor(Author author) {
        long row = 0;

        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(KEY_ID_INC_BOOK_FK, author.getIncComicBook());
        values.put(KEY_FIRST_NAME, author.getFirstName());
        values.put(KEY_LAST_NAME, author.getLastName());

        // after adding all values we are passing
        // content values to our table.
        row = db.insert(TABLE_AUTHOR, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();

        return row;
    }

    // Insert a collecting comic numbers into the database
    public long addCollectingComicNumbers(CollectingComicNumbers collectComicNum) {
        long row = 0;
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(KEY_ID_INC_BOOK_FK2, collectComicNum.getIncComicBook());
        values.put(KEY_NAME_COLLECT_BOOK, collectComicNum.getNameCollectBook());

        // after adding all values we are passing
        // content values to our table.
        row = db.insert(TABLE_COLLECT_COMIC_NUMBERS, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();

        return row;
    }
    //Get all Comic Books from db
    public List<ComicBook> getAllComicBook(){
        List<ComicBook> readComicBooks = new ArrayList<>();

        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorBooks = db.rawQuery("SELECT * FROM " + TABLE_COMIC_BOOK, null);

        // moving our cursor to first position.
        if (cursorBooks.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                readComicBooks.add(new ComicBook(cursorBooks.getLong(0),
                        cursorBooks.getString(1),
                        cursorBooks.getString(2),
                        cursorBooks.getInt(3)));
            } while (cursorBooks.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorBooks.close();

        return readComicBooks;
    }

    //Get Included Comic Books by comic book id from db
    public List<IncludedComicBook> getIncComicBooksByIdForeignKey(long id){
        List<IncludedComicBook> readIncComicBooks = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorBooks = db.rawQuery("SELECT * FROM " + TABLE_INC_COMIC_BOOK +
                " WHERE " + KEY_ID_BOOK_FK + " = " + id, null);

        if (cursorBooks.moveToFirst()) {
            do {
                readIncComicBooks.add(new IncludedComicBook(cursorBooks.getLong(0),
                        cursorBooks.getLong(1),
                        cursorBooks.getString(2),
                        cursorBooks.getString(3),
                        cursorBooks.getString(4),
                        cursorBooks.getString(5)));
            } while (cursorBooks.moveToNext());
        }
        cursorBooks.close();

        return readIncComicBooks;
    }

    //Get Authors by included comic book id from db
    public List<Author> getAuthorsByIdForeignKey(long id){
        List<Author> readAuthors = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorAuthors = db.rawQuery("SELECT * FROM " + TABLE_AUTHOR +
                " WHERE " + KEY_ID_INC_BOOK_FK + " = " + id, null);

        if (cursorAuthors.moveToFirst()) {
            do {
                readAuthors.add(new Author(cursorAuthors.getLong(0),
                        cursorAuthors.getLong(1),
                        cursorAuthors.getString(2),
                        cursorAuthors.getString(3)));
            } while (cursorAuthors.moveToNext());
        }
        cursorAuthors.close();

        return readAuthors;
    }

    //Get Collecting Comic Numbers by included comic book id from db
    public List<CollectingComicNumbers> getCollectNumbersByIdForeignKey(long id){
        List<CollectingComicNumbers> readCollects = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorCollects = db.rawQuery("SELECT * FROM " + TABLE_COLLECT_COMIC_NUMBERS +
                " WHERE " + KEY_ID_INC_BOOK_FK2 + " = " + id, null);

        if (cursorCollects.moveToFirst()) {
            do {
                readCollects.add(new CollectingComicNumbers(cursorCollects.getLong(0),
                        cursorCollects.getLong(1),
                        cursorCollects.getString(2)));
            } while (cursorCollects.moveToNext());
        }
        cursorCollects.close();

        return readCollects;
    }

    //update Comic Book in db
    public void updateComicBook(ComicBook comicBook){
        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(KEY_NAME_BOOK, comicBook.getNameBook());
        values.put(KEY_STATUS, comicBook.getStatus());
        values.put(KEY_PRICE, comicBook.getPrice());

        db.update(TABLE_COMIC_BOOK, values, "id_book=?", new String[]{String.valueOf(comicBook.getIdBook())});
        db.close();
    }

    //update Included Comic book in db
    public void updateIncludedComicBook(IncludedComicBook incComicBook) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(KEY_ID_BOOK_FK, incComicBook.getComicBook());
        values.put(KEY_NAME_INC_BOOK, incComicBook.getNameIncBook());
        values.put(KEY_DESCRIPTION, incComicBook.getDescription());
        values.put(KEY_PUBLISHED_DATE, incComicBook.getPublishedDate());
        values.put(KEY_PATH_IMAGE, incComicBook.getPathImage());

        db.update(TABLE_INC_COMIC_BOOK, values, "id_inc_book=?", new String[]{String.valueOf(incComicBook.getIdIncBook())});
        db.close();
    }

    //update author
    public void updateAuthor(Author author){
        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(KEY_ID_INC_BOOK_FK, author.getIncComicBook());
        values.put(KEY_FIRST_NAME, author.getFirstName());
        values.put(KEY_LAST_NAME, author.getLastName());

        db.update(TABLE_AUTHOR, values, "id_author=?", new String[]{String.valueOf(author.getIdAuthor())});
        db.close();
    }

    //update comic numbers
    public void updateCollectingComicNumbers(CollectingComicNumbers collectComicNum){
        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(KEY_ID_INC_BOOK_FK2, collectComicNum.getIncComicBook());
        values.put(KEY_NAME_COLLECT_BOOK, collectComicNum.getNameCollectBook());

        db.update(TABLE_COLLECT_COMIC_NUMBERS, values, "id_collecting=?", new String[]{String.valueOf(collectComicNum.getIdCollecting())});
        db.close();
    }

    //delete authors
    public void deleteAuthor(long idAuthor){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_AUTHOR, "id_author=?", new String[]{String.valueOf(idAuthor)});
        db.close();
    }

    //delete collecting comic numbers
    public void deleteCollectingComicNumbers(long idCollect){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_COLLECT_COMIC_NUMBERS, "id_collecting=?", new String[]{String.valueOf(idCollect)});
        db.close();
    }

    //delete included book and all connected authors, collecting numbers
    public void deleteAllIncBookInfo(long idIncBook){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_COLLECT_COMIC_NUMBERS, "id_inc_book=?", new String[]{String.valueOf(idIncBook)});
        db.delete(TABLE_AUTHOR, "id_inc_book=?", new String[]{String.valueOf(idIncBook)});
        db.delete(TABLE_INC_COMIC_BOOK, "id_inc_book=?", new String[]{String.valueOf(idIncBook)});

        db.close();
    }

    //delete comic book and all connected inc books info
    public void deleteAllComicBookInfo(long idComicBook, List<Long>idIncBooks){
        SQLiteDatabase db = this.getWritableDatabase();

        //delete all inc books info
        for(Long idIncBook : idIncBooks){
            db.delete(TABLE_COLLECT_COMIC_NUMBERS, "id_inc_book=?", new String[]{String.valueOf(idIncBook)});
            db.delete(TABLE_AUTHOR, "id_inc_book=?", new String[]{String.valueOf(idIncBook)});
            db.delete(TABLE_INC_COMIC_BOOK, "id_inc_book=?", new String[]{String.valueOf(idIncBook)});
        }
        db.delete(TABLE_COMIC_BOOK, "id_book=?", new String[]{String.valueOf(idComicBook)});

        db.close();
    }

}
