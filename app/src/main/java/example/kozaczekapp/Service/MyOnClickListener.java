package example.kozaczekapp.Service;


import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import example.kozaczekapp.MainActivity;
import example.kozaczekapp.R;

public class MyOnClickListener implements View.OnClickListener {
    String urlToArticle;
    MainActivity activity;
    public MyOnClickListener(String url){
        this.urlToArticle = url;
    };
    public MyOnClickListener(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.CardView :
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(urlToArticle));
                v.getContext().startActivity(i);
                break;
            case R.id.refresh :
                if(activity.checkNetworkConnection()) {
                    ImageView image;
                    if(((image = (ImageView) activity.findViewById(R.id.item_refresh))) != null) {
                        image.setClickable(false);
                    }
                    activity.startOrStopRefreshingAnimation(true, 1);
                    activity.startService(activity.getKozaczekServiceIntent());
                } else {
                    activity.startOrStopRefreshingAnimation(true, 2);
                    Toast.makeText(activity.getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }
}
