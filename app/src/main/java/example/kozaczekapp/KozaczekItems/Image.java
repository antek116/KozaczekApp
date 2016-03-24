package example.kozaczekapp.KozaczekItems;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {

    String imageUrl;

    String imageSize;


    public Image(Parcel in) {
        imageUrl = in.readString();
        imageSize = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getImageSizet() {
        return imageSize;
    }


    public Image(String imageUrl, String imageSize) {
        this.imageUrl = imageUrl;
        this.imageSize = imageSize;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageLenght() {
        return imageSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(imageSize);
    }
}
