package example.kozaczekapp.KozaczekItems;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  Article class implementation of one Article.
 */
public class Article implements Parcelable {
    Image image;
    String title;
    String description;
    String pubDate;
    String linkToArticle;

    /**
     * Constructor
     * @param pubDate Publication date as a string.
     * @param image Reference to Image Object.
     * @param linkGuide url link to article in kozaczek.pl website;
     * @param title Title of Article as a string.
     * @param description Description of Article as a String.
     */
    public Article(String pubDate, Image image, String linkGuide, String title, String description) {
        this.pubDate = pubDate;
        this.image = image;
        this.title = title;
        this.linkToArticle = linkGuide;
        this.description = description;
    }

    /**
     * Constructor to create instance of Article from Parcelable.
     * @param in parcelable
     */
    protected Article(Parcel in) {
        image = in.readParcelable(Image.class.getClassLoader());;
        title = in.readString();
        linkToArticle = in.readString();
        description = in.readString();
        pubDate = in.readString();
    }

    /**
     * Class creator for a Parcelable.
     */
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

    /**
     *
     * @return instance of Image object.
     */
    public Image getImage() {
        return image;
    }

    /**
     *
     * @return Public date as a string.
     */
    public String getPubDate() {
        return pubDate;
    }
    /**
     *
     * @return link to article on website.
     */
    public String getLinkToArticle() {
        return linkToArticle;
    }
    /**
     *
     * @return Description of Article as a String.
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return Title of article as a String
     */
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
        dest.writeString(title);
        dest.writeString(linkToArticle);
        dest.writeString(description);
        dest.writeString(pubDate);
    }
}
