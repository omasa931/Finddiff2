package com.grgs93971.finddiff;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



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

    private int[] mThumbIds = new int[FinddiffConst.STAGE_COUNT];

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

        for (int i = 0 ; i < FinddiffConst.STAGE_COUNT; i++) {
            mThumbIds[i] = getStageThumId(i+1);
        }

        holder.imageView.setImageResource(mThumbIds[position]);
        holder.textView.setText(titleArray[position]);
        holder.textView.setTextSize(1f);

        return convertView;
    }



    private int getStageThumId(int stageno) {

        String stagefilename = "stgthum" + stageno;
        int resId = mContext.getResources().getIdentifier(stagefilename, "drawable", mContext.getPackageName());
        return resId;
    }
}
