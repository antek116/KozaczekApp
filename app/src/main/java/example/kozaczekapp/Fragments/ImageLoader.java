package example.kozaczekapp.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

public class ImageLoader extends AsyncTask<Void, Void, Drawable> {
    private final WeakReference<ImageView> imageViewReference;
    private  Context context;
    private String imageUrl;
    private LruCache<String,Bitmap> mLruCache;
    public ImageLoader(ImageView imageView,Context context, String imageName, LruCache<String,Bitmap> lruCache) {
        imageViewReference = new WeakReference<>(imageView);
        this.context = context;
        this.imageUrl = imageName;
        this.mLruCache = lruCache;
    }


    @Override
    protected Drawable doInBackground(Void... params) {
        return loadImageFromUrl(context, imageUrl);
    }



    @Override
    protected void onPostExecute(Drawable drawable) {
        if (drawable != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageDrawable(drawable);
            }
        }
    }
    private Drawable loadImageFromUrl(Context context,String imageUrl){
        Drawable myDrawable = null;
        try{
            URL url = new URL(imageUrl);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            myDrawable = resize(context,bmp);

        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return myDrawable;
    }
    private Drawable resize(Context context, Bitmap image) {
        Bitmap bitmapResized = Bitmap.createScaledBitmap(image, 80, 80, false);
        mLruCache.put(imageUrl,bitmapResized);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }
}
