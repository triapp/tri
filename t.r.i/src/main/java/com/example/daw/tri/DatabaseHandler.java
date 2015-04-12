package com.example.daw.tri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EN on 11.4.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.example.daw.tri/databases/";

    private static String DB_NAME = "tri";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHandler(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

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
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does't exist yet.
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

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public void insertDay(int id, String date) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("day", date);
        myDataBase.insert("day", null, values);
        myDataBase.close();

    }

    public List<String> selectDay(){
        Cursor see = myDataBase.rawQuery("SELECT * FROM day ",null);
        List<String> listOfDays = new ArrayList<String>();
        see.moveToFirst();
        while (see.isAfterLast() == false) {
            Long id = see.getLong(0);
            String date = see.getString(1);
            String day = "ID of day: "+id+" is on: "+date;
            listOfDays.add(day);
            see.moveToNext();
        }
        see.close();
        return listOfDays;
    }

    public void insertHall(int id, String name){
        myDataBase.rawQuery("INSERT INTO hall (id,name) VALUES (" + id + ", '" + name + "')", null);
    }

    public void insertPresentation(int id, int id_section,String name, String author){
        myDataBase.rawQuery("INSERT INTO presentation (id, id_section, name, author) VALUES (" + id + ", " + id_section + ", '" + name + "', '" + author + "')", null);
    }

    public void insertSection(int id,int id_hall,int id_day,String name,String chairman, String time_from, String time_to, String type){
        myDataBase.rawQuery("INSERT INTO day (id,id_hall,id_day,name,chairman,time_from,time_to,type) VALUES (" + id + ", '" + id_hall + ", "+ id_day +",'" + name + "','" + chairman + "','"+time_from+"','"+time_to+"','" + type + "')", null);
    }
}
