package example.kozaczekapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import example.kozaczekapp.Fragments.ArticleListFragment;
import example.kozaczekapp.Service.KozaczekService;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String stringUrl = "http://www.kozaczek.pl/rss/plotki.xml";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        ArticleListFragment listArticle = new ArticleListFragment();





        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
                Intent inputIntent = new Intent(MainActivity.this, KozaczekService.class);
                inputIntent.putExtra("url","ABCD");
                inputIntent.putExtra("requestId", 101);
                startService(inputIntent);
        } else {
            Toast.makeText(this,"No Connection to Internet",Toast.LENGTH_SHORT).show();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new ArticleListFragment())
                .commit();
    }
}
