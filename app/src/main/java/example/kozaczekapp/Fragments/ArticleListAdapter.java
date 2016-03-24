package example.kozaczekapp.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.R;

/**
 * Class implementation of TaskPreview adapter.
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> implements Parcelable {
    ArrayList<Article> listOfArticles = new ArrayList<>();
    Context context;
    private LruCache<String,Bitmap> mLruCache;
    /**
     * Constructor where we initialize ArrayList of tasks;
     */
    public ArticleListAdapter(Context context) {

        this.context = context;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;
        Log.d("LRUCACHE", "max memory " + maxMemory + " cache size " + cacheSize);
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    protected ArticleListAdapter(Parcel in) {
        listOfArticles = in.createTypedArrayList(Article.CREATOR);
    }

    public static final Creator<ArticleListAdapter> CREATOR = new Creator<ArticleListAdapter>() {
        @Override
        public ArticleListAdapter createFromParcel(Parcel in) {
            return new ArticleListAdapter(in);
        }

        @Override
        public ArticleListAdapter[] newArray(int size) {
            return new ArticleListAdapter[size];
        }
    };

    public Bitmap getBitmapFromMemCache(String key) {
        return mLruCache.get(key);
    }
    /**
     * Method to replace ArrayList of Tasks.
     * @param list ArrayList of Tasks.
     */
    public void replaceListOfArtiles(ArrayList<Article> list)
    {
        this.listOfArticles = list;
    }

    /**
     * This method calls onCreateViewHolder(ViewGroup, int) to create a new RecyclerView.ViewHolder
     * and initializes some private fields to be used by RecyclerView.
     *
     * @return instance of ViewHolder;
     */
    @Override
    public ArticleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_element_layout, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of TextView and ProgressBar.
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ArticleListAdapter.ViewHolder holder, int position) {
        if(listOfArticles.size() != 0) {
            holder.mTitle.setText(listOfArticles.get(position).getTitle());
            holder.mDescription.setText(listOfArticles.get(position).getDescription());
            holder.mPubData.setText(listOfArticles.get(position).getPubDate());
            loadImage(holder,position);
        }

    }
    public void loadImage(ArticleListAdapter.ViewHolder holder ,int position){
        String imageUrl = listOfArticles.get(position).getImage().getImageUrl();
        if(getBitmapFromMemCache(imageUrl) != null){
            holder.imageView.setImageBitmap(mLruCache.get(imageUrl));
        }
        else {
            ImageLoader loader = new ImageLoader(holder.imageView, context, imageUrl, mLruCache);
            loader.execute();
        }
    }

    /**
     *
     * @return number of Tasks in List.
     */
    @Override
    public int getItemCount() {
        return this.listOfArticles.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listOfArticles);
    }

    /**
     * class of ViewHolder implementation.
     * Use to remember each element in list.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView mDescription;
        public final TextView mPubData;
        public final ImageView imageView;

        /**
         * Constructor where we set reference to layout of progressbar and TextView.
         *
         * @param view instance of view.
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.titleTextView);
            mTitle.setTextSize(10);
            mDescription = (TextView) view.findViewById(R.id.descriptionTextView);
            mDescription.setTextSize(8);
            mPubData = (TextView) view.findViewById(R.id.publicDateTextView);
            mPubData.setTextSize(5);
            imageView = (ImageView) view.findViewById(R.id.articleImage);
        }

        /**
         * @return text as String from titleTextView.
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }


}
