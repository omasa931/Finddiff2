package com.grgs93971.finddiff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class GameMainActivity  extends AppCompatActivity {
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
}