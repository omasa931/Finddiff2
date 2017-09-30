package com.grgs93971.finddiff;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

public class DispUtil {
    private static final String TAG = "DispUtil";

    /**
     * dpからpixelへの変換
     *
     * @param dp
     * @param context
     * @return float pixel
     */
    public static float convertDp2Px(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * metrics.density;
    }

    /**
     * pixelからdpへの変換
     *
     * @param px
     * @param context
     * @return float dp
     */
    public static float convertPx2Dp(int px, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return px / metrics.density;
    }

    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public static void getDisplaySize(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        Display disp = activity.getWindowManager().getDefaultDisplay();

        Point size = new Point();
        disp.getSize(size);
        String width = "Width = " + size.x;
        String height = "Height = " + size.y;
        String height2 = "Height = " + size.y;

    }
}
