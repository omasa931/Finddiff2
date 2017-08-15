package com.grgs93971.finddiff;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;

public class GameMainActivity extends AppCompatActivity {
    //Log用
    private final String TAG = "GameMainActivity:";
    private SurfaceView surfaceView;
    private GameMainSurfaceView mainSurfaceView;
    private String mStagenum;
    private DataBaseHelper mDbHelper;
    private SQLiteDatabase db;
    // 正解リスト
    private ArrayList<Integer> ansXPoslist = new ArrayList();
    private ArrayList<Integer> ansYPoslist = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemain);

        //起動引数のSTAGE_NO取得
        Intent intent = getIntent();
        mStagenum = intent.getStringExtra("STAGE_NO");

        // DB初期設定
        // setDatabase(mStagenum);
        //データベースヘルパーのインスタンスを作成する（まだデータベースはできない）
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        //データベースオブジェクトを取得する（データベースにアクセスすると作成される。）
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        dbHelper.onCreate(db);

        String selSql = "SELECT xpos, ypos FROM game_answer WHERE stageno=?";
        Cursor cursor = db.rawQuery(selSql, new String[]{mStagenum});
        try {
            while (cursor.moveToNext()) {
                int xpos = cursor.getInt(cursor.getColumnIndex("xpos"));
                int ypos = cursor.getInt(cursor.getColumnIndex("ypos"));

                ansXPoslist.add(xpos);
                ansYPoslist.add(ypos);
            }
        } finally {
            cursor.close();
        }

        //データベースを閉じる
        db.close();

        surfaceView = (SurfaceView) findViewById(R.id.GameMainSurfaceView);
        mainSurfaceView = new GameMainSurfaceView(this);

        TextView tv = (TextView)findViewById(R.id.stagename);
        tv.setText("Stage " + mStagenum);

        AdView mAdView = (AdView) findViewById(R.id.adView3);
        mAdView.loadAd(admobUtil.getAdRequest());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        TextView tv = (TextView)findViewById(R.id.count1);
        if (tv.getText().toString().equals(FinddiffConst.RIGHT_ANSWER_COUNT)) {
            mainSurfaceView.finishLoop();
            Intent intent = new Intent(getApplication(), FinishStageActivity.class);
            startActivity(intent);
        }
        Log.d("activityから正解数",tv.getText().toString());

        return true;
    }

    @Override
    protected void onPostResume() {
        Log.i(TAG, "onPostResume()");
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
        getDelegate().onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
        getDelegate().onDestroy();
    }

    private void setDatabase(String stageno) {
        //データベースヘルパーのインスタンスを作成する（まだデータベースはできない）
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        //データベースオブジェクトを取得する（データベースにアクセスすると作成される。）
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);

        String selSql = "SELECT * FROM game_answer WHERE stageno=1";
        Cursor cursor = db.rawQuery(selSql, new String[]{stageno});
        try {
            if (cursor.moveToNext()) {
                int xpos = cursor.getInt(cursor.getColumnIndex("xpos"));
                int ypos = cursor.getInt(cursor.getColumnIndex("ypos"));

                ansXPoslist.add(xpos);
                ansYPoslist.add(ypos);
            }
        } finally {
            cursor.close();
        }

        //データベースを閉じる
        db.close();
    }

    public int getStagenum() {
        return Integer.parseInt(mStagenum);
    }

    public ArrayList getXposList() {
        return ansXPoslist;
    }

    public ArrayList getYposList() {
        return ansYPoslist;
    }

}