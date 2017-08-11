package com.grgs93971.finddiff;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by masaki on 2017/08/01.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] titleArray = {
            "stage1",
            "stage2",
            "stage3",
            "stage4",
            "stage5",
            "stage6",
            "stage7",
            "stage8",
            "stage9",
            "stage10",
            "stage11",
            "stage12"
    };
    public ImageAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return titleArray[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.stageselthum,
            R.drawable.stageselthum,
            R.drawable.stageselthum,

            R.drawable.stageselthum,
            R.drawable.stageselthum,
            R.drawable.stageselthum,

            R.drawable.stageselthum,
            R.drawable.stageselthum,
            R.drawable.stageselthum,

            R.drawable.stageselthum,
            R.drawable.stageselthum,
            R.drawable.stageselthum
    };

    // Adapterから参照される新しいImageViewを作成
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.grid_item_image, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);
            holder.textView = (TextView)convertView.findViewById(R.id.textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.imageView.setImageResource(mThumbIds[position]);
        holder.textView.setText(titleArray[position]);

        return convertView;
    }
}
