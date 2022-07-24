package com.app.comicslibrary.MenuFragments.ExpandableList;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.comicslibrary.DB.DBHandler;
import com.app.comicslibrary.DB.IncBookAddInDB;
import com.app.comicslibrary.ImageSaveAndLoad;
import com.app.comicslibrary.MainActivity;
import com.app.comicslibrary.MenuFragments.ListActions.CollectViewAdapter;
import com.app.comicslibrary.MenuFragments.ListActions.ModelCollectView;
import com.app.comicslibrary.MenuFragments.ListActions.OnIntentReceived;
import com.app.comicslibrary.Model.Author;
import com.app.comicslibrary.Model.CollectingComicNumbers;
import com.app.comicslibrary.Model.ComicBook;
import com.app.comicslibrary.Model.IncludedComicBook;
import com.app.comicslibrary.R;
import com.app.comicslibrary.StringConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdatePosition extends AppCompatActivity {
    private Dialog dialog;
    ImageView imageLoad;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    DatePickerDialog picker;
    EditText eTextDate;
    ArrayList<ModelCollectView> arrayList;
    List<ModelItemView> listChild;
    private OnIntentReceived mIntentListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_position);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Button buttonAdd = findViewById(R.id.buttonAdd);
        Button buttonSave = findViewById(R.id.buttonSave);

        arrayList = new ArrayList<ModelCollectView>();

        TextView textComicName = findViewById(R.id.editName);
        TextView textPrice = findViewById(R.id.editTextNumber);
        Spinner spinStatus = findViewById(R.id.spinner);

        Bundle extras = getIntent().getExtras();
        final Long idBook = extras.getLong("id");;
        if (extras != null) {
            //The key argument here must match that used in the other activity
            textComicName.setText(extras.getString("name"));
            textPrice.setText(extras.getString("price"));
            spinStatus.setSelection(getIndex(spinStatus, extras.getString("status")));

            listChild = new ArrayList<ModelItemView>();
            Intent i = getIntent();
            listChild = (List<ModelItemView>) i.getSerializableExtra("childList");

            for(ModelItemView child : listChild){
                IncludedComicBook incBook = child.getIncBook();

                ImageSaveAndLoad loadImage = new ImageSaveAndLoad(UpdatePosition.this);
                String imagePath = incBook.getPathImage();
                Bitmap bmpImage = loadImage.loadImageFromStorage(imagePath, incBook.getIdIncBook());

                StringConverter convert = new StringConverter();
                String strAuthors = convert.convertListOfAuthorsToString(child.getAuthors());
                String strCollects = convert.convertListOfCollectsToString(child.getCollects());

                arrayList.add(new ModelCollectView(bmpImage,incBook.getNameIncBook(),
                        incBook.getDescription(),strAuthors,
                        strCollects, incBook.getPublishedDate()));
            }

            CollectViewAdapter numbersArrayAdapter = new CollectViewAdapter(UpdatePosition.this, arrayList);
            mIntentListener = numbersArrayAdapter;

            ListView numbersListView = findViewById(R.id.listViewCollecting);

            numbersListView.setAdapter(numbersArrayAdapter);
        }

        //update all
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String nameBook = textComicName.getText().toString();
                if(nameBook.isEmpty()){
                    Toast.makeText(UpdatePosition.this, "Пожалуйста, введите наименование комикса", Toast.LENGTH_SHORT).show();
                    return;
                }
                ComicBook upComicBook = new ComicBook();
                upComicBook.setNameBook(nameBook);
                String tPrice = textPrice.getText().toString();
                if(tPrice.isEmpty()){
                    upComicBook.setPrice(0);
                }
                else{
                    upComicBook.setPrice(Integer.parseInt(tPrice));
                }
                upComicBook.setStatus(spinStatus.getSelectedItem().toString());

                upComicBook.setIdBook(idBook);
                // Get singleton instance of database
                DBHandler databaseHelper = DBHandler.getInstance(UpdatePosition.this);

                // Update comic book to the database
                databaseHelper.updateComicBook(upComicBook);

                //get other objects from view
                int currentPos = 0;
                for (ModelCollectView collect : arrayList) {
                    IncBookAddInDB addIncBookInfo = new IncBookAddInDB(getApplicationContext());

                    if(currentPos >= listChild.size()){//add new child
                        addIncBookInfo.addAllInfoIncBook(collect,idBook);

                        currentPos++;
                        continue;
                    }

                    //update
                    //get Included Comic book
                    String incName = collect.getName();
                    String description = collect.getDescription();
                    String date = collect.getDate();

                    IncludedComicBook upIncBook = new IncludedComicBook();
                    upIncBook.setComicBook(idBook);
                    upIncBook.setNameIncBook(incName);
                    upIncBook.setDescription(description);
                    upIncBook.setPublishedDate(date);

                    Long idIncBook = listChild.get(currentPos).getIncBook().getIdIncBook();
                    upIncBook.setIdIncBook(idIncBook);

                    //delete and update image
                    ImageSaveAndLoad delUpImage = new ImageSaveAndLoad(getApplicationContext());
                    if(delUpImage.deleteImageInStorage(idIncBook) == true) {//image was found
                        //save new image
                        Bitmap bmpImage = collect.getImage();

                        String pathImage = delUpImage.saveToInternalStorage(delUpImage.resizeImage(bmpImage), idIncBook);
                        upIncBook.setPathImage(pathImage);
                    }
                    //update Included Book
                    databaseHelper.updateIncludedComicBook(upIncBook);

                    //get authors
                    String authors = collect.getAuthors();
                    String[] authorsName = authors.split(",");
                    StringConverter convert = new StringConverter();

                    int currentAuthor = 0;
                    List<Author> listChildAuthors = listChild.get(currentPos).getAuthors();
                    for(String authorFullName : authorsName){
                        String[] fullName = convert.getFirstAndLastName(authorFullName);

                        if(currentAuthor >= listChildAuthors.size()){//add new authors
                            addIncBookInfo.addAuthorIncBook(fullName, idIncBook);
                        }
                        else{
                            //update authors
                            Author upAuthor = new Author();
                            upAuthor.setFirstName(fullName[0]);
                            upAuthor.setLastName(fullName[1]);
                            upAuthor.setIncComicBook(idIncBook);
                            upAuthor.setIdAuthor(listChildAuthors.get(currentAuthor).getIdAuthor());
                            databaseHelper.updateAuthor(upAuthor);
                        }

                        currentAuthor++;
                    }

                    addIncBookInfo.checkAndDeleteExtraAuthors(listChildAuthors, currentAuthor);

                    //get collecting comic numbers
                    String collectComicsNumbers = collect.getCollectBooks();
                    String[] collectNames = collectComicsNumbers.split(",");

                    int currentCollecting = 0;
                    List<CollectingComicNumbers> listChildCollecting = listChild.get(currentPos).getCollects();
                    for(String collectName : collectNames){

                        if(currentCollecting >= listChildCollecting.size()){//add new collects
                            addIncBookInfo.addCollectingNumbersIncBook(collectName, idIncBook);
                        }
                        else{
                            //update
                            CollectingComicNumbers upCollectBook = new CollectingComicNumbers();
                            upCollectBook.setNameCollectBook(collectName);
                            upCollectBook.setIncComicBook(idIncBook);
                            upCollectBook.setIdCollecting(listChildCollecting.get(currentCollecting).getIdCollecting());
                            databaseHelper.updateCollectingComicNumbers(upCollectBook);
                        }

                        currentCollecting++;
                    }

                    addIncBookInfo.checkAndDeleteExtraCollecting(listChildCollecting, currentCollecting);

                    currentPos++;
                }
                Toast.makeText(UpdatePosition.this, "Комикс был изменен", Toast.LENGTH_SHORT).show();
                //back to first fragment
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        //dialog window
        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog = new Dialog(UpdatePosition.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_dialog_add_include_comic_book);
                dialog.setCancelable(true);

                Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
                Button buttonAddInList = dialog.findViewById(R.id.buttonAddInList);

                buttonCancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        dialog.cancel();
                    }
                });

                // create a arraylist of the type CollectView
                buttonAddInList.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //add data in list
                        TextView textName = dialog.findViewById(R.id.editName2);
                        String collectName = textName.getText().toString();
                        if(collectName.isEmpty()){
                            Toast.makeText(UpdatePosition.this, "Пожалуйста, введите наименование комикса", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        TextView textDescription = dialog.findViewById(R.id.editDescription);
                        TextView textAuthors = dialog.findViewById(R.id.editAuthors);
                        TextView textCollectNum = dialog.findViewById(R.id.editCollectingNumbers);

                        BitmapDrawable drawable = (BitmapDrawable) imageLoad.getDrawable();
                        Bitmap bitmapImage = drawable.getBitmap();

                        arrayList.add(new ModelCollectView(bitmapImage,collectName,
                                textDescription.getText().toString(),textAuthors.getText().toString(),
                                textCollectNum.getText().toString(), eTextDate.getText().toString()));

                        CollectViewAdapter numbersArrayAdapter = new CollectViewAdapter(UpdatePosition.this, arrayList);
                        mIntentListener = numbersArrayAdapter;

                        ListView numbersListView = findViewById(R.id.listViewCollecting);

                        numbersListView.setAdapter(numbersArrayAdapter);

                        dialog.cancel();

                        Toast.makeText(UpdatePosition.this, "Комикс добавлен в список", Toast.LENGTH_SHORT).show();
                    }
                });

                //load image
                Button buttonLoadImage = dialog.findViewById(R.id.buttonLoadPicture);
                imageLoad = dialog.findViewById(R.id.imageView);

                buttonLoadImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });

                //edit date from picker
                eTextDate = dialog.findViewById(R.id.editTextDate);
                eTextDate.setInputType(InputType.TYPE_NULL);

                eTextDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        picker = new DatePickerDialog(UpdatePosition.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        eTextDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                });
                //clear listener
                mIntentListener = null;
                dialog.show();
            }
        });
    }
    //select image in gallery
    void selectImage(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the selectImage
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                if (mIntentListener != null) {
                    mIntentListener.onIntent(data, resultCode);
                }
                else{
                    Uri selectedImageUri = data.getData();

                    if (null != selectedImageUri) {
                        imageLoad.setImageURI(selectedImageUri);
                    }
                }
            }
        }
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
}

