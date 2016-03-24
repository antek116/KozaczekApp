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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;

import example.kozaczekapp.Fragments.ArticleListFragment;
import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.Service.KozaczekService;
import example.kozaczekapp.Service.MyOnClickListener;

public class MainActivity extends AppCompatActivity {
    public static final String FRAGMENT_KEY = "ArticleListFragmentSaveState";
    public static final String SERVICE_URL = "http://www.kozaczek.pl/rss/plotki.xml";
    ArticleListFragment listArticle;
    Intent kozaczekServiceIntent;
    ImageView image;
    private ObjectAnimator anim;
    private IntentFilter filterAdapterArticlesChange = new IntentFilter(KozaczekService.INTENT_FILTER);
    private MenuItem refreshMenuItem;
    private BroadcastReceiver articlesRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Article> articles = intent.getParcelableArrayListExtra(ArticleListFragment.PARCELABLE_ARTICLE_ARRAY_KEY);
            listArticle.updateTasksInList(articles);
            stopService(kozaczekServiceIntent);
            startOrStopRefreshingAnimation(false, 0);
        }
    };

    public Intent getKozaczekServiceIntent() {
        return kozaczekServiceIntent;
    }

    private boolean isInternetConnection;
    private IntentFilter filterForInternetConnectionChange = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private BroadcastReceiver networkConnectionReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (checkNetworkConnection()) {
                isInternetConnection = true;
            } else {
                isInternetConnection = false;
            }
        }
    };

    /**
     * Methods where we initialize servis.
     * @param savedInstanceState saved instance bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kozaczekServiceIntent = new Intent(MainActivity.this, KozaczekService.class);
        kozaczekServiceIntent.putExtra(KozaczekService.URL, SERVICE_URL);
        initializationOfSaveInstanceState(savedInstanceState);
        initializationOfRefreshItemInMenu();
    }

    /**
     *Initialize the contents of the Activity's standard options menu.
     * @param menu  The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        refreshMenuItem = menu.findItem(R.id.item_refresh);
        refreshMenuItem.setActionView(image);
        return true;
    }

    /**
     * MEthod use to save instance of fragment.
     * @param outState bundle of saves states.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_KEY, listArticle);
    }

    /**
     * Method where we register two Receiver : ArticleRefresher and Network access change.
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(articlesRefreshReceiver, filterAdapterArticlesChange);
        registerReceiver(networkConnectionReceiver, filterForInternetConnectionChange);
    }
    /**
     * Method where we unregister two Receiver : ArticleRefresher and Network access change.
     */
    @Override
    public void onPause() {
        unregisterReceiver(articlesRefreshReceiver);
        unregisterReceiver(networkConnectionReceiver);
        super.onPause();
    }

    /**
     *  Method starting choosen by kind refreshing animation.
     * @param refreshing true if wanna start animation, false if wanna stop animation, animation is stoping by dooing last circle.
     * @param kind 1 - Infinite animation, 2 - one loop animation.
     */
    public void startOrStopRefreshingAnimation(boolean refreshing, int kind) {
        if (refreshMenuItem != null) {
            if (refreshing && kind == 1) {
                anim.setRepeatCount(ObjectAnimator.INFINITE);
                anim.setRepeatMode(ObjectAnimator.RESTART);
                anim.start();
            } else if (refreshing && kind == 2) {
                anim.setRepeatCount(1);
                anim.setRepeatMode(ObjectAnimator.REVERSE);
                anim.start();
            } else {
                anim.setRepeatCount(1);
                anim.start();
                image.setClickable(true);
            }
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initializationOfSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            listArticle = new ArticleListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container, listArticle).commit();
            isInternetConnection = checkNetworkConnection();
            if (isInternetConnection) {
                startOrStopRefreshingAnimation(true, 1);
                startService(kozaczekServiceIntent);
            }
            else{
                Toast.makeText(this,"NO INTERNET CONECTION",Toast.LENGTH_SHORT).show();
            }
        } else {
            listArticle = (ArticleListFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
        }
    }

    private void initializationOfRefreshItemInMenu() {
        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout iv = (FrameLayout) inflater.inflate(R.layout.iv_refresh, null);
        image = (ImageView) iv.findViewById(R.id.refresh);
        anim = ObjectAnimator.ofFloat(image, "rotation", 0f, 360f);
        image.setOnClickListener(new MyOnClickListener(this));
    }
}
