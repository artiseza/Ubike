package com.example.user.ubike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListSetUserPhotoDialogAdapter extends ArrayAdapter<String> {
    //region define variable
    private final Context context;
    private final String[] values1;
    private TextView filter_text;
    private ImageView filter_image;
    //endregion

    //region Adapter
    public ListSetUserPhotoDialogAdapter(Context context, String[] values1) {
        super(context, R.layout.filter_dialog_list, values1);
        this.context = context;
        this.values1 = values1;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.filter_dialog_list, parent, false);

        ComponentInit(rowView, parent);
        ShowText(position);
//        ShowImage(position);

        return rowView;
    }
    //endregion

    //region initiate
    private void ComponentInit(View rowView, ViewGroup parent) {
        filter_text = (TextView) rowView.findViewById(R.id.filter_text);
//        filter_image = (ImageView) rowView.findViewById(R.id.filter_image);
    }
    //endregion

    //region Show
    private void ShowText(int position) {
        filter_text.setText(values1[position]);
    }

//    private void ShowImage(int position) {
//        if (position == 1) //"依「價錢」低到高排序"
//            filter_image.setImageBitmap(AsyncImageLoader.readBitMap(R.drawable.menoy_l_h));
//        else if (position == 2)  //"依「價錢」高到低排序"
//            filter_image.setImageBitmap(AsyncImageLoader.readBitMap(R.drawable.menoy_h_l));
//        else if (position == 3)  //"依「評價」排序"
//            filter_image.setImageBitmap(AsyncImageLoader.readBitMap(R.drawable.star_black));
//        else if (position == 4)  //"依「評價次數」排序"
//            filter_image.setImageBitmap(AsyncImageLoader.readBitMap(R.drawable.commit_total));
//        else if (position == 0)  //"依「距離」近到遠排序"
//            filter_image.setImageBitmap(AsyncImageLoader.readBitMap(R.drawable.distance_l_h));
//    }
    //endregion
}
