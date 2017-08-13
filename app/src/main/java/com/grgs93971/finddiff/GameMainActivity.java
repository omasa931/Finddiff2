package com.grgs93971.finddiff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

public class GameMainActivity extends AppCompatActivity {
    //Log用
    private final String TAG = "GameMainActivity:";
    private SurfaceView surfaceView;
    private GameMainSurfaceView mainSurfaceView;
    private String mStagenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemain);

        surfaceView = (SurfaceView) findViewById(R.id.GameMainSurfaceView);

        Intent intent = getIntent();
        mStagenum = intent.getStringExtra("STAGE_NO");
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
        if (tv.getText().toString().equals("1")) {
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

    public int getStagenum() {
        return Integer.parseInt(mStagenum);
    }
}