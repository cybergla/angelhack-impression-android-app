package com.impression;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoDesign(View view)
    {
        Intent intent = new Intent(this,DesignActivity.class);
        startActivity(intent);
    }

    public void gotoCardChooser(View view)
    {
        Intent intent = new Intent(this,ChooseCard.class);
        startActivity(intent);
    }
}
