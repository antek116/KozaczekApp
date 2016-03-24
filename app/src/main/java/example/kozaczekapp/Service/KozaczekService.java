package example.kozaczekapp.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
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
        HttpResponse response = null;


            response =  getResponseFromUrl(url);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                DataWrapper dataWrapper = new DataWrapper(response);
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(DownloadResultReceiver.PROCESS_RESPONSE);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(DownloadResultReceiver.RESPONSE, dataWrapper);
                sendBroadcast(broadcastIntent);
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

