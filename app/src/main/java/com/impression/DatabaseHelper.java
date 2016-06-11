package com.impression;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Pulkit Juneja on 11-Jun-16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static String DB_PATH;
    public static String DB_NAME = "impression";
    private SQLiteDatabase myDataBase;
    private final Context context;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        Log.d("path", DB_PATH + DB_NAME);
    }

    public void createDataBase() {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            Log.d("exists", "true");
        }
        else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkDataBase() {
        boolean checkdb = false;
        try {
            String myPath = context.getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return checkdb;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = context.getAssets().open("articleSQL");
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        Log.d("dbcopied", "copied");
    }

    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
