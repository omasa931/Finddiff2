package com.grgs93971.finddiff;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MsgUtil {
    /** アプリURL*/
    public static String APP_URL = "https://play.google.com/store/apps/details?id=com.grgs93971.finddiff2";

    public static String getLinMsg() {
        String msg = "間違い探しゲーム!\n" + MsgUtil.APP_URL;
        try {
            msg = URLEncoder.encode(msg, "utf-8");

        } catch (UnsupportedEncodingException e) {
            msg = MsgUtil.APP_URL;
        }
        return msg;
    }

    public static String getTweeterMsg() {
        String msg = MsgUtil.APP_URL
                + "\n#間違い探しゲーム"
                + "\n#Find the Difference"
                + "\n#grgs93971";
        return msg;
    }
}
