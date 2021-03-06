package com.grgs93971.finddiff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

public class FinishStageActivity extends Activity {

    private final String TAG = "FinishStageActivity:";
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishstage);

        interstitial = new InterstitialAd(this);
        String  ad = getResources().getString(R.string.interstitial_ad_unit_id);
        interstitial.setAdUnitId(ad);
        interstitial.loadAd(admobUtil.getAdRequest());
        interstitial.setAdListener(new AdListener() {
            //広告がロードできた時
            @Override
            public void onAdLoaded() {
                //super.onAdLoaded();
                displayInterstitial();
            }

            //広告がロードできなかった時
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            //広告が閉じられた時
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        });
        ImageButton toTopBtn = (ImageButton)findViewById(R.id.toTop);
        toTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);

            }
        });

        ImageButton toSelectBtn = (ImageButton)findViewById(R.id.toSelect);
        toSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SelectStageActivity.class);
                startActivity(intent);
            }
        });
    }
    // インタースティシャルを表示する準備ができたら、displayInterstitial() を呼び出す。
    public void displayInterstitial() {
//        if (interstitial.isLoaded()) {
//            interstitial.show();
//        }
    }

    boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    // ダイアログ表示など特定の処理を行いたい場合はここに記述
                    // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    protected void onPostResume() {
        Log.i(TAG, "onPostResume()");
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();

    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }
}
