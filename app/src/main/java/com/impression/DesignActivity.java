package com.impression;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.impression.Utilities.BitmapLoader;
import com.impression.Utilities.Constants;
import com.impression.Utilities.DataBaseHelper;
import com.impression.Utilities.GenericDataListener;
import com.impression.Utilities.templateAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class DesignActivity extends AppCompatActivity {

    int [] templates = {R.layout.template_1,R.layout.template_2,R.layout.template_3};
    int panelHeight;

    FrameLayout main ;
    LinearLayout cardContainer,listPanel;
    RecyclerView templateList;
    int currentRequestingImage=-1;
    ArrayList<EditText> texts ;
    Bitmap image ;
    int chosenCardLayout = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);
        main = (FrameLayout) findViewById(R.id.fl_main);
        cardContainer = (LinearLayout)findViewById(R.id.card_holder);

        listPanel = (LinearLayout)findViewById(R.id.ll_list);
        listPanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                panelHeight = listPanel.getHeight();
                listPanel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        texts = new ArrayList<>();
    }
    @Override
    public void onBackPressed()
    {
        if(chosenCardLayout!=-1);

    }

    public void setCardForEdit(int id)
    {
        Log.d("jl","kh");
        image = null;
        currentRequestingImage = -1;
       View root = LayoutInflater.from(this).inflate(id,cardContainer,true);
        getViews((ViewGroup) root);
        Interpolator intp = new android.view.animation.OvershootInterpolator();
        listPanel.animate().setDuration(800).setInterpolator(intp).translationY(panelHeight).start();
        Log.d("texts",String.valueOf(texts.size()));
    }

    void getViews(ViewGroup root)
    {
        for(int i=0;i<root.getChildCount();i++){
            View v=root.getChildAt(i);
            if(v instanceof EditText){
                texts.add((EditText) v);
            }
            else if(v instanceof ViewGroup){
                getViews((ViewGroup)v);
            }
        }
    }

    public void imgClick(View view)
    {
        Intent photoIntent = new Intent();
        photoIntent.setType("image/*");
        photoIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(photoIntent,"Select Picture"),23);
        currentRequestingImage = view.getId();

    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode,resultCode,data);
        Log.d("result",String.valueOf(resultCode));
        if(resultCode == RESULT_OK)
        {
            Uri selectedMediaUri = data.getData();
            Log.d("URI",selectedMediaUri.toString());
            new BitmapLoader(this, new GenericDataListener<Bitmap>() {
                @Override
                public void onData(Bitmap data) {
                    ((ImageView)findViewById(currentRequestingImage)).setImageBitmap(data);
                    image =data ;

                }
            }).execute(selectedMediaUri);
        }

    }

    public void bgColorClick(View view)
    {

      View crv = findViewById(R.id.card_root_view);
        ColorDrawable bg = (ColorDrawable)view.getBackground();
        int col = bg.getColor();
      crv.setBackgroundColor(col);
    }

    public void fontColorClick(View view)
    {
        View crv = findViewById(R.id.card_root_view);
        ColorDrawable bg = (ColorDrawable)view.getBackground();
        int col = bg.getColor();
        for(EditText et : texts)
        {
            et.setTextColor(col);
        }
    }

    public String getDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateTime = df.format(new Date());
            return dateTime;
    }


    public void saveCard(View view)  {
        File file = new File(getFilesDir()+"/me" + String.valueOf(System.nanoTime()));
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush();
            fOut.close();
            JSONObject object = new JSONObject();
            ArrayList<String> strings = new ArrayList<>();
            for(EditText et : texts)
            {
                strings.add(et.getText().toString());
            }

            object.put("texts",new JSONArray(strings));
            object.put("image",file.getAbsolutePath());
            String json = object.toString();
            String xml = getResources().getResourceEntryName(chosenCardLayout);
            String dt = getDateTime();
            SQLiteDatabase db = openOrCreateDatabase(DataBaseHelper.DB_NAME,MODE_PRIVATE,null);
            db.execSQL("INSERT INTO `cards` (email , xml , json , updatedAt) VALUES (?,?,?,?)",new Object[]{"name",xml,json,dt});
            db.close();
            sendToBackend(xml,json,file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void sendToBackend(String xml , String json ,File file) throws Exception {
        byte[] b = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
        RequestParams params = new RequestParams();
        params.put("image",new ByteArrayInputStream(b),file.getAbsolutePath());
        params.put("email","mee");
        params.put("xml",xml);
        params.put("json",json);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.URL_BASE+"createcard.php", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Snackbar.make(main,"Posted to net",Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Snackbar.make(main,"fail",Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }


}
