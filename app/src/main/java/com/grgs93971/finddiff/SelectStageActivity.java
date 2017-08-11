package com.grgs93971.finddiff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class SelectStageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectstage);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(SelectStageActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                //ゲームメイン画面に遷移
                Intent intent = new Intent(getApplication(), GameMainActivity.class);
                startActivity(intent);
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView2);
        mAdView.loadAd(admobUtil.getAdRequest());
    }
}
