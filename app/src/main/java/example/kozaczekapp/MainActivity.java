package example.kozaczekapp;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import example.kozaczekapp.Fragments.ArticleListFragment;
import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.Service.KozaczekService;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_KEY = "ArticleListFragmentSaveState";

    ArticleListFragment listArticle;
    Intent inputIntent;
    ImageView image;
    private IntentFilter filter = new IntentFilter("com.example.broadcastsample.PRZYKLADOWY");
    private MenuItem refreshMenuItem;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Article> articles = intent.getParcelableArrayListExtra(ArticleListFragment.PARCELABLE_ARRAY_KEY);
            listArticle.updateTasksInList(articles);
            stopService(inputIntent);
            setRefreshing(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String stringUrl = "http://www.kozaczek.pl/rss/plotki.xml";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        inputIntent = new Intent(MainActivity.this, KozaczekService.class);
        inputIntent.putExtra("url", "ABCD");
        inputIntent.putExtra("requestId", 101);

        if (savedInstanceState == null) {
            if (networkInfo != null && networkInfo.isConnected()) {
                listArticle = new ArticleListFragment();
                setRefreshing(true);
                startService(inputIntent);

            } else {
                Toast.makeText(this, "No Connection to Internet", Toast.LENGTH_SHORT).show();
            }
        } else {
            listArticle = (ArticleListFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }

        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout iv = (FrameLayout) inflater.inflate(R.layout.iv_refresh, null);
        image = (ImageView) iv.findViewById(R.id.refresh);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setClickable(false);
                setRefreshing(true);
                startService(inputIntent);
            }
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, listArticle)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        refreshMenuItem = menu.findItem(R.id.item_refresh);
        refreshMenuItem.setActionView(image);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putParcelable(FRAGMENT_KEY,listArticle.getAdapter());
        getSupportFragmentManager().putFragment(outState, "mContent", listArticle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        listArticle = (ArticleListFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    private void setRefreshing(boolean refreshing) {
        if (refreshMenuItem != null) {
            if (refreshing) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(image, "rotation", 0f, 360f);
                anim.start();

            } else {
                image.setClickable(true);
            }
        }
    }
}
