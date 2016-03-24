package example.kozaczekapp.KozaczekItems;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {
    Image image;
    String linkGuide;
    String title;
    String description;
    String pubDate;

    public Article(String pubDate, Image image, String linkGuide, String title, String description) {
        this.pubDate = pubDate;
        this.image = image;
        this.linkGuide = linkGuide;
        this.title = title;
        this.description = description;
    }

    protected Article(Parcel in) {
        image = in.readParcelable(Image.class.getClassLoader());
        linkGuide = in.readString();
        title = in.readString();
        description = in.readString();
        pubDate = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public Image getImage() {
        return image;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getLinkGuide() {
        return linkGuide;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(image, flags);
        dest.writeString(linkGuide);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(pubDate);
    }
}
