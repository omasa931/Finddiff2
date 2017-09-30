package com.grgs93971.finddiff;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import java.util.ArrayList;

public class GameMainSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    //Log用
    private final String TAG = "GameMainSurfaceView:";
    //Context
    private Context mContext;
    //サーフェイスの大きさやフォーマットを変えたり、描画するインターフェイス
    private SurfaceHolder holder;
    //描画に使うスレッド
    private static Thread thread;

    private static boolean mThread;
    //描画用ペイント
    private Paint paint = new Paint();
    // bitmap
    private Bitmap bitmap1;
    private Bitmap bitmapSrc1;

    private Bitmap bitmap2;
    private Bitmap bitmapSrc2;
    //bitmapの描画位置(X)
    private int xPos;
    //bitmap(2つめ)の描画位置(Y)
    private int yPos2;
    private ArrayList<Integer> xPoslist = new ArrayList();
    private ArrayList<Integer> yPoslist = new ArrayList();

    // 正解リスト
    private ArrayList<Integer> ansXPoslist = new ArrayList();
    private ArrayList<Integer> ansYPoslist = new ArrayList();

    //ステージ番号
    private String mStagenum;
    private final int RADIUS  = 30;
    private int rightAns = 0;
    /**
     * コンストラクタ
     *
     * @param context
     */
    public GameMainSurfaceView(Context context) {

        super(context);
        mContext = context;
        init();
    }

    /**
     * コンストラクタ
     *
     * @param context
     * @param attrs
     */
    public GameMainSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    /**
     * 初期処理
     */
    private void init() {
        /// このビューの描画内容がウインドウの最前面に来るようにする。
        //setZOrderOnTop(true);
        holder = getHolder();
        holder.addCallback(this);
        paint.setColor(Color.BLUE);

        GameMainActivity activity = (GameMainActivity)this.getContext();
//        ansXPoslist = activity.getXposList();
//        ansYPoslist = activity.getYposList();

    }

    /*サーフェイスが初めて作られたときに呼ばれる、*/
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated(SurfaceHolder holder)");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        bitmapSrc1 = BitmapFactory.decodeResource(getResources(), getDrawableId(), options);
        bitmapSrc2 = BitmapFactory.decodeResource(getResources(), getDrawableId2(), options);
        int hoseiWidth = 0; // 画像横位置の補正

        Log.d("bitmap 高", String.valueOf(bitmapSrc1.getHeight()));
        Log.d("bitmap 幅", String.valueOf(bitmapSrc1.getWidth()));

        Log.d("bitmap 高2", String.valueOf(DispUtil.convertPx2Dp(bitmapSrc1.getHeight(), mContext)));
        Log.d("bitmap 幅2", String.valueOf(DispUtil.convertPx2Dp(bitmapSrc1.getWidth(), mContext)));

        int w1 = bitmapSrc1.getWidth();         // 元画像の幅
        int h1 = bitmapSrc1.getHeight();        // 元画像の高さ
        float w2 = DispUtil.convertPx2Dp(bitmapSrc1.getWidth(), mContext);      //PX⇒DP
        float h2 = DispUtil.convertPx2Dp(bitmapSrc1.getHeight(), mContext);     //PX⇒DP

        DisplayMetrics metrics = DispUtil.getDisplayMetrics((GameMainActivity)this.getContext());
        float screenWidth = (float) metrics.widthPixels;       //端末のサイズ（幅）
        float screenHeight = (float) metrics.heightPixels;     //端末のサイズ（高）

        Matrix scale = new Matrix();
        float scaleWidth = w2 / w1;     //画像の幅／端末の幅
        float scaleHeight = h2 / h1;    //画像の高／端末の高
        float scaleFactor = Math.min(scaleWidth, scaleHeight);
        scale.postScale(scaleFactor, scaleFactor);

        xPos = getStartXpos((int) w2);      //開始位置(X)
        yPos2 = getStartYpos2((int) h2);    //開始位置(下段画像)

        bitmap1 = Bitmap.createBitmap(bitmapSrc1, 0, 0, w1, h1, scale, false);  //bitmap生成
        bitmap2 = Bitmap.createBitmap(bitmapSrc2, 0, 0, w1, h1, scale, false);  //bitmap生成

        hoseiWidth = (int)(screenWidth - w2) / 2;

        //端末のサイズ（幅）< 元画像の幅
        if (screenWidth < w2) {

            float f2 = new Float(FinddiffConst.SMALL_RATE);
            float scaleFactor2 = Math.min(f2, f2);
            Matrix scale2 = new Matrix();
            scale2.postScale(scaleFactor2, scaleFactor2);

            int w3 = (int) ((int)w2 * f2);  //縮小
            int h3 = (int) ((int)h2 * f2);  //縮小
            xPos = getStartXpos((int) w3);      //開始位置(X)
            yPos2 = getStartYpos2((int) h3);    //開始位置(下段画像)

            bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, (int)w2, (int)h2, scale2, false);  //bitmap生成
            bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, (int)w2, (int)h2, scale2, false);  //bitmap生成

            hoseiWidth = (int)(screenWidth - w3) / 2;
        }

        ArrayList xlist = ((GameMainActivity)this.getContext()).getXposList();

        for (int i = 0; i < xlist.size(); i++) {
            ansXPoslist.add(i, ((Integer)xlist.get(i) + hoseiWidth));
        }
        ansYPoslist = ((GameMainActivity)this.getContext()).getYposList();

        mThread = true;
        thread = new Thread(this);
    }

    /*サーフェイスの大きさやフォーマットが変わった時の処理*/
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        Log.i(TAG, "surfaceChanged(SurfaceHolder holder, int format, int width, int height)");
        thread.start();
    }

    /*サーフェイスが壊される直前に呼ばれる。*/
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed(SurfaceHolder holder)");
        mThread = false;
        while( thread != null && thread.isAlive());
        thread = null;
    }

    /**描画スレッドを実行する。*/
    @Override
    public void run() {
        Log.i(TAG, "run()");
        Canvas canvas = null;

        while (mThread) {
            canvas = holder.lockCanvas(null);
            //キャンバスをロック
            //synchronized (holder) {
            if (canvas != null) {
                //キャンバスに図形を描画
                //canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bitmap1, xPos, 0, new Paint());
                canvas.drawBitmap(bitmap2, xPos, yPos2, new Paint());

//                bitmap1.recycle();
//                bitmap2.recycle();

                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                paint.setColor(Color.RED);
                for (int i = 0; i < xPoslist.size(); i++) {
                    canvas.drawCircle(xPoslist.get(i), yPoslist.get(i), 40.f, paint);
                }
               holder.unlockCanvasAndPost(canvas);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i("X:Y", event.getX() + ":" + event.getY());

                int touchXpos = (int)event.getX();
                int touchYpos = (int)event.getY();

                for (int i = 0; i < ansXPoslist.size(); i++) {
                    boolean ue = false;
                    boolean shita = false;

                    if((touchXpos >= (ansXPoslist.get(i) - RADIUS) && touchXpos <= (ansXPoslist.get(i) + RADIUS))
                            && (touchYpos >= (ansYPoslist.get(i) - RADIUS) && touchYpos <= (ansYPoslist.get(i) + RADIUS))) {
                        ue = true;
                    }

                    if((touchXpos >= (ansXPoslist.get(i) - RADIUS) && touchXpos <= (ansXPoslist.get(i) + RADIUS))
                            && (touchYpos >= (ansYPoslist.get(i) + yPos2 - RADIUS) && touchYpos <= (ansYPoslist.get(i) + yPos2 + RADIUS))) {
                        shita = true;
                    }

                    if (ue || shita) {
                        //上段
                        xPoslist.add(ansXPoslist.get(i));
                        yPoslist.add(ansYPoslist.get(i));

                        //下段
                        xPoslist.add(ansXPoslist.get(i));
                        yPoslist.add(ansYPoslist.get(i) + yPos2);

                        //正解を削除
                        ansXPoslist.remove(i);
                        ansYPoslist.remove(i);

                        //正解数をカウントアップ
                        rightAns++;
                        Log.d("残正解数：", String.valueOf(ansXPoslist.size()));
                    }
                }
                break;
        }

        TextView tv = (TextView)((Activity)mContext).findViewById(R.id.count1);
        tv.setText(String.valueOf(rightAns));

        // 全問正解したらスレッドを停止
        if (ansXPoslist.size() == 0) {
            mThread = false;
        }

        return super.onTouchEvent(event);
    }

    public void finishLoop() {
        Log.i(TAG, "finishLoop()");
        synchronized (thread) {
            mThread = false;
        }
        try{
            thread.join();
        } catch( InterruptedException ex ){
            Thread.currentThread().interrupt();
        }
    }

    private int getStartXpos(int bitmapWidth) {

        Activity activity = (Activity)this.getContext();
        DisplaySizeCheck ds = new DisplaySizeCheck();
        Point point =  DisplaySizeCheck.getRealSize(activity);
        int dispWidth = point.x;
        int xStartPos = (dispWidth - bitmapWidth) / 2;

        return xStartPos;
    }

    private int getStartYpos1() {

        Activity activity = (Activity)this.getContext();
        DisplaySizeCheck ds = new DisplaySizeCheck();
        Point point =  DisplaySizeCheck.getRealSize(activity);
        int dispHeight = point.y;
        int yStartPos = dispHeight / 100;

        return yStartPos;
    }

    private int getStartYpos2(int bitmapHeight) {

        int yStartPos =  bitmapHeight + getStartYpos1();

        return yStartPos;
    }

    private int getDrawableId() {
        GameMainActivity activity = (GameMainActivity)this.getContext();
        int num = activity.getStagenum();
        String stagefilename = "dummy";

        stagefilename = "stage"+ num  + "_1";

        int resId = getResources().getIdentifier(stagefilename, "drawable", mContext.getPackageName());
        return resId;
    }

    private int getDrawableId2() {

        GameMainActivity activity = (GameMainActivity)this.getContext();
        int num = activity.getStagenum();
        String stagefilename = "dummy";

        stagefilename = "stage"+ num  + "_2";
        int resId = getResources().getIdentifier(stagefilename, "drawable", mContext.getPackageName());
        return resId;
    }
}