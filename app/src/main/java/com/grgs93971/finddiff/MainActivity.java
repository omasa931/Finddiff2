package com.grgs93971.finddiff;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imageButton = (ImageButton)findViewById(R.id.start_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SelectStageActivity.class);
                startActivity(intent);
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(admobUtil.getAdRequest());

        DisplaySizeCheck ds = new DisplaySizeCheck();
        Point p1 = ds.getDisplaySize(this);
        Log.d("P1", p1.toString());

        Point p2 =  ds.getRealSize(this);
        Log.d("P2", p2.toString());

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        RelativeLayout r = (RelativeLayout)findViewById(R.id.relativeLayout);
//        Point view = DisplaySizeCheck.getViewSize(r);
//
//        StringBuilder text = new StringBuilder();
//        text.append("ViewSize → X:" + view.x + " Y:" + view.y);
//        text.append("\nDisplaySize → X:" + disp.x + " Y:" + disp.y);
//        text.append("\nHardwareSize → X:" + real.x + " Y:" + real.y);
//
//        dipSize.setText(text.toString());
    }
}
