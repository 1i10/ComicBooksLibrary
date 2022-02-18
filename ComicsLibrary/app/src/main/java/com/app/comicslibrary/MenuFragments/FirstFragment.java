package com.app.comicslibrary.MenuFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.app.comicslibrary.DB.DBHandler;
import com.app.comicslibrary.MenuFragments.ExpandableList.ExpandableListAdapter;
import com.app.comicslibrary.MenuFragments.ExpandableList.ModelItemView;
import com.app.comicslibrary.MenuFragments.ListActions.AddPosition;
import com.app.comicslibrary.Model.Author;
import com.app.comicslibrary.Model.CollectingComicNumbers;
import com.app.comicslibrary.Model.ComicBook;
import com.app.comicslibrary.Model.IncludedComicBook;
import com.app.comicslibrary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirstFragment extends Fragment {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<ComicBook> expandableListTitle;
    HashMap<ComicBook, List<ModelItemView>> expandableListDetail;

    public FirstFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_fab);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        expandableListDetail = new HashMap<ComicBook, List<ModelItemView>>();
        expandableListTitle = new ArrayList<ComicBook>();

        // Get singleton instance of database
        DBHandler databaseHelper = DBHandler.getInstance(container.getContext());
        //get data from db
        List<ComicBook> comicBooks = databaseHelper.getAllComicBook();
        for (ComicBook comicBook : comicBooks) {
            long idComicBook = comicBook.getIdBook();
            List<IncludedComicBook> incBooks = databaseHelper.getIncComicBooksByIdForeignKey(idComicBook);

            List<ModelItemView> includeBooksInfo = new ArrayList<ModelItemView>();

            for(IncludedComicBook incComicBook : incBooks){
                long idIncBook = incComicBook.getIdIncBook();
                List<Author> authors = databaseHelper.getAuthorsByIdForeignKey(idIncBook);
                List<CollectingComicNumbers> collects = databaseHelper.getCollectNumbersByIdForeignKey(idIncBook);

                includeBooksInfo.add(new ModelItemView(incComicBook, authors, collects));
            }

            expandableListDetail.put(comicBook,includeBooksInfo);
            expandableListTitle.add(comicBook);
        }

        expandableListAdapter = new ExpandableListAdapter(container.getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        //fab add comic book
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v){
                Intent intent = new Intent(getActivity(), AddPosition.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
