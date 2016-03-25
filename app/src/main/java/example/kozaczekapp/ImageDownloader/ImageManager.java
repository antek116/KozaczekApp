package example.kozaczekapp.ImageDownloader;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import example.kozaczekapp.KozaczekItems.Article;

public class ImageManager extends Observable implements Observer {


    private LruCache<String,Bitmap> mLruCache;
    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public ImageManager(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;
        Log.d("LRUCACHE", "max memory " + maxMemory + " cache size " + cacheSize);
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            /**
             * Method to return size of item in LRU cache;
             * @param key as String.
             * @param bitmap instance of Bitmap
             * @return size of bitmap in KB;
             */
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }


    public LruCache<String,Bitmap> getLruCache(){
        return this.mLruCache;
    }
    private Bitmap getBitmapFromMemCache(String key) {
        return mLruCache.get(key);
    }

    public void addImagesFromArticlesToLruCache(ArrayList<Article> articleArrayList) {
        for(Article article : articleArrayList)
        {
            String imageUrl = article.getImage().getImageUrl();
            if(!(imageUrl.isEmpty())){
                downloadBitmap(imageUrl);
            }
        }

    }
    private void downloadBitmap(String imageUrl){
        ImageLoad imageLoad = new ImageLoad(imageUrl,mLruCache,this);
        executor.execute(imageLoad);
    }

    @Override
    public void update(Observable observable, Object data) {
        notifyObservers();
    }
}
