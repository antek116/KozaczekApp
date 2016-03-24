package example.kozaczekapp.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.apache.http.HttpResponse;

import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;


public abstract class DownloadResultReceiver extends BroadcastReceiver {

    public static final String PROCESS_RESPONSE ="ON_RESPONSE";
    public static final String RESPONSE = "RESPONSE";
    @Override
    public void onReceive(Context context, Intent intent) {
       DataWrapper dataWrapper =  intent.getParcelableExtra(RESPONSE);
       HttpResponse response =  dataWrapper.getResponse();

        KozaczekParser parser = new KozaczekParser();
        ArrayList<Article> articles = parser.parse(response);
        updateAdapter(articles);
    }

    public abstract void updateAdapter(ArrayList<Article> arrayArticleList);
}
