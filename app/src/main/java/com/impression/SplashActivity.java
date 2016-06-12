package com.impression;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.impression.Utilities.Constants;
import com.impression.Utilities.DataBaseHelper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                Intent intent = new Intent(SplashActivity.this,DesignActivity.class);
            startActivity(intent);
            }
        },SPLASH_DISPLAY_LENGTH);
    }


    public String getDateTime() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS,MODE_PRIVATE);
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
