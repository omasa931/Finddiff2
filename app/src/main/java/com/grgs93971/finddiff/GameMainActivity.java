package com.grgs93971.finddiff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class GameMainActivity extends AppCompatActivity {
    //Log用
    private final String TAG = "GameMainActivity:";
    private SurfaceView surfaceView;
    private GameMainSurfaceView mainSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemain);

        surfaceView = (SurfaceView) findViewById(R.id.GameMainSurfaceView);
        mainSurfaceView = new GameMainSurfaceView(this);

        //setContentView(mainSurfaceView);

        AdView mAdView = (AdView) findViewById(R.id.adView3);
        mAdView.loadAd(admobUtil.getAdRequest());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        TextView tv = (TextView)findViewById(R.id.count1);
        if (tv.getText().toString().equals("1")) {
            mainSurfaceView.finishLoop();
            moveTaskToBack(true);
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
}