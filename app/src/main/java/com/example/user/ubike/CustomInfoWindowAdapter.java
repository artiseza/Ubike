package com.example.user.ubike;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by asus on 12/12/2016.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity context;


    public CustomInfoWindowAdapter(Activity context){
        this.context = context ;
    }

    @Override
    public View getInfoWindow(Marker marker){
        return null;
    }
    public void getInfofavourite(){return;}

    @Override
    public View getInfoContents(final Marker marker){
        final int[] favourite = {0};
        View view = context.getLayoutInflater().inflate(R.layout.customwindow, null);
        TextView tvTitle = (TextView)view.findViewById(R.id.tv_title);
        TextView tvSubTitle= (TextView)view.findViewById(R.id.tv_subtitle);
        final ToggleButton mToggleButton=(ToggleButton)view.findViewById(R.id.info_favourite);
        mToggleButton.setChecked(false);
        mToggleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mToggleButton.isChecked()) {    // 按鈕按下顯示可租按鈕及圖示

                } else {                              // 按鈕按下顯示可停按鈕及圖示

                }
            }
        });
        tvTitle.setText(marker.getTitle());
        tvSubTitle.setText(marker.getSnippet());
        getInfofavourite();

        return view;
    }
}
