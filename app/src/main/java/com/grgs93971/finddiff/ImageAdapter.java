package com.grgs93971.finddiff;


import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] titleArray = new String [FinddiffConst.STAGE_COUNT];

    public ImageAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        for (int i = 0; i < FinddiffConst.STAGE_COUNT ; i++) {
            titleArray[i] = "Stage" + (i + 1);
        }
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
        holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.text_size10));

        return convertView;
    }



    private int getStageThumId(int stageno) {

        String stagefilename = "stgthum" + stageno;
        int resId = mContext.getResources().getIdentifier(stagefilename, "drawable", mContext.getPackageName());
        return resId;
    }
}
