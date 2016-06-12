package com.impression;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends AppCompatActivity {

    public static String getPrefString(String nameOfPreference, Context c) {
        // BT_debugger.showIt(objectName + ":getPrefString getting value of \""
        // + nameOfPreference +
        // "\" from the devices settings");
        String ret = "";
        try {
            if (nameOfPreference.length() > 1) {
                ret = "get the value here...";

                SharedPreferences BT_prefs = c.getSharedPreferences(
                        "MyPref", 0); // 0 - for private mode
                ret = BT_prefs.getString(nameOfPreference, "");

                // BT_debugger.showIt(objectName + ":getPrefString value is: \""
                // + ret + "\"");

            }
        } catch (Exception e) {
            //Log.e(TAG, "getPrefString EXCEPTION " + e.toString());
        }
        return ret;
    }

    final int SPLASH_DISPLAY_LENGTH = 1000;
    final String PREFS = "impressions_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            DataBaseHelper helper = new DataBaseHelper(SplashActivity.this);
                try {
                    helper.createDataBase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //String status=getPrefString("loginstatus",getApplicationContext());
                Intent intent = new Intent(SplashActivity.this,QrGenerator.class);
                Intent intent1 = new Intent(SplashActivity.this,MainActivity.class);
                String status=getPrefString("loginstatus",getApplicationContext());
                if(status.equals("loggedin"))
                    startActivity(intent1);
                else
                    startActivity(intent);
            }
        },SPLASH_DISPLAY_LENGTH);
    }




    public String getDateTime() {
        SharedPreferences prefs = getSharedPreferences(PREFS,MODE_PRIVATE);
        String date = prefs.getString("last_sync", null);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateTime = df.format(new Date());
        prefs.edit().putString("last_sync", dateTime);
        if (date != null)
            return date;
        else
            return dateTime;

    }
}
