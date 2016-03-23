package example.kozaczekapp.Service;

import android.accounts.Account;
import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;

public class KozaczekService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    private static final String ARTICLE_KEY = "Article";
    private static final String RECIVER = "reciver";
    private static final String URL = "url";

    private static final String TAG = "KozaczekService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public KozaczekService() {
        super(KozaczekService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: Service Started");
        final ResultReceiver receiver = intent.getParcelableExtra(RECIVER);
        String url = intent.getStringExtra(URL);
        Bundle bundle = new Bundle();
        ArrayList<Article> articles = null;

        try{
            HttpResponse response =  getResponseFromUrl(" ");
            KozaczekParser parser = new KozaczekParser(response);
            articles = parser.parse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bundle.putSparseParcelableArray(ARTICLE_KEY,new DataWrapper(articles));
        receiver.send(STATUS_FINISHED,bundle);
    }

    public HttpResponse getResponseFromUrl(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGetHC4 get = new HttpGetHC4("http://www.kozaczek.pl/rss/plotki.xml");
        HttpResponse response = null;
        response = client.execute(get);
        return response;
    }
}

