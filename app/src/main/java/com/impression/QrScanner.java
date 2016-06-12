package com.impression;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.impression.Utilities.CardModel;
import com.impression.Utilities.Constants;
import com.impression.Utilities.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QrScanner extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

    private TextView myTextView;
    private QRCodeReaderView mydecoderview;
    ArrayList<EditText> texts ;
    ImageView imageView ;
    LinearLayout cardHolder;
    CardModel model ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        cardHolder = (LinearLayout)findViewById(R.id.ll_card_container);
        texts = new ArrayList<>();
        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
    }


    public String getDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateTime = df.format(new Date());
        return dateTime;

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

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
          String [] data = text.split("#");
          model = new CardModel(data[4],Integer.parseInt(data[3]),data[1],data[0],data[2]);
          mydecoderview.setVisibility(View.GONE);
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
        String baseName = path.substring(path.lastIndexOf("/")+1);
        URL newurl = new URL(Constants.URL_BASE+"upload/"+baseName);
        Bitmap bmp = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
        if(imageView!=null) {
            imageView.setImageBitmap(bmp);
            imageView.setClickable(false);
        }
        saveInDb(bmp,path);
    }

    void saveInDb(Bitmap bmp ,String path) throws IOException {
        File file = new File(path);
        FileOutputStream fOut = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to model file compressed as model JPEG with 85% compression rate
        fOut.flush();
        fOut.close();
        SQLiteDatabase db = openOrCreateDatabase(DataBaseHelper.DB_NAME,MODE_PRIVATE,null);
        db.execSQL("INSERT INTO `cards` (email , xml , cardname , json , updatedAt) VALUES (?,?,?,?,?)",new Object[]{model.email,model.templateId,model.CardName,model.Json,getDateTime()});
    }



    // Called when your device have no camera
    @Override
    public void cameraNotFound() {

    }

    // Called when there's no QR codes in the camera preview image
    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }
}

