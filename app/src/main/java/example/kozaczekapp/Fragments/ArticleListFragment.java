package example.kozaczekapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.R;
import example.kozaczekapp.Service.DownloadResultReceiver;


public class ArticleListFragment extends Fragment {
    ArticleListAdapter adapter;
    DownloadResultReceiver receiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         adapter = new ArticleListAdapter(getContext());
       receiver =  new DownloadResultReceiver() {
            @Override
            public void updateAdapter(ArrayList<Article> arrayArticleList) {
                updateTasksInList(arrayArticleList);
            }
        };
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_list_layout, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.allTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }
    public void updateTasksInList(ArrayList<Article> articles){
        adapter.setArticles(articles);
        adapter.notifyDataSetChanged();
    }

}
