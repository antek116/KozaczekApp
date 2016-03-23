package example.kozaczekapp.Service;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;

public class DataWrapper extends SparseArray<Parcelable> implements Parcelable {
    private ArrayList<Article> articles;

    public DataWrapper(ArrayList<Article> articles) {
        this.articles = articles;
    }

    protected DataWrapper(Parcel in) {
        articles = in.createTypedArrayList(Article.CREATOR);
    }

    public static final Creator<DataWrapper> CREATOR = new Creator<DataWrapper>() {
        @Override
        public DataWrapper createFromParcel(Parcel in) {
            return new DataWrapper(in);
        }

        @Override
        public DataWrapper[] newArray(int size) {
            return new DataWrapper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(articles);
    }
}
