package com.impression;

import android.content.Intent;
import android.os.Bundle;
import com.loopj.android.http.*;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

import android.content.Context;
import android.content.SharedPreferences;

public class SignUp extends AppCompatActivity {

    EditText et1,et2,et3;
    public static void setPrefString(String nameOfPreference, String valueOfPreference, Context c) {
        // BT_debugger.showIt(objectName + ":setPrefString setting \"" +
        // nameOfPreference + "\" to \"" +
        // valueOfPreference + "\" in the devices settings");
        try {

            SharedPreferences BT_prefs = c.getSharedPreferences("MyPref", 0); // 0
            // -
            // for
            // private
            // mode
            SharedPreferences.Editor prefsEditor = BT_prefs.edit();
            prefsEditor.putString(nameOfPreference, valueOfPreference);
            prefsEditor.commit();

        } catch (Exception e) {
            //    Log.e(TAG, "setPrefString EXCEPTION " + e.toString());
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        et1=(EditText)findViewById(R.id.et_reg_email);
        et2=(EditText)findViewById(R.id.et_reg_password);
        et3=(EditText)findViewById(R.id.et_reg_conf_password);
    }


    public void checkLogin(View v) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email_id", et1.getText().toString().toLowerCase());
        params.put("name", et2.getText().toString().toLowerCase());
        params.put("auth", et3.getText().toString());

        client.post("http://shareb.azurewebsites.net/signup.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                // called when response HTTP status is "200 OK"
                String s1 = new String(responseBody);
                //Toast.makeText(getApplicationContext(), "connected" + s1, Toast.LENGTH_LONG).show();
                if (s1.equals("OK")) {
                    Intent i5 = new Intent(SignUp.this, MainActivity.class);

                    setPrefString("loginstatus", "loggedin", getApplicationContext());
                    //   setPrefString("name","loggedin",getApplicationContext());
                    setPrefString("email", et1.getText().toString().toLowerCase(), getApplicationContext());
                    startActivity(i5);
                } else
                    Toast.makeText(getApplicationContext(), "Incorrect id or password", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}
