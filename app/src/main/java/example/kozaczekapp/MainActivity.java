package example.kozaczekapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import example.kozaczekapp.Fragments.ArticleListFragment;
import example.kozaczekapp.Service.DataWrapper;
import example.kozaczekapp.Service.DownloadResultReceiver;
import example.kozaczekapp.Service.KozaczekParser;
import example.kozaczekapp.Service.KozaczekService;

public class MainActivity extends AppCompatActivity  implements DownloadResultReceiver.Receiver{
    ArticleListFragment articlesFragment = new ArticleListFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String stringUrl = "http://www.kozaczek.pl/rss/plotki.xml";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);




        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DownloadResultReceiver mReceiver = new DownloadResultReceiver(new Handler());
                mReceiver.setReceiver(this);
                Intent inputIntent = new Intent(MainActivity.this, KozaczekService.class);
                inputIntent.putExtra("url","ABCD");
                inputIntent.putExtra("receiver", mReceiver);
                inputIntent.putExtra("requestId", 101);
                startService(inputIntent);
        } else {
            Toast.makeText(this,"No Connection to Internet",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case KozaczekService.STATUS_RUNNING:

                break;
            case KozaczekService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                DataWrapper data = (DataWrapper) resultData.getParcelableArrayList("Articles");

                articlesFragment.updateTasksInList();

                break;
            case KozaczekService.STATUS_ERROR:
                /* Handle the error */
                Toast.makeText(this, "SOMETHING GOES WRONG", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
