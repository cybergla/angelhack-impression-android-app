package com.impression;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.impression.Utilities.CardModel;
import com.impression.Utilities.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class NfcOrQr extends AppCompatActivity {
CardModel model;
    LinearLayout cardHolder ;
    ArrayList<EditText> texts ;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_or_qr);
        Intent intent = getIntent();
        cardHolder = (LinearLayout)findViewById(R.id.ll_card_container);
        model =(CardModel) intent.getSerializableExtra("card");
        texts = new ArrayList<>();
        int layoutID = getResources().getIdentifier(model.templateId, "layout",getPackageName());
        View root = LayoutInflater.from(this).inflate(layoutID,cardHolder,true);
        getViews((ViewGroup) root);
        applyJson(root,model.Json);
    }

    void applyJson(View card , String json)
    {
        try {
            JSONObject obj = new JSONObject(json);
            int color = obj.getInt("bgcolor");
            int fcolor = obj.getInt("fontcolor");
            JSONArray array = obj.getJSONArray("texts");
            card.findViewById(R.id.card_root_view).setBackgroundColor(color);
            for(int i = 0 ; i < texts.size() ; i++)
            {
                texts.get(i).setTextColor(fcolor);
                texts.get(i).setText(array.getString(i));
                texts.get(i).setEnabled(false);
            }
            applyImages(card,obj);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    void applyImages(View card , JSONObject obj) throws Exception {
        String path = obj.getString("image");
        Bitmap bmp = BitmapFactory.decodeFile(path);
        if(imageView!=null) {
            imageView.setImageBitmap(bmp);
            imageView.setClickable(false);
        }
    }

    void getViews(ViewGroup root)
    {
        for(int i=0;i<root.getChildCount();i++){
            View v=root.getChildAt(i);
            if(v instanceof EditText){
                texts.add((EditText) v);
            }
            else if(v instanceof ImageView)
            {
                imageView = (ImageView) v ;
            }
            else if(v instanceof ViewGroup){
                getViews((ViewGroup)v);
            }
        }
    }




public void gotoqr1(View v)
{Intent intent = new Intent(NfcOrQr.this,QrGenerator.class);
    intent.putExtra("card", model);
    startActivity(intent);
}

    public void gotonfc1(View v)
    {Intent intent = new Intent(NfcOrQr.this,NfcEx.class);
        intent.putExtra("card", model);
        startActivity(intent);
    }

    public void gotoqrs(View view)
    {
        Intent intent = new Intent(NfcOrQr.this,QrScanner.class);
        startActivity(intent);
    }


}
