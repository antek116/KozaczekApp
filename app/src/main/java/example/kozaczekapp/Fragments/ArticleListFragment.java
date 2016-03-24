package example.kozaczekapp.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.R;
import example.kozaczekapp.Service.DataWrapper;
import example.kozaczekapp.Service.DownloadResultReceiver;


public class ArticleListFragment extends Fragment {
    ArticleListAdapter adapter;
    public static final String PARCELABLE_ARRAY_KEY = "FragmentParcelable";


    public ArticleListFragment(){

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.article_list_layout, container, false);
        adapter = new ArticleListAdapter(view.getContext());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.allTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }
    public void updateTasksInList(ArrayList<Article> articles){
        adapter.replaceListOfArtiles(articles);
        adapter.notifyDataSetChanged();
    }

}
