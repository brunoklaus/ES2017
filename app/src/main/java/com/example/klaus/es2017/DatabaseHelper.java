package com.example.klaus.es2017;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

/**
 * Created by klaus on 30/08/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Receitas";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SURNAME";
    public static final String COL_4 = "MARKS";
    //The Android's default system path of your application database.
    private static String DB_PATH;
    private static String DB_NAME = "receitas.db";


    boolean FORCE_READ_DB_FROM_FILE = false; //if true, always reads DB from file
    private SQLiteDatabase myDataBase;
    private  Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH= myContext.getDatabasePath(DB_NAME).toString();
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist && !FORCE_READ_DB_FROM_FILE){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getWritableDatabase();

            try {

                copyDataBase();


            } catch (IOException e) {

                throw new Error("Error copying database");

            }

        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        //  this.getReadableDatabase();

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH ;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){
            //database doesn't exist yet.
        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();

        final StringBuilder out = new StringBuilder();
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH ;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName,false);

        //We will read line by line
        StringBuffer buf = new StringBuffer();
        String str = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(myInput));
            if (myInput != null) {
                while ((str = reader.readLine()) != null) {
                    db.execSQL(str);
                }
            }
        } finally {
            try { myInput.close(); } catch (Throwable ignore) {}
        }

        //Close the streams
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH ;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*
        sqLiteDatabase.execSQL("create table " + TABLE_NAME  +
                " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, SURNAME TEXT, MARKS INTEGER)");
                */
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean insertData(String name, String surname, String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, marks);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from  " + TABLE_NAME,null);
        return res;
    }

    public Cursor matchSearch(String searchQuery) {
        SQLiteDatabase db = this.getReadableDatabase();
        //TODO: Make this safe
        Cursor res = db.rawQuery("SELECT NAME FROM  " + TABLE_NAME + " WHERE NAME LIKE  '" +searchQuery +"%' ",null);

        return res;
    }

}
