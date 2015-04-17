package com.example.daw.tri.Library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.daw.tri.Objects.Day;
import com.example.daw.tri.Objects.Presentation;
import com.example.daw.tri.Objects.Section;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

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
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
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


    public void dropAll(){
        myDataBase.execSQL("delete from day");
        myDataBase.execSQL("delete from section");
        myDataBase.execSQL("delete from presentation");
        myDataBase.execSQL("delete from hall");

    }


    public void insertDay(int id, String date) {
        Log.i("daw", date);
        ContentValues values = new ContentValues();
        try {
            this.openDataBase();
            values.put("id", id);
            values.put("day", date);
            myDataBase.insert("day", null, values);
            myDataBase.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Day> selectDay() throws SQLException, ParseException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT * FROM day ",null);
        ArrayList<Day> listOfDays = new ArrayList<Day>();
        Day tmp;
        see.moveToFirst();
        while (!see.isAfterLast()) {
            tmp = new Day(see.getLong(0), see.getString(1));
            listOfDays.add(tmp);
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return listOfDays;
    }

    public ArrayList<Section> selectSectionByDay(Long id) throws SQLException, ParseException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT * FROM section WHERE id_day="+id,null);
        ArrayList<Section> listOfSections = new ArrayList<Section>();
        Section tmp;
        see.moveToFirst();
        while (!see.isAfterLast()) {
            tmp = new Section(see.getLong(0), see.getLong(1),see.getLong(2),see.getString(3));
            listOfSections.add(tmp);
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return listOfSections;
    }

    public ArrayList<Presentation> selectPresentationById(Long id) throws SQLException, ParseException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT * FROM presentation WHERE id_section="+id,null);
        ArrayList<Presentation> listOfPresentations = new ArrayList<>();
        Presentation tmp;
        see.moveToFirst();
        while (!see.isAfterLast()) {
            tmp = new Presentation(see.getLong(0), see.getString(2),see.getString(3));
            listOfPresentations.add(tmp);
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return listOfPresentations;
    }

    public void insertHall(int id, String name){
        ContentValues values = new ContentValues();
        Log.i("hall test", name);
        try {
            this.openDataBase();
            values.put("id", id);
            values.put("name", name);
            myDataBase.insert("hall", null, values);
            myDataBase.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPresentation(int id, int id_section,String name, String author){
        ContentValues values = new ContentValues();
        try {
            this.openDataBase();
            values.put("id", id);
            values.put("id_section", id_section);
            values.put("name", name);
            values.put("author", author);
            myDataBase.insert("presentation", null, values);
            myDataBase.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertSection(int id,int id_hall,int id_day,String name,String chairman, String time_from, String time_to, String type){
        ContentValues values = new ContentValues();
        try {
            this.openDataBase();
            values.put("id", id);
            values.put("id_hall", id_hall);
            values.put("id_day", id_day);
            values.put("name", name);
            values.put("chairman", chairman);
            values.put("time_from", time_from);
            values.put("time_to", time_to);
            values.put("type", type);
            myDataBase.insert("section", null, values);
            myDataBase.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
