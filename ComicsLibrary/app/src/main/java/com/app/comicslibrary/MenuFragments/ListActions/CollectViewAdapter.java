package com.app.comicslibrary.MenuFragments.ListActions;
import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.comicslibrary.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CollectViewAdapter extends ArrayAdapter<ModelCollectView> implements OnIntentReceived  {
    private List<ModelCollectView> list = new ArrayList<>();
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

    // invoke the suitable constructor of the ArrayAdapter class
    public CollectViewAdapter(@NonNull Context context, ArrayList<ModelCollectView> arrayList) {
        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
        list = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_collecting_item_for_list_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        ModelCollectView currentPosition = getItem(position);

        //name
        TextView textViewName = currentItemView.findViewById(R.id.textViewName);
        textViewName.setText(currentPosition.getName());
        //image
        ImageView imageView = currentItemView.findViewById(R.id.imageView);
        imageView.setImageBitmap(currentPosition.getImage());

        //description
        TextView textViewDescription = currentItemView.findViewById(R.id.textViewDescription);
        textViewDescription.setText(currentPosition.getDescription());
        //authors
        TextView textViewAuthors = currentItemView.findViewById(R.id.textViewAuthors);
        textViewAuthors.setText(currentPosition.getAuthors());
        //collect
        TextView textViewCollect = currentItemView.findViewById(R.id.textViewCollect);
        textViewCollect.setText(currentPosition.getCollectBooks());
        //date
        TextView textViewDate = currentItemView.findViewById(R.id.textViewDate);
        textViewDate.setText(currentPosition.getDate());

        //delete position
        ImageButton deletePos = currentItemView.findViewById(R.id.imageButtonDelete);
        deletePos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        //edit position
        //dialog window
        ImageButton editPos = currentItemView.findViewById(R.id.imageButtonEdit);
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
                textName.setText(currentPosition.getName());
                TextView textDescription = dialog.findViewById(R.id.editDescription);
                textDescription.setText(currentPosition.getDescription());
                TextView textAuthors = dialog.findViewById(R.id.editAuthors);
                textAuthors.setText(currentPosition.getAuthors());
                TextView textCollectNum = dialog.findViewById(R.id.editCollectingNumbers);
                textCollectNum.setText(currentPosition.getCollectBooks());

                // edit list
                buttonAddInList.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        list.remove(position);
                        BitmapDrawable drawable = (BitmapDrawable) imageLoad.getDrawable();
                        Bitmap bitmapImage = drawable.getBitmap();
                        list.add(position, new ModelCollectView(bitmapImage,textName.getText().toString(),
                                textDescription.getText().toString(),textAuthors.getText().toString(),
                                textCollectNum.getText().toString(), eTextDate.getText().toString()));

                        notifyDataSetChanged();
                        dialog.cancel();

                        Toast.makeText(parent.getContext(), "Комикс изменен", Toast.LENGTH_SHORT).show();
                    }
                });

                //load image
                Button buttonLoadImage = dialog.findViewById(R.id.buttonLoadPicture);
                imageLoad = dialog.findViewById(R.id.imageView);
                imageLoad.setImageBitmap(currentPosition.getImage());

                buttonLoadImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });

                //edit date from picker
                eTextDate = dialog.findViewById(R.id.editTextDate);
                eTextDate.setText(currentPosition.getDate());

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


        // then return the recyclable view
        return currentItemView;

    }
}
