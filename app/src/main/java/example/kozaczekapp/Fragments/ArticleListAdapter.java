package example.kozaczekapp.Fragments;


import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.R;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder>  {
    Context context;
    private ArrayList<Article> articcles = new ArrayList<>();
    ArticleListAdapter(Context context){
        this.context = context;
    }

    public void setArticles(ArrayList<Article> articles)
    {
        this.articcles = articles;
    }
    @Override
    public ArticleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ArticleListAdapter.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private TextView mTitle;
        private TextView mDescription;
        private TextView mPublicDate;

        /**
         * Constructor where we set reference to layout of progressbar and TextView.
         *
         * @param view instance of view.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.articleImage);
            mTitle = (TextView) itemView.findViewById(R.id.titleTextView);
            mDescription = (TextView) itemView.findViewById(R.id.descriptionTextView);
            mPublicDate = (TextView) itemView.findViewById(R.id.publicDateTextView);
        }
    }
}
