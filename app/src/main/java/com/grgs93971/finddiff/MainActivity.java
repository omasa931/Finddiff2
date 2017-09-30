package com.grgs93971.finddiff;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.google.android.gms.ads.AdView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int GMAIL_ID = 0;
    private final int LINE_ID = 1;
    private final int FACEBOOK_ID = 2;
    private final int TWITTER_ID = 3;
    private final String[] sharePackages = {"com.google.android.gm","jp.naver.line.android","com.facebook.katana","com.twitter.android"};
    String activityName = null;
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

        // Line 改行コードが入っていると不正なコードがはいっている旨のアラートがでて投稿できない
        findViewById(R.id.line_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShareAppInstall(LINE_ID)){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("line://msg/text/" + MsgUtil.getLinMsg()));
                    startActivity(intent);
                }else{
                    shareAppDl(LINE_ID);
                }
            }
        });

        /** facebook
         https://developers.facebook.com/policy/
         ポリシー上 Facebook公式アプリにインテントを飛ばす際は、アプリ側から投稿の文字等はアプリ側から設定出来ない
         以下のソースは、Facebook公式アプリに共有URLしたいURLを飛ばしている それならIntentから送れる*/
        findViewById(R.id.fb_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();
                Intent intent = new Intent( Intent.ACTION_SEND );
                intent.setType("text/plain");
                List<ResolveInfo> resolves = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                ActivityInfo activityinfo = null;
                for (ResolveInfo info: resolves) {
                    activityinfo = info.activityInfo;
                    if (activityinfo.packageName.equals(sharePackages[FACEBOOK_ID])) {
                        activityName = activityinfo.name;
                        break;
                    }
                }

                // Facebook公式アプリがインストールされてない
                if (activityName == null) {
                    shareAppDl(FACEBOOK_ID);

                } else {

                    // 呼び出す
                    ComponentName componentName = new ComponentName(sharePackages[FACEBOOK_ID], activityName);
                    intent.setComponent(componentName)
                            .putExtra(Intent.EXTRA_TEXT, MsgUtil.APP_URL);
                    startActivity(intent);
                }
            }
        });

        // Twitter
        findViewById(R.id.tw_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShareAppInstall(TWITTER_ID)){;

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setPackage(sharePackages[TWITTER_ID]);
                    intent.setType("image/png");
                    intent.putExtra(Intent.EXTRA_TEXT, MsgUtil.getTweeterMsg());
                    startActivity(intent);
                }else{
                    shareAppDl(TWITTER_ID);
                }
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

    // アプリがインストールされているかチェック
    private Boolean isShareAppInstall(int shareId){
        try {
            PackageManager pm = getPackageManager();
            pm.getApplicationInfo(sharePackages[shareId], PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    // アプリが無かったのでGooglePalyに飛ばす
    private void shareAppDl(int shareId){
        Uri uri = Uri.parse("market://details?id=" + sharePackages[shareId]);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
