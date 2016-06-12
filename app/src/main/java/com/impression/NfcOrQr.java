package com.impression;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.impression.Utilities.CardModel;

public class NfcOrQr extends AppCompatActivity {
CardModel a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_or_qr);
        Intent intent = getIntent();
        a=intent.getParcelableExtra("card");
    }
public void gotoqr1(View v)
{Intent intent = new Intent(NfcOrQr.this,QrGenerator.class);
    intent.putExtra("card",a);
    startActivity(intent);
}

    public void gotonfc1(View v)
    {Intent intent = new Intent(NfcOrQr.this,NfcEx.class);
        intent.putExtra("card",a);
        startActivity(intent);
    }


}
