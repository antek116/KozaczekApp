package example.kozaczekapp.Service;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.http.HttpResponse;

public class DataWrapper implements Parcelable {
    public DataWrapper(HttpResponse response) {
        this.response = response;
    }

    private HttpResponse response;

    public HttpResponse getResponse(){
        return this.response;
    }

    protected DataWrapper(Parcel in) {
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
    }
}
