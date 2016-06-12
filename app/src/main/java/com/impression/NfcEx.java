package com.impression;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.impression.Utilities.CardModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.nio.charset.Charset;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class NfcEx extends AppCompatActivity implements NfcAdapter.OnNdefPushCompleteCallback,
        NfcAdapter.CreateNdefMessageCallback {
    private NfcAdapter mNfcAdapter;

    public NfcEx(){

    }


    public static String getPrefString(String nameOfPreference,Context c) {
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


    public NfcEx(NfcAdapter mNfcAdapter) {
        this.mNfcAdapter = mNfcAdapter;
    }

    protected void onPostResume() {
        super.onPostResume();
    }

    private ArrayList<String> messagesToSendArray = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        handleNfcIntent(getIntent());

    }

    private ArrayList<String> messagesReceivedArray = new ArrayList<>();

    public void snedwithNFC(View view) {
        nfcCheck();


//        txtBoxAddMessage.setText(null);
//        updateTextViews();

        //  Toast.makeText(this, "Added Message", Toast.LENGTH_LONG).show();
        //  createRecords();
    }


    public void nfcCheck() {
        //Check if NFC is available on device
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter != null) {
            //This will refer back to createNdefMessage for what it will send
            mNfcAdapter.setNdefPushMessageCallback(this, this);

            //This will be called if the message is sent successfully
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        } else {
            Toast.makeText(this, "NFC not available on this device",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public NdefRecord[] createRecords() {
        NdefRecord[] records = new NdefRecord[messagesToSendArray.size() + 1];
        //To Create Messages Manually if API is less than
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            for (int i = 0; i < messagesToSendArray.size(); i++) {
                byte[] payload = messagesToSendArray.get(i).
                        getBytes(Charset.forName("UTF-8"));
                NdefRecord record = new NdefRecord(
                        NdefRecord.TNF_WELL_KNOWN,      //Our 3-bit Type name format
                        NdefRecord.RTD_TEXT,            //Description of our payload
                        new byte[0],                    //The optional id for our Record
                        payload);                       //Our payload for the Record

                records[i] = record;
            }
        }
        //Api is high enough that we can use createMime, which is preferred.
        else {
            for (int i = 0; i < messagesToSendArray.size(); i++) {
                byte[] payload = messagesToSendArray.get(i).
                        getBytes(Charset.forName("UTF-8"));

                NdefRecord record = NdefRecord.createMime("text/plain", payload);
                records[i] = record;
            }
        }
        records[messagesToSendArray.size()] =
                NdefRecord.createApplicationRecord(getPackageName());
        return records;
    }

    private void handleNfcIntent(Intent NfcIntent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(NfcIntent.getAction())) {
            Parcelable[] receivedArray =
                    NfcIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (receivedArray != null) {
                messagesReceivedArray.clear();
                NdefMessage receivedMessage = (NdefMessage) receivedArray[0];
                NdefRecord[] attachedRecords = receivedMessage.getRecords();

                for (NdefRecord record : attachedRecords) {
                    String string = new String(record.getPayload());
                    //Make sure we don't pass along our AAR (Android Application Record)
                    if (string.equals(getPackageName())) {
                        continue;
                    }
                    messagesReceivedArray.add(string);
                    showReceivedData(string);
                }
                Toast.makeText(this, "Received"+ //+ messagesReceivedArray.size() +
                         "Messages", Toast.LENGTH_LONG).show();
                //updateTextViews();
            } else {
                Toast.makeText(this, "Received Blank Parcel", Toast.LENGTH_LONG).show();
            }
        }
    }


    void showReceivedData(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (!isFinishing()) {
                    new AlertDialog.Builder(NfcEx.this)
                            .setTitle("Contact Received")
                            .setMessage("")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).create().show();
                    ;

/////idhar chutiyaap message mein ek string hai

                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    //String email=getPrefString("email",getApplicationContext());

                    String eid=getPrefString("email",getApplicationContext());
                    params.put("uid1", eid.toString());
                    params.put("uid2", message.toString());
                    client.post("http://shareb.azurewebsites.net/addcontact.php", params, new AsyncHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            // called before request is started

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                            // called when response HTTP status is "200 OK"
                            String s1 = new String(response);
                            Toast.makeText(getApplicationContext(), "connected" + s1, Toast.LENGTH_LONG).show();
                            if (s1.equals("OK")) {
                                Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_LONG).show();

                            } else
                                Toast.makeText(getApplicationContext(), "Contact could not be added/ already exists", Toast.LENGTH_LONG).show();
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
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        handleNfcIntent(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_ex);
        Intent intent = getIntent();
        CardModel a=(CardModel) intent.getSerializableExtra("card");

        messagesToSendArray.add(a.CardName);
        messagesToSendArray.add(a.templateId);
        messagesToSendArray.add(a.Json);
        messagesToSendArray.add(a.id+"");
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return null;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {

    }
}
