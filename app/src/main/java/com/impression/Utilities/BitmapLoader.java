package com.impression.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Pulkit Juneja on 12-Jun-16.
 */
public class BitmapLoader extends AsyncTask<Uri,Void,Bitmap> {

    GenericDataListener<Bitmap> listener;
    Context context;

    public BitmapLoader(Context context , GenericDataListener<Bitmap> ls)
    {
        this.context=context;
        listener=ls;
    }

    @Override
    protected Bitmap doInBackground(Uri... params) {
        Bitmap bitmap = null ;
        try {
             bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        if(listener!=null){
            listener.onData(image);
        }
        Log.d("Bitmap","Done loading");
    }
}