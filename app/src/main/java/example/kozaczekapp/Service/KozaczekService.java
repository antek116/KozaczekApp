package example.kozaczekapp.Service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.util.LruCache;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;

import example.kozaczekapp.Fragments.ArticleListFragment;
import example.kozaczekapp.ImageDownloader.ImageLoader;
import example.kozaczekapp.KozaczekItems.Article;

public class KozaczekService extends IntentService {

    public static final String URL = "url";
    public static final String INTENT_FILTER ="example.kozaczekapp.broadcast.intent.filter";
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
        String url = intent.getStringExtra(URL);
        HttpResponse response =  getResponseFromUrl(url);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                KozaczekParser parser = new KozaczekParser();
                ArrayList<Article> articles = parser.parse(response);
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(INTENT_FILTER);
                broadcastIntent.putParcelableArrayListExtra(ArticleListFragment.PARCELABLE_ARTICLE_ARRAY_KEY, articles);
                sendBroadcast(broadcastIntent);
                Log.d(TAG, "onHandleIntent: Broadcast send...");
            }
    }

    public HttpResponse getResponseFromUrl(String url){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}

