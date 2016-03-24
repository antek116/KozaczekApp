package example.kozaczekapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import example.kozaczekapp.Fragments.ArticleListAdapter;
import example.kozaczekapp.Fragments.ArticleListFragment;
import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.Service.KozaczekService;

public class MainActivity extends AppCompatActivity {
    ArticleListFragment listArticle;
    Intent inputIntent;
    ImageView image;
    private boolean refreshing;
    ImageView animCorsshair;
    private IntentFilter filter =
            new IntentFilter("com.example.broadcastsample.PRZYKLADOWY");
    private MenuItem refreshMenuItem, refreshMenuSecound;
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
    protected void onResume() {
        super.onResume();

        this.registerReceiver(receiver, filter);
        Log.d("MAINACTITY", "OnResume : Receiver register");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listArticle = new ArticleListFragment();
        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout iv = (FrameLayout) inflater.inflate(R.layout.iv_refresh, null);
        image = (ImageView)iv.findViewById(R.id.refresh);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setClickable(false);
                setRefreshing(true);
                startService(inputIntent);
            }
        });
        String stringUrl = "http://www.kozaczek.pl/rss/plotki.xml";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, listArticle)
                .commit();

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            inputIntent = new Intent(MainActivity.this, KozaczekService.class);
            inputIntent.putExtra("url", "ABCD");
            inputIntent.putExtra("requestId", 101);
            setRefreshing(true);
            startService(inputIntent);
        } else {
            Toast.makeText(this, "No Connection to Internet", Toast.LENGTH_SHORT).show();
        }

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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_refresh:
                setRefreshing(true);
                startService(inputIntent);
                break;
            default:
                break;
        }

        return true;
    }


    @Override
    public void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    private void setRefreshing(boolean refreshing) {
        if (refreshMenuItem != null) {
            if (refreshing) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(image,"rotation", 0f, 360f);
//                anim.setDuration(Long.MAX_VALUE);
                anim.start();

            } else {
                image.setClickable(true);
            }
        }
    }
}
