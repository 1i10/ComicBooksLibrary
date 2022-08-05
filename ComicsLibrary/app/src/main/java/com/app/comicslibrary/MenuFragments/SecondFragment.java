package com.app.comicslibrary.MenuFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.comicslibrary.Jsoup.ParseBookInfo;

import com.app.comicslibrary.MainActivity;
import com.app.comicslibrary.MenuFragments.ListActions.AddPosition;
import com.app.comicslibrary.MenuFragments.ListActions.ModelCollectView;
import com.app.comicslibrary.MenuFragments.SearchMenu.SearchListAdapter;
import com.app.comicslibrary.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class SecondFragment extends Fragment {

    ArrayList<ModelCollectView> listParseComics;
    ArrayList<ModelCollectView> selectedComics;
    SearchListAdapter searchArrayAdapter;
    private ProgressBar loadingBar;
    private Context context;

    public SecondFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            View view2 = inflater.inflate(R.layout.check_ur_connection, container, false);

            Button retry = (Button)view2.findViewById(R.id.buttonRetry);

            //refresh fragment
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void  onClick(View v){
                    Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.detach(currentFragment);
                    fragmentTransaction.attach(currentFragment);
                    fragmentTransaction.commit();
                }
            });

            return view2;
        }

        View view = inflater.inflate(R.layout.fragment_second, container, false);

        EditText searchLine = (EditText) view.findViewById(R.id.searchLine);
        Button searchButton = (Button) view.findViewById(R.id.searchButton);
        ListView searchList = (ListView) view.findViewById(R.id.listViewSearch);
        Button saveSearchButton = (Button) view.findViewById(R.id.buttonSaveSearch);
        loadingBar = (ProgressBar)view.findViewById(R.id.loadingProgressBar);

        listParseComics = new ArrayList<>();

        //search and display result list
        searchButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void  onClick(View v){
                String textSearch = searchLine.getText().toString();

                if(textSearch.isEmpty() || textSearch.length() <= 3){
                    Toast.makeText(getContext(), "Введите более 3 символов в строку поиска", Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingBar.setVisibility(View.VISIBLE);

                ParseBookInfo getInfo = new ParseBookInfo(getContext());
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listParseComics = getInfo.searchComicsInfo(textSearch, 1);//change numberpage later
                            } catch (IOException e) {
                                Log.d("PARSEERROR", "Search comics is fail");
                            }
                            ((MainActivity)context).runOnUiThread(new Runnable() {
                                public void run() {
                                    loadingBar.setVisibility(View.GONE);
                                    searchArrayAdapter = new SearchListAdapter(container.getContext(), listParseComics);
                                    searchList.setAdapter(searchArrayAdapter);
                                }
                            });

                        }
                    }).start();
                }
            }
        });

        //select position and call add menu
        selectedComics = new ArrayList<>();
        saveSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listParseComics == null || searchArrayAdapter == null){
                    Toast.makeText(getContext(), "Нет данных для добавления", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean []statusItems = searchArrayAdapter.getChecked();

                Intent intent = new Intent(getActivity(), AddPosition.class);
                for (int i = 0; i < statusItems.length; i++) {
                    if(statusItems[i]){
                        ModelCollectView selectedItem = listParseComics.get(i);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        selectedItem.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bytes = stream.toByteArray();

                        intent.putExtra("bytesImages" + i, bytes);
                        selectedItem.setImage(null);
                        selectedComics.add(selectedItem);
                    }
                }
                intent.putParcelableArrayListExtra("selectedItemsInSearch", selectedComics);
                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
