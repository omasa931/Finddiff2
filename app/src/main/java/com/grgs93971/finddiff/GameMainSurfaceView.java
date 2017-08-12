package com.grgs93971.finddiff;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


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
        ansXPoslist.add(0, new Integer("40"));
        ansYPoslist.add(0, new Integer("40"));

//        ansXPoslist.add(1, new Integer("300"));
//        ansYPoslist.add(1, new Integer("400"));

//        ansXPoslist.add(2, new Integer("500"));
//        ansYPoslist.add(2, new Integer("500"));
//
//        ansXPoslist.add(3, new Integer("600"));
//        ansYPoslist.add(3, new Integer("100"));
//
//        ansXPoslist.add(4, new Integer("700"));
//        ansYPoslist.add(4, new Integer("400"));
    }

    /*サーフェイスが初めて作られたときに呼ばれる、*/
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated(SurfaceHolder holder)");
        bitmapSrc1 = BitmapFactory.decodeResource(getResources(), R.drawable.dummy);
        bitmapSrc2 = BitmapFactory.decodeResource(getResources(), R.drawable.dummy);

        Log.d("bitmap 高", String.valueOf(bitmapSrc1.getHeight()));
        Log.d("bitmap 幅", String.valueOf(bitmapSrc1.getWidth()));

        Log.d("bitmap 高2", String.valueOf(DispUtil.convertPx2Dp(bitmapSrc1.getHeight(), mContext)));
        Log.d("bitmap 幅2", String.valueOf(DispUtil.convertPx2Dp(bitmapSrc1.getWidth(), mContext)));

        int w1 = bitmapSrc1.getWidth();
        int h1 = bitmapSrc1.getHeight();
        float w2 = DispUtil.convertPx2Dp(bitmapSrc1.getWidth(), mContext);
        float h2 = DispUtil.convertPx2Dp(bitmapSrc1.getHeight(), mContext);
        float scaleWidth = w2 / w1;
        float scaleHeight = h2 / h1;
        float scaleFactor = Math.min(scaleWidth, scaleHeight);
        Matrix scale = new Matrix();
        scale.postScale(scaleFactor, scaleFactor);

        xPos = getStartXpos((int)w2);
        yPos2 = getStartYpos2((int)h2);

        bitmap1 = Bitmap.createBitmap(bitmapSrc1, 0, 0, w1, h1, scale, false);
        bitmap2 = Bitmap.createBitmap(bitmapSrc2, 0, 0, w1, h1, scale, false);

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

                for (int i = 0; i < xPoslist.size(); i++) {
                    canvas.drawCircle(xPoslist.get(i), yPoslist.get(i), 40.f, new Paint());
                }
               holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    List<Float> pointArray = new ArrayList<Float>();
    //なぞられた点を記録するリスト
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("X:Y", event.getX() + ":" + event.getY());

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

        int yStartPos = getStartYpos1() + bitmapHeight + getStartYpos1();

        return yStartPos;
    }
}