package com.impression;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_login);
        et1=(EditText)findViewById(R.id.et_login_email);
        et2=(EditText)findViewById(R.id.et_login_password);

    }


    public void checkLogin1(View v) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email_id", et1.getText().toString().toLowerCase());
        params.put("auth", et2.getText().toString().toLowerCase());


        client.post("http://shareb.azurewebsites.net/login.php", params, new AsyncHttpResponseHandler() {

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
                    Intent i5 = new Intent(LoginActivity.this, MainActivity.class);

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

    public void gotosignup(View v) {
        Intent i5 = new Intent(LoginActivity.this, SignUp.class);
        startActivity(i5);
    }
    public void gotoqr(View v) {
        Intent i5 = new Intent(LoginActivity.this, QrScanner.class);
        startActivity(i5);
    }
}
