package com.grgs93971.finddiff;


import com.google.android.gms.ads.AdRequest;

public class admobUtil {

    public static AdRequest getAdRequest() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("XXXXXsXXXXXY")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        return adRequest;
    }
}
