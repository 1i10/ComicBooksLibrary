package com.app.comicslibrary.MenuFragments.ExpandableList;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;

import com.app.comicslibrary.DB.DBHandler;
import com.app.comicslibrary.DB.IncBookAddInDB;
import com.app.comicslibrary.ImageSaveAndLoad;
import com.app.comicslibrary.MenuFragments.ListActions.AddPosition;
import com.app.comicslibrary.MenuFragments.ListActions.ModelCollectView;
import com.app.comicslibrary.MenuFragments.ListActions.OnIntentReceived;
import com.app.comicslibrary.Model.Author;
import com.app.comicslibrary.Model.CollectingComicNumbers;
import com.app.comicslibrary.Model.ComicBook;
import com.app.comicslibrary.Model.IncludedComicBook;
import com.app.comicslibrary.R;
import com.app.comicslibrary.StringConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class ExpandableListAdapter extends BaseExpandableListAdapter implements OnIntentReceived {
    private Context context;
    private List<ComicBook> expandableListTitle;
    private HashMap<ComicBook, List<ModelItemView>> expandableListDetail;

    private Dialog dialog;
    ImageView imageLoad;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    DatePickerDialog picker;
    EditText eTextDate;

    @Override
    public void onIntent(Intent i, int resultCode) {
        if(resultCode == RESULT_OK){
            Uri selectedImageUri = i.getData();
            //Log.d("sdfds3", selectedImageUri.toString());
            if (null != selectedImageUri) {
                imageLoad.setImageURI(selectedImageUri);
            }
        }
    }

    public ExpandableListAdapter(Context context, List<ComicBook> expandableListTitle,
                                       HashMap<ComicBook, List<ModelItemView>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ModelItemView expandedListItem = (ModelItemView) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_collecting_item_for_list_view, null);
        }

        IncludedComicBook incBook = expandedListItem.getIncBook();
        List<Author> authors = expandedListItem.getAuthors();
        List<CollectingComicNumbers> collects = expandedListItem.getCollects();
        //name
        TextView textViewName = (TextView) convertView.findViewById(R.id.textViewName);
        textViewName.setText(incBook.getNameIncBook());
        //image
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        ImageSaveAndLoad loadImage = new ImageSaveAndLoad(context);
        String imagePath = incBook.getPathImage();
        //Log.d("dfsdf", imagePath);
        Bitmap bmpImage = loadImage.loadImageFromStorage(imagePath, incBook.getIdIncBook());
        imageView.setImageBitmap(bmpImage);
        //description
        TextView textViewDescription = (TextView) convertView.findViewById(R.id.textViewDescription);
        textViewDescription.setText(incBook.getDescription());
        //authors
        StringConverter convert = new StringConverter();
        String strAuthors = convert.convertListOfAuthorsToString(authors);

        TextView textViewAuthors = (TextView) convertView.findViewById(R.id.textViewAuthors);
        textViewAuthors.setText(strAuthors);
        //collect
        String strCollects = convert.convertListOfCollectsToString(collects);

        TextView textViewCollect = (TextView) convertView.findViewById(R.id.textViewCollect);
        textViewCollect.setText(strCollects);
        //date
        TextView textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
        textViewDate.setText(incBook.getPublishedDate());

        final String eAuthors = strAuthors.replace(';',',');
        final String eCollects = strCollects.replace(';',',');

        //delete position
        ImageButton deletePos = convertView.findViewById(R.id.imageButtonDelete);
        deletePos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get singleton instance of database
                DBHandler databaseHelper = DBHandler.getInstance(parent.getContext());
                databaseHelper.deleteAllIncBookInfo(incBook.getIdIncBook());

                expandableListDetail.get(expandableListTitle.get(listPosition)).remove(expandedListPosition);
                notifyDataSetChanged();
            }
        });

        //edit position
        //dialog window
        ImageButton editPos = convertView.findViewById(R.id.imageButtonEdit);
        editPos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog = new Dialog(parent.getContext());
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

                TextView textName = dialog.findViewById(R.id.editName2);
                textName.setText(incBook.getNameIncBook());
                TextView textDescription = dialog.findViewById(R.id.editDescription);
                textDescription.setText(incBook.getDescription());
                TextView textAuthors = dialog.findViewById(R.id.editAuthors);
                textAuthors.setText(eAuthors);
                TextView textCollectNum = dialog.findViewById(R.id.editCollectingNumbers);
                textCollectNum.setText(eCollects);

                // edit list
                buttonAddInList.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        // Get singleton instance of database
                        DBHandler databaseHelper = DBHandler.getInstance(parent.getContext());

                        incBook.setNameIncBook(textName.getText().toString());
                        incBook.setDescription(textDescription.getText().toString());
                        incBook.setPublishedDate(eTextDate.getText().toString());
                        //delete and update image
                        ImageSaveAndLoad delUpImage = new ImageSaveAndLoad(parent.getContext());
                        if(delUpImage.deleteImageInStorage(incBook.getIdIncBook()) == true) {//image was found
                            //save new image
                            Bitmap bmpImage = ((BitmapDrawable)imageLoad.getDrawable()).getBitmap();

                            String pathImage = delUpImage.saveToInternalStorage(delUpImage.resizeImage(bmpImage), incBook.getIdIncBook());
                            incBook.setPathImage(pathImage);
                        }

                        //update inc book
                        databaseHelper.updateIncludedComicBook(incBook);

                        //update authors
                        String getAuthors = textAuthors.getText().toString();
                        String[] authorsName = getAuthors.split(",");

                        StringConverter convert = new StringConverter();
                        int currentPos = 0;

                        IncBookAddInDB upIncBookInfo = new IncBookAddInDB(context);
                        for(String authorFullName : authorsName){
                            String[] fullName = convert.getFirstAndLastName(authorFullName);
                            //check current authors
                            if(currentPos >= authors.size()) {
                                //add new author (new list > current list authors)
                                Author addAuthor = new Author();
                                addAuthor.setFirstName(fullName[0]);
                                addAuthor.setLastName(fullName[1]);
                                addAuthor.setIncComicBook(incBook.getIdIncBook());
                                long rowIndexAuthor = databaseHelper.addAuthor(addAuthor);
                                addAuthor.setIdAuthor(rowIndexAuthor);

                                authors.add(addAuthor);
                            }
                            else{
                                Author upAuthor = authors.get(currentPos);
                                String currentFirstName = upAuthor.getFirstName();
                                String currentLastName = upAuthor.getLastName();
                                if(currentFirstName != fullName[0] || currentLastName != fullName[1]){
                                    upAuthor.setFirstName(fullName[0]);
                                    upAuthor.setLastName(fullName[1]);
                                    //update current author
                                    databaseHelper.updateAuthor(upAuthor);
                                }
                            }

                            currentPos++;
                        }

                        upIncBookInfo.checkAndDeleteExtraAuthors(authors, currentPos);

                        //update collecting comic numbers
                        currentPos = 0;
                        String getCollects = textCollectNum.getText().toString();
                        String[] collectNames = getCollects.split(",");
                        for(String collectName : collectNames){
                            //check current collects
                            if(currentPos >= collects.size()) {
                                CollectingComicNumbers addCollectBook = new CollectingComicNumbers();
                                addCollectBook.setNameCollectBook(collectName);
                                addCollectBook.setIncComicBook(incBook.getIdIncBook());
                                long rowIndexCollectBook = databaseHelper.addCollectingComicNumbers(addCollectBook);
                                addCollectBook.setIdCollecting(rowIndexCollectBook);

                                collects.add(addCollectBook);
                            }
                            else{
                                CollectingComicNumbers upCollectBook = collects.get(currentPos);
                                String currentCollectName = upCollectBook.getNameCollectBook();
                                if(currentCollectName != collectName){
                                    upCollectBook.setNameCollectBook(collectName);

                                    //update current collect numbers
                                    databaseHelper.updateCollectingComicNumbers(upCollectBook);
                                }
                            }
                            currentPos++;
                        }

                        upIncBookInfo.checkAndDeleteExtraCollecting(collects, currentPos);

                        notifyDataSetChanged();
                        dialog.cancel();

                        Toast.makeText(parent.getContext(), "Комикс изменен", Toast.LENGTH_SHORT).show();
                    }
                });

                //load image
                Button buttonLoadImage = dialog.findViewById(R.id.buttonLoadPicture);
                imageLoad = dialog.findViewById(R.id.imageView);
                imageLoad.setImageBitmap(bmpImage);

                buttonLoadImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });

                //edit date from picker
                eTextDate = dialog.findViewById(R.id.editTextDate);
                eTextDate.setText(incBook.getPublishedDate());

                eTextDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        picker = new DatePickerDialog(parent.getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        eTextDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                });

                dialog.show();
            }

            //select image in gallery
            void selectImage(){
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                ((Activity)parent.getContext()).startActivityForResult(gallery, SELECT_PICTURE);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ComicBook listTitle = (ComicBook) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_collecting_parent_for_list_view, null);
        }
        //name
        TextView textComicName = (TextView) convertView.findViewById(R.id.listParentBookName);
        textComicName.setText(listTitle.getNameBook());
        //price
        TextView textPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
        textPrice.setText(listTitle.getPrice()+"");
        //status
        Button buttonStatus = (Button) convertView.findViewById(R.id.buttonIndicator);

        ColorStateList tint = ColorStateList.valueOf(getColorStatus(listTitle.getStatus()));
        setButtonTint(buttonStatus, tint);

        //edit parent
        ImageButton editPos = convertView.findViewById(R.id.imageButtonEditParent);
        editPos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(context, UpdatePosition.class);
                i.putExtra("id", listTitle.getIdBook());
                i.putExtra("name",listTitle.getNameBook());
                i.putExtra("price",listTitle.getPrice()+"");
                i.putExtra("status",listTitle.getStatus());
                List<ModelItemView> parentChild = expandableListDetail.get(expandableListTitle.get(listPosition));
                i.putExtra("childList", (Serializable) parentChild);
                context.startActivity(i);
            }

        });


        //delete position
        ImageButton deletePos = convertView.findViewById(R.id.imageButtonDeleteAll);
        deletePos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get singleton instance of database
                DBHandler databaseHelper = DBHandler.getInstance(parent.getContext());
                List<Long>idIncBooks = new ArrayList<Long>();
                ImageSaveAndLoad delImage = new ImageSaveAndLoad(parent.getContext());

                for(ModelItemView bookInfo : expandableListDetail.get(expandableListTitle.get(listPosition))){
                    idIncBooks.add(bookInfo.getIncBook().getIdIncBook());
                    //delete images in store
                    delImage.deleteImageInStorage(bookInfo.getIncBook().getIdIncBook());
                }

                databaseHelper.deleteAllComicBookInfo(listTitle.getIdBook(), idIncBooks);

                expandableListTitle.remove(listPosition);
                expandableListDetail.remove(listPosition);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public int getColorStatus(String status){
        int color = Color.RED;

        String s1 = "Прочитано";
        String s2 = "В процессе";

        if(status.intern() == s1){
            color = Color.GREEN;
        }
        else if(status.intern() == s2){
            color = Color.YELLOW;
        }

        return color;
    }

    @SuppressLint("RestrictedApi")
    public static void setButtonTint(Button button, ColorStateList tint) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP && button instanceof AppCompatButton) {
            ((AppCompatButton) button).setSupportBackgroundTintList(tint);
        } else {
            ViewCompat.setBackgroundTintList(button, tint);
        }
    }

}
