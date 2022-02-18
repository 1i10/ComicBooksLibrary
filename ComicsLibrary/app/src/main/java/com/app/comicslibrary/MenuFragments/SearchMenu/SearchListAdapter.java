package com.app.comicslibrary.MenuFragments.SearchMenu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.comicslibrary.MenuFragments.ExpandableList.ModelItemView;
import com.app.comicslibrary.MenuFragments.ListActions.ModelCollectView;
import com.app.comicslibrary.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchListAdapter extends ArrayAdapter<ModelCollectView> {
    private List<ModelCollectView> list = new ArrayList<>();
    private Context context;
    int check = 1;


    // invoke the suitable constructor of the ArrayAdapter class
    public SearchListAdapter(@NonNull Context context, ArrayList<ModelCollectView> arrayList) {
        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
        this.list = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.search_menu_item_for_list, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        ModelCollectView currentPosition = getItem(position);

        //name
        TextView textViewName = currentItemView.findViewById(R.id.textViewName);
        textViewName.setText(currentPosition.getName());
        //image
        ImageView imageView = currentItemView.findViewById(R.id.imageView);
        imageView.setImageDrawable(currentPosition.getImage().getDrawable());

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

        //select or deselect item
        ImageButton selectPos = currentItemView.findViewById(R.id.imageButtonAddList);
        selectPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check == 1){
                    selectPos.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_baseline_add_box_24));
                    check = 0;
                    //Log.d("SELECT", "sdfds");
                }
                else{
                    selectPos.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_baseline_add_box_24_2));
                    check = 1;
                    //Log.d("DESELECT", "dsfsd");
                }

                notifyDataSetChanged();
            }
        });

        // then return the recyclable view
        return currentItemView;

    }
}
