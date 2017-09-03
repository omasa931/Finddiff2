package com.grgs93971.finddiff;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataBaseHelper extends SQLiteOpenHelper {
    /** ログ用*/
    private final String TAG = "DataBaseHelper:";
    /* データベース名 */
    private final static String DB_NAME = "finddiff.sql";
    /* データベースのバージョン */
    private final static int DB_VER = 1;
    /* コンテキスト */
    private Context mContext;

    /**
     * コンストラクタ
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
        mContext = context;
    }

    /**
     * データベースが作成された時に呼ばれます。
     * assets/sql/create内に定義されているsqlを実行します。
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate(SQLiteDatabase db)");

        try {
            execSql(db,"sql/create");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * データベースをバージョンアップした時に呼ばれます。
     * assets/sql/drop内に定義されているsqlを実行します。
     * その後onCreate()メソッドを呼び出します。
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)");
        try {
            execSql(db,"sql/drop");
        } catch (IOException e) {
            e.printStackTrace();
        }
        onCreate(db);
    }

    /**
     * 引数に指定したassetsフォルダ内のsqlを実行します。
     * @param db データベース
     * @param assetsDir assetsフォルダ内のフォルダのパス
     * @throws IOException
     */
    private void execSql(SQLiteDatabase db,String assetsDir) throws IOException {
        Log.i(TAG, "execSql(SQLiteDatabase db,String assetsDir) ");
        AssetManager as = mContext.getResources().getAssets();
        try {
            String files[] = as.list(assetsDir);
            for (int i = 0; i < files.length; i++) {
                String str = readFile(as.open(assetsDir + "/" + files[i]));
                for (String sql: str.split("\n")){
                    Log.i(TAG, sql);
                    db.execSQL(sql);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ファイルから文字列を読み込みます。
     * @param is
     * @return ファイルの文字列
     * @throws IOException
     */
    private String readFile(InputStream is) throws IOException{
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is,"SJIS"));

            StringBuilder sb = new StringBuilder();
            String str;
            while((str = br.readLine()) != null){
                sb.append(str +"\n");
            }
            return sb.toString();
        } finally {
            if (br != null) br.close();
        }
    }
}