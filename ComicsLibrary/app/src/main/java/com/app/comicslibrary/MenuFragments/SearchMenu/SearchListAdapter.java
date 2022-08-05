package com.app.comicslibrary.MenuFragments.SearchMenu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.comicslibrary.MenuFragments.ListActions.ModelCollectView;
import com.app.comicslibrary.R;

import java.util.ArrayList;
import java.util.List;

public class SearchListAdapter extends ArrayAdapter<ModelCollectView>  {
    private List<ModelCollectView> list = new ArrayList<>();
    private Context context;
    boolean[] checked;
    ViewHolder holder = null;


    // invoke the suitable constructor of the ArrayAdapter class
    public SearchListAdapter(@NonNull Context context, ArrayList<ModelCollectView> arrayList) {
        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
        this.list = arrayList;
        checked = new boolean[this.list.size()];
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.search_menu_item_for_list, parent, false);
            holder = new ViewHolder();
            currentItemView.setTag(holder);
            holder.chBox = (CheckBox) currentItemView.findViewById(R.id.checkBoxAddList);
            holder.chBox.setTag(holder);
        }else{
            holder = (ViewHolder) currentItemView.getTag();
            holder.chBox.setTag(holder);
        }

        holder.chBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checked[position] = !checked[position];
            }
        });
        holder.chBox.setChecked(checked[position]);


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

        // then return the recyclable view
        return currentItemView;
    }

    public boolean[] getChecked(){
        return checked;
    }

    private static class ViewHolder {
        public CheckBox chBox = null;
    }
}
