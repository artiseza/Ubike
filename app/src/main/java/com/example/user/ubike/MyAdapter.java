package com.example.user.ubike;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 16/05/2017.
 */

public class MyAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<UbikeList> UbikeLists;

    public MyAdapter(Context context, List<UbikeList> ubikelist){
        myInflater = LayoutInflater.from(context);
        this.UbikeLists = ubikelist;
    }
    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle;
        TextView txtTotal;
        TextView txtAvailable;
        TextView txtTime;
        public ViewHolder(TextView txtTitle, TextView txtTime, TextView txtTotal,TextView txtAvailable){
            this.txtTitle = txtTitle;
            this.txtTime = txtTime;
            this.txtTotal = txtTotal;
            this.txtAvailable=txtAvailable;
        }
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
                    (TextView) convertView.findViewById(R.id.available)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
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

        return convertView;
    }
}
