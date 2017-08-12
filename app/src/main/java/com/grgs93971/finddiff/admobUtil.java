package com.grgs93971.finddiff;


import com.google.android.gms.ads.AdRequest;

public class admobUtil {

    public static AdRequest getAdRequest() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("120DF49DD252878FA8989C714705A78A")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        return adRequest;
    }
}
