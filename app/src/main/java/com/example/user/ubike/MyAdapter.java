package com.example.user.ubike;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 16/05/2017.
 */

public class MyAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<UbikeList> UbikeLists;
    private Button favorite_change;
    int favorite_num,favorite_end;
    SQLiteDatabase dbrw;

    public MyAdapter(Context context, List<UbikeList> ubikelist,int bundle){
        myInflater = LayoutInflater.from(context);
        this.UbikeLists = ubikelist;
        this.favorite_num = bundle;
    }
    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle;
        TextView txtTotal;
        TextView txtAvailable;
        TextView txtTime;
        Button favorite_change;

        public ViewHolder(TextView txtTitle, TextView txtTime, TextView txtTotal,TextView txtAvailable,Button favorite_change){
            this.txtTitle = txtTitle;
            this.txtTime = txtTime;
            this.txtTotal = txtTotal;
            this.txtAvailable=txtAvailable;
            this.favorite_change=favorite_change;
        }
    }
    public int getFavorite_num(){
        return favorite_end;
    }
    @Override
    public int getCount() {
        return UbikeLists.size();
    }

    @Override
    public Object getItem(int arg0) {
        return UbikeLists.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return UbikeLists.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = myInflater.inflate(R.layout.listview, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.title),
                    (TextView) convertView.findViewById(R.id.time),
                    (TextView) convertView.findViewById(R.id.total),
                    (TextView) convertView.findViewById(R.id.available),
                    (Button) convertView.findViewById(R.id.favourite)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        getFavorite_num();

        UbikeList ubikelist = (UbikeList) getItem(position);

        //0 = movie, 1 = title, 2 = nine
        int color_title[] = {Color.BLACK, Color.WHITE, Color.YELLOW};
        int color_time[] = {Color.BLACK, Color.WHITE, Color.YELLOW};
        int color_back[] = {Color.WHITE, Color.WHITE, Color.WHITE};
        int time_vis[] = {View.VISIBLE, View.GONE, View.VISIBLE};

        int type_num = ubikelist.getType();
        holder.txtTitle.setText(ubikelist.getName());
        holder.txtTitle.setTextColor(color_title[type_num]);
        holder.txtTitle.setBackgroundColor(color_back[type_num]);
        holder.txtTotal.setText(ubikelist.getTotal());
        holder.txtTotal.setTextColor(color_title[type_num]);
        holder.txtTotal.setBackgroundColor(color_back[type_num]);
        holder.txtAvailable.setText(ubikelist.getAvailable());
        holder.txtAvailable.setTextColor(color_title[type_num]);
        holder.txtAvailable.setBackgroundColor(color_back[type_num]);
        holder.txtTime.setText(ubikelist.getTime());
        holder.txtTime.setTextColor(color_time[type_num]);
        holder.txtTime.setVisibility(time_vis[type_num]);

//        MyDBHelper dbHelper = Ubike.;
//        dbrw = dbHelper.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//            cv.put("title", Ubike.title_name_all[i]);
//            cv.put("favorite","0");
//            dbrw.insert("myTable", null, cv);

        holder.favorite_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favorite_num == 0){
                    favorite_num=1;
                    favorite_end=favorite_num;
                }else if(favorite_num == 1){
                    favorite_num=0;
                    favorite_end=favorite_num;
                }
            }
        });
        return convertView;
    }

}
