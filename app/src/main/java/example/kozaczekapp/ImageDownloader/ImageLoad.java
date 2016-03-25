package example.kozaczekapp.ImageDownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;

import java.io.IOException;
import java.net.URL;

public class ImageLoad implements Runnable {
    String imageUrl;
    LruCache<String,Bitmap> mLruCache;
    ImageManager imageManager;

    public ImageLoad(String imageUrl, LruCache<String, Bitmap> mLruCache, ImageManager imageManager) {
        this.imageUrl = imageUrl;
        this.mLruCache = mLruCache;
        this.imageManager = imageManager;
    }

    @Override
    public void run() {
        loadImageFromUrl(imageUrl);
    }
    private void loadImageFromUrl(String imageUrl){
        try{
            URL url = new URL(imageUrl);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            resize(bmp);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    private Bitmap resize(Bitmap image) {
        Bitmap bitmapResized = Bitmap.createScaledBitmap(image, 80, 80, false);
        if(mLruCache.get(imageUrl) == null) {
            mLruCache.put(imageUrl, bitmapResized);
        }
        return bitmapResized;
    }
}
