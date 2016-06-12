package com.impression;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QrGenerator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);
        ImageView imageView = (ImageView) findViewById(R.id.qrimage);
        try {
            Bitmap bitmap = encodeAsBitmap("abc");
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();


        }}

        Bitmap encodeAsBitmap (String str)throws WriterException {
            BitMatrix result;
            try {
                result = new MultiFormatWriter().encode(str,
                        BarcodeFormat.QR_CODE, 200, 200, null);
            } catch (IllegalArgumentException iae) {
                // Unsupported format
                return null;
            }
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? Color.parseColor("#000000") : Color.parseColor("#ffffff");
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, 200, 0, 0, w, h);
            return bitmap;
        }
    }