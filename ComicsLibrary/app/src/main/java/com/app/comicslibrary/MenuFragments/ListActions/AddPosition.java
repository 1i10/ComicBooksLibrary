package com.app.comicslibrary.MenuFragments.ListActions;

import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.app.comicslibrary.DB.DBHandler;
import com.app.comicslibrary.DB.IncBookAddInDB;
import com.app.comicslibrary.MainActivity;

import com.app.comicslibrary.Model.ComicBook;

import com.app.comicslibrary.R;

import java.util.ArrayList;
import java.util.Calendar;

public class AddPosition extends AppCompatActivity {

    private Dialog dialog;
    ImageView imageLoad;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    DatePickerDialog picker;
    EditText eTextDate;
    ArrayList<ModelCollectView> arrayList;
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

        //for search menu adding dialog window
        arrayList = getIntent().getParcelableArrayListExtra("selectedItemsInSearch");
        if (arrayList != null) {
            //get images
            for(int i = 0; i < arrayList.size(); i++){
                byte[] bytes = getIntent().getByteArrayExtra("bytesImages"+i);
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                ModelCollectView currentItem = arrayList.get(i);
                currentItem.setImage(bmp);
                arrayList.set(i, currentItem);
            }

            CollectViewAdapter numbersArrayAdapter = new CollectViewAdapter(AddPosition.this, arrayList);
            mIntentListener = numbersArrayAdapter;

            ListView numbersListView = findViewById(R.id.listViewCollecting);

            numbersListView.setAdapter(numbersArrayAdapter);
        }

        //save all
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String nameBook = textComicName.getText().toString();
                if(nameBook.isEmpty()){
                    Toast.makeText(AddPosition.this, "Пожалуйста, введите наименование комикса", Toast.LENGTH_SHORT).show();
                    return;
                }
                ComicBook addComicBook = new ComicBook();
                addComicBook.setNameBook(nameBook);
                String tPrice = textPrice.getText().toString();
                if(tPrice.isEmpty()){
                    addComicBook.setPrice(0);
                }
                else{
                    addComicBook.setPrice(Integer.parseInt(tPrice));
                }
                addComicBook.setStatus(spinStatus.getSelectedItem().toString());

                // Get singleton instance of database
                DBHandler databaseHelper = DBHandler.getInstance(AddPosition.this);

                // Add comic book to the database
                long rowIndexComicBook = databaseHelper.addComicBook(addComicBook);
                addComicBook.setIdBook(rowIndexComicBook);

                IncBookAddInDB addIncBookInfo = new IncBookAddInDB(getApplicationContext());
                //get other objects from view
                for (ModelCollectView collect : arrayList) {
                    addIncBookInfo.addAllInfoIncBook(collect, rowIndexComicBook);
                }
                Toast.makeText(AddPosition.this, "Комикс добавлен в список", Toast.LENGTH_SHORT).show();
                //back to first fragment
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        //dialog window
        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
           public void onClick(View v){
               dialog = new Dialog(AddPosition.this);
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
                          Toast.makeText(AddPosition.this, "Пожалуйста, введите наименование комикса", Toast.LENGTH_SHORT).show();
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

                      CollectViewAdapter numbersArrayAdapter = new CollectViewAdapter(AddPosition.this, arrayList);
                      mIntentListener = numbersArrayAdapter;

                      ListView numbersListView = findViewById(R.id.listViewCollecting);

                      numbersListView.setAdapter(numbersArrayAdapter);

                      dialog.cancel();

                      Toast.makeText(AddPosition.this, "Комикс добавлен в список", Toast.LENGTH_SHORT).show();
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
                       picker = new DatePickerDialog(AddPosition.this,
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
}