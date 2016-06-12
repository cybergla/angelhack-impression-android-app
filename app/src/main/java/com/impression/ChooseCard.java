package com.impression;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.impression.Utilities.*;
import com.impression.Utilities.DataBaseHelper;

import java.util.ArrayList;

public class ChooseCard extends AppCompatActivity {
RecyclerView templateList;
    SQLiteDatabase myDataBase=null;
    ArrayList<CardModel> cards ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);
        cards = new ArrayList<>();
        templateList = (RecyclerView)findViewById(R.id.rv_templates);
        templateList.setLayoutManager(new LinearLayoutManager(this));
        fetchFromDb();
        chooseCardAdapter adapter = new chooseCardAdapter(this,cards, new GenericDataListener<CardModel>() {
            @Override
            public void onData(CardModel data) {
                Intent intent = new Intent(ChooseCard.this,NfcOrQr.class);
                intent.putExtra("card",data);
                startActivity(intent);

            }
        });
        templateList.setAdapter(adapter);


    }

    void fetchFromDb()
    {
        SharedPreferences pref = getSharedPreferences(Constants.PREFS ,MODE_PRIVATE);
        String email = pref.getString("email",null);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DataBaseHelper.DB_NAME,null);
        Cursor c = db.rawQuery("Select * from cards where email = ?",new String[]{email});
        c.moveToFirst();
        while (!c.isAfterLast()) {
            cards.add(new CardModel(c.getString(1),c.getInt(0), c.getString(2), c.getString(3), c.getString(4)));
        }

    }
}
