package example.kozaczekapp.Service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.util.LruCache;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;

import example.kozaczekapp.Fragments.ArticleListFragment;
import example.kozaczekapp.KozaczekItems.Article;

public class KozaczekService extends IntentService {

    private static final String RECIVER = "reciver";
    private static final String URL = "url";

    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = maxMemory / 8;


    private static final String TAG = "KozaczekService";

    IntentFilter filter = new IntentFilter("BROADCAST_DOWNLOADED");


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
        HttpResponse response =  getResponseFromUrl(url);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                KozaczekParser parser = new KozaczekParser();
                ArrayList<Article> articles = parser.parse(response);
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.broadcastsample.PRZYKLADOWY");
                broadcastIntent.putParcelableArrayListExtra(ArticleListFragment.PARCELABLE_ARRAY_KEY,articles);
                sendBroadcast(broadcastIntent);
                Log.d(TAG, "onHandleIntent: Broadcast send...");
            }
    }

    public HttpResponse getResponseFromUrl(String url){
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://www.kozaczek.pl/rss/plotki.xml");
        httpGet.setHeader(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.ISO_8859_1);
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}

