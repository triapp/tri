package com.orl.chigger.tri.Library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.orl.chigger.tri.Objects.Day;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by EN on 11.4.2015.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.orl.chigger.tri/databases/";

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

            } catch (SQLException e) {
                e.printStackTrace();
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
    private void copyDataBase() throws IOException, SQLException {

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

    public void dropAll() throws SQLException {
        openDataBase();
        myDataBase.execSQL("delete from day");
        myDataBase.execSQL("delete from day_hall");
        myDataBase.execSQL("delete from section");
        myDataBase.execSQL("delete from presentation");
        myDataBase.execSQL("delete from hall");
        myDataBase.close();
    }

    public void insertDay(int id, String date) {
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

    public void insertHallDay(int id_day, int id_hall) throws SQLException {
        ContentValues values = new ContentValues();
        openDataBase();
        values.put("id_day", id_day);
        values.put("id_hall",id_hall);
        myDataBase.insert("day_hall",null,values);
        myDataBase.close();
    }

    public ArrayList<Day> getDays() throws SQLException, ParseException {
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

    public Long getSectionIdByPresentationId(Long id) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT section.id FROM section, presentation WHERE presentation.id_section=section.id AND presentation.id="+id+" LIMIT 1",null);
        see.moveToFirst();
        Long result = see.getLong(0);
        see.close();
        myDataBase.close();
        return result;
    }

    public List<String> getHallListbyDay(Long idDay) throws SQLException {
        List<String> listOfHalls = new ArrayList<>();
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT hall.name FROM hall,day_hall WHERE hall.id = day_hall.id_hall AND day_hall.id_day="+idDay,null);
        see.moveToFirst();
        while(!see.isAfterLast()){
            listOfHalls.add(see.getString(0));
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return listOfHalls;
    }

    public List<String> getSpeakers() throws SQLException {
        List<String> listOfSpeakers = new ArrayList<>();
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT author FROM presentation GROUP BY author",null);
        see.moveToFirst();
        while(!see.isAfterLast()){
            listOfSpeakers.add(see.getString(0));
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return listOfSpeakers;
    }

    public List<String> getSpeakersSearch(String query) throws SQLException {
        List<String> listOfSpeakers = new ArrayList<>();
        openDataBase();

       Cursor see = myDataBase.rawQuery("SELECT author FROM presentation WHERE author LIKE '%"+query+"%' GROUP BY author",null);
        //Cursor see = myDataBase.rawQuery("SELECT author FROM presentation WHERE author = '%"+query+"%' GROUP BY author",null);
        if (query.equals(" Search") || query.equals(" ")  ){
            see = myDataBase.rawQuery("SELECT author FROM presentation GROUP BY author",null);
        }
        see.moveToFirst();
        while(!see.isAfterLast()){
            listOfSpeakers.add(see.getString(0));
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return listOfSpeakers;
    }

    public List<String> getPresentationBySpeaker(String query) throws SQLException {
        List<String> listOfPresentations = new ArrayList<>();
        openDataBase();
       // Cursor see = myDataBase.rawQuery("SELECT name FROM presentation WHERE author LIKE '%"+query+"%'",null);

        Cursor see = myDataBase.rawQuery("SELECT name FROM presentation WHERE author = '"+query+"'",null);
        see.moveToFirst();
        while(!see.isAfterLast()){
            listOfPresentations.add(see.getString(0));
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return listOfPresentations;
    }

    public Long getSectionIdByPresentationPosition(String author, int position) throws SQLException {
        Long result = null;
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT id_section FROM presentation WHERE author LIKE '%"+author+"%' LIMIT "+position+",1",null);
        see.moveToFirst();
        while(!see.isAfterLast()){
            result = see.getLong(0);
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return result;
    }

    public Long getPresentationIdByPresentationPosition(String author, int position) throws SQLException {
        Long result = null;
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT id FROM presentation WHERE author LIKE '%"+author+"%' LIMIT "+position+",1",null);
        see.moveToFirst();
        while(!see.isAfterLast()){
            result = see.getLong(0);
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return result;
    }

    public Long[] getArrayIdHall(Long idDay) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT id_hall FROM day_hall WHERE id_day="+idDay,null);
        see.moveToFirst();

        Long[] result = new Long[see.getCount()];
        while(!see.isAfterLast()){
            result[see.getPosition()] = see.getLong(0);
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return result;
    }

    public void insertPersonalSection(Long idSection) throws SQLException, ParseException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT day,time_to, time_from,section.name, id_hall FROM section,day WHERE section.id="+idSection+" AND section.id_day=day.id",null);
        see.moveToFirst();
        ContentValues values = new ContentValues();
        values.put("id", idSection);
        values.put("name", see.getString(3));
        String time_to = see.getString(0)+" "+ see.getString(1);
        String time_from = see.getString(0)+" "+ see.getString(2);
        values.put("time_to",time_to);
        values.put("time_from",time_from);
        values.put("id_hall", see.getString(4));
        see.close();
        myDataBase.insert("personal", null, values);
        myDataBase.close();
    }

    public Long getNthSectionInPersonal(int position) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT id FROM personal ORDER BY time_from LIMIT "+position+",1",null);
        see.moveToFirst();
        Long result = see.getLong(0);
        see.close();
        myDataBase.close();
        return result;
    }

    public List<String> getPersonalList() throws SQLException, ParseException {
        List<String> result = new ArrayList<>();
        openDataBase();
        Cursor see,pointer;
        see = myDataBase.rawQuery("SELECT id FROM personal ORDER BY time_from", null);
        see.moveToFirst();
        if  (see.getCount() == 0){
            result.add("Your personal program is empty.");
        }
        while (!see.isAfterLast()){
            pointer = myDataBase.rawQuery("SELECT name FROM section WHERE id="+see.getLong(0),null);
            pointer.moveToFirst();
            result.add(pointer.getString(0));
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return result;
    }

    public boolean isSectionInPersonal(Long idSection) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT id FROM personal WHERE id="+idSection,null);
        see.moveToFirst();
        myDataBase.close();
        Boolean result = see.getCount() > 0;
        return result;
    }

    public void removeSectionFromPersonal(Long id) throws SQLException {
        openDataBase();
        myDataBase.execSQL("DELETE FROM personal WHERE id=" + id);
        myDataBase.close();
    }

    public boolean doesSectionExist(Long id) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT id FROM section WHERE id="+id+" LIMIT 1",null);
        boolean result = see.getCount() > 0;
        see.close();
        myDataBase.close();
        return result;
    }

    public String renewPersonal() throws SQLException, ParseException {
        String result = "";
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT id,name FROM personal",null);
        see.moveToFirst();
        while(!see.isAfterLast()){
            if (doesSectionExist(see.getLong(0))){
            } else {
                removeSectionFromPersonal(see.getLong(0));
                result +=see.getString(1) + " was removed from program.";
            }
            see.moveToNext();
        }
        openDataBase();
        see = myDataBase.rawQuery("SELECT id,id_hall, name FROM personal",null);
        see.moveToFirst();
        while (!see.isAfterLast()){
            Long currentId = see.getLong(0);
            Cursor see1 = myDataBase.rawQuery("SELECT id_hall, hall.name FROM section, hall WHERE section.id="+currentId+"",null);
            see1.moveToFirst();
            if (see1.getLong(0) != see.getLong(1)){
                result += see.getString(2) + " was moved to "+ see1.getString(1)+".";
                updateHallInPersonal(currentId, see1.getLong(0));
            }
            see1.close();
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return result;
    }

    public String getSectionChairmanBySectionId(Long id) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT chairman FROM section WHERE id="+id+" LIMIT 1",null);
        see.moveToFirst();
        String result = see.getString(0);
        see.close();
        myDataBase.close();
        return result;
    }

    public String getPresentationSpeakerByPresentationId(Long id) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT author FROM presentation WHERE id="+id+" LIMIT 1",null);
        see.moveToFirst();
        String result = see.getString(0);
        see.close();
        myDataBase.close();
        return result;
    }

    public void updateHallInPersonal(Long id, Long newHall){
        ContentValues values = new ContentValues();
        values.put("id_hall",newHall);
        myDataBase.update("personal", values, "id=" + id, null);
    }

    private Long getPersonalNthSection (int position) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT id FROM personal ORDER by time_from LIMIT "+position+",1",null);
        see.moveToFirst();
        Long result = see.getLong(0);
        see.close();
        myDataBase.close();
        return result;
    }

    public boolean doesHavePersonalSectionPresentations(Long section) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT presentation.id FROM personal_presentation, presentation WHERE presentation.id_section="+section+" AND personal_presentation.id=presentation.id LIMIT 1", null);
        boolean result = see.getCount() > 0;
        see.close();
        myDataBase.close();
        return result;
    }

    public String checkPersonalForCollisions() throws SQLException, ParseException {
        openDataBase();
        String result = "";
        Long section1;
        String section1Name;
        Cursor see = myDataBase.rawQuery("SELECT personal.id, personal.name FROM personal ORDER BY personal.time_from",null);
        see.moveToFirst();
        int total = see.getCount();
        if(total > 0){
            while(!see.isAfterLast()){
                section1Name = see.getString(1);
                section1 = see.getLong(0);
                for (int i=see.getPosition()+1;i < total;i++){
                    Log.i("Comparing: ", Long.toString(section1)+", "+Long.toString(getPersonalNthSection(i))+":"+Boolean.toString(isSectionCollision(section1,getPersonalNthSection(i))));
                    if (isSectionCollision(section1,getPersonalNthSection(i))){
                        openDataBase();
                        Cursor pointer = myDataBase.rawQuery("SELECT name FROM personal WHERE id="+getPersonalNthSection(i),null);
                        pointer.moveToFirst();
                        result += "Collision between (" +section1Name+") and ("+pointer.getString(0)+").\n\n";
                        pointer.close();
                    };
                }
                see.moveToNext();
            }
        }
        see.close();
        myDataBase.close();
        return result;
    }

    public void insertPersonalPresentation(Long idPresentaiton) throws SQLException {
        openDataBase();
        ContentValues values = new ContentValues();
        values.put("id", idPresentaiton);
        myDataBase.insert("personal_presentation", null, values);
        myDataBase.close();
    }

    public void removePresentationFromPersonal(Long idPresentation) throws SQLException {
        openDataBase();
        myDataBase.execSQL("DELETE FROM personal_presentation WHERE id=" + idPresentation);
        myDataBase.close();
    }

    public boolean isPresentationInPersonal(Long idPresentation) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT id FROM personal_presentation WHERE id="+idPresentation,null);
        see.moveToFirst();
        myDataBase.close();
        Boolean result = see.getCount() > 0;
        see.close();
        return result;
    }

    public Long getNthPresentation(Long idSection, int presentationPosition) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT presentation.id FROM presentation, section WHERE presentation.id_section=section.id AND section.id="+idSection+" LIMIT "+presentationPosition+",1",null);
        see.moveToFirst();
        myDataBase.close();
        Long result = see.getLong(0);
        see.close();
        return result;
    }

    public Long getNthPresentationFromPersonal(Long idSection, int presentationPosition) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT presentation.id FROM presentation, personal,personal_presentation WHERE presentation.id_section=personal.id AND presentation.id=personal_presentation.id AND personal.id="+idSection+" LIMIT "+presentationPosition+",1",null);
        see.moveToFirst();
        myDataBase.close();
        Long result = see.getLong(0);
        see.close();
        return result;
    }

    public boolean isSectionCollision(Long section1, Long section2) throws SQLException, ParseException {
        openDataBase();
        Date section1To,section2From;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Cursor see = myDataBase.rawQuery("SELECT time_from,time_to FROM personal WHERE id="+section1+" OR id="+section2+" ORDER BY time_from LIMIT 2", null);
        see.moveToFirst();
        section1To = dateFormat.parse(see.getString(1));
        see.moveToNext();
        section2From = dateFormat.parse(see.getString(0));
        see.close();
        myDataBase.close();
        if (section2From.equals(section1To)){
            return false;
        }
        return section2From.before(section1To);
    }

    public List<String> getSectionList(Long day, Long hall) throws SQLException {
        openDataBase();
        List<String> result = new ArrayList<>();
        Cursor see = myDataBase.rawQuery("SELECT name FROM section WHERE id_day="+day+" AND id_hall="+hall,null);
        see.moveToFirst();
        while(!see.isAfterLast()){
            result.add(see.getString(0));
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return result;
    }

    public String getSectionTime(Long idSection) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT time_from,time_to FROM section WHERE id="+idSection,null);
        see.moveToFirst();
        String time_from = see.getString(0);
        time_from = time_from.substring(0,time_from.length()-3);
        String time_to = see.getString(1);
        time_to = time_to.substring(0,time_to.length()-3);
        see.close();
        myDataBase.close();
        return time_from +" - "+ time_to;
    }

    public String getSectionHall(Long idSection) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT hall.name FROM section,hall WHERE section.id="+idSection+" AND section.id_hall=hall.id",null);
        see.moveToFirst();
        String hall = see.getString(0);
        see.close();
        myDataBase.close();
        return hall;
    }

    public HashMap<String,List<String>> getSectionPresentationMap(Long day, Long hall) throws SQLException {
        openDataBase();
        HashMap<String,List<String>> result = new HashMap<>();
        Cursor pointer;
        Cursor see  = myDataBase.rawQuery("SELECT id,name FROM section WHERE id_day="+day+" AND id_hall="+hall,null);
        see.moveToFirst();
        while(!see.isAfterLast()){
            List<String> presentations = new ArrayList<>();
            pointer = myDataBase.rawQuery("SELECT name FROM presentation WHERE id_section="+see.getLong(0),null);
            pointer.moveToFirst();
            while (!pointer.isAfterLast()){
                presentations.add(pointer.getPosition()+1+". "+pointer.getString(0));
                pointer.moveToNext();
            }
            result.put(see.getString(1), presentations);
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return result;
    }

    public HashMap<String,List<String>> getPersonalPresentationMap() throws SQLException, ParseException {
        openDataBase();
        HashMap<String,List<String>> result = new HashMap<>();
        Cursor pointer;
        Cursor see  = myDataBase.rawQuery("SELECT personal.id,personal.name, personal.time_from,personal.time_to FROM personal ORDER BY time_from",null);
        see.moveToFirst();
        if (see.getCount() == 0){
            result.put("Your personal program is empty.",new ArrayList<String>());
        }
        while(!see.isAfterLast()){
            List<String> presentations = new ArrayList<>();
            pointer = myDataBase.rawQuery("SELECT name FROM presentation,personal_presentation WHERE presentation.id=personal_presentation.id AND id_section="+see.getLong(0),null);
            pointer.moveToFirst();
            while (!pointer.isAfterLast()){
                presentations.add(pointer.getString(0));
                pointer.moveToNext();
            }
            result.put(see.getString(1), presentations);
            see.moveToNext();
            pointer.close();
        }
        see.close();
        myDataBase.close();
        return result;
    }

    public Long getNthSection(int position, Long idDay, Long hall) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT id FROM section WHERE id_day="+idDay+" AND id_hall="+hall+" LIMIT "+position+",1",null);
        see.moveToFirst();
        myDataBase.close();
        Long result = see.getLong(0);
        see.close();
        return result;
    }

    public Long getNthSectionFromPersonal(int position) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT id FROM personal ORDER BY time_from LIMIT "+position+",1",null);
        see.moveToFirst();
        myDataBase.close();
        Long result = see.getLong(0);
        return result;
    }

    public String getDateBySectionId(Long idSection) throws SQLException, ParseException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT day FROM day,section WHERE section.id_day=day.id AND section.id="+idSection,null);
        see.moveToFirst();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date day = dateFormat.parse(see.getString(0));
        see.close();
        myDataBase.close();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(day);
    }

    public void insertHall(int id, String name){
        ContentValues values = new ContentValues();
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

    public List<String> getPosterList(String query) throws SQLException {
        List<String> result = new ArrayList<>();
        openDataBase();
        Cursor see;
        if(query.equals(" ") || query.equals("") || query.equals(" Search")){
            see = myDataBase.rawQuery("SELECT Nazev FROM POK_Postery",null);
        } else {
            see = myDataBase.rawQuery("SELECT Nazev FROM POK_Postery WHERE Nazev LIKE '%"+query+"%' OR Autor LIKE '%"+query+"%'",null);
        }
        see.moveToFirst();
        while (!see.isAfterLast()){
            result.add(see.getString(0));
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return result;
    }

    public HashMap<String,List<String>> getPosterSectionPosterMap(String query) throws SQLException {
        openDataBase();
        HashMap<String,List<String>> result = new HashMap<>();
        Cursor see;
        if(query.equals(" ") || query.equals("") || query.equals(" Search")){
            see = myDataBase.rawQuery("SELECT IDSekce, Nazev, Autor, Firma FROM POK_Postery",null);
        } else {
            see = myDataBase.rawQuery("SELECT IDSekce, Nazev, Autor, Firma FROM POK_Postery WHERE Nazev LIKE '%"+query+"%' OR Autor LIKE '%"+query+"%'",null);
        }
        see.moveToFirst();
        while(!see.isAfterLast()){
            List<String> posterInfo = new ArrayList<>();
            Cursor pointer = myDataBase.rawQuery("SELECT Nazev FROM POK_Sekce WHERE ID="+see.getLong(0),null);
            pointer.moveToFirst();
            while (!pointer.isAfterLast()){
                posterInfo.add("Author: "+see.getString(2)+"("+see.getString(3)+")\n"+"Section: "+pointer.getString(0));
                pointer.moveToNext();
            }
            pointer.close();
            result.put(see.getString(1), posterInfo);
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return result;
    }

    public int getPositionInPersonalBySectionId(Long sectionId) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT id FROM personal ORDER BY time_from", null);
        see.moveToFirst();
        while(!see.isAfterLast()){
            Boolean compare = (see.getLong(0) == sectionId);
            if(compare){
                int result = see.getPosition();
                see.close();
                myDataBase.close();
                return result;
            }
            see.moveToNext();
        }
        see.close();
        myDataBase.close();
        return 0;
    }

    public boolean isSectionBreak(Long sectionId) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT type FROM section WHERE id="+sectionId+" LIMIT 1",null);
        see.moveToFirst();
        if (see.getString(0).equals("b")) {
            myDataBase.close();
            see.close();
            return true;
        }
        else {
            myDataBase.close();
            see.close();
            return false;
        }
    }

    public String getSectionName(Long idSection) throws SQLException {
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT name FROM section WHERE section.id="+idSection+"",null);
        see.moveToFirst();
        String name = see.getString(0);
        see.close();
        myDataBase.close();
        return name;
    }

    public void checkUpdateCreate() throws SQLException {
        openDataBase();
        myDataBase.execSQL("CREATE TABLE IF NOT EXISTS `update` (`year` INT, `month` INT, `day` INT, `hour` INT);");
        Cursor see = myDataBase.rawQuery("SELECT * FROM `update`;",null);
        Boolean result = see.getCount() > 0;

        if (!result) {
            myDataBase.execSQL("INSERT INTO `update` (`year`, `month`, `day`, `hour`) VALUES (1995, 5, 15, 14);");

        }
        see.close();
        myDataBase.close();
    }
    public Boolean checkLastUpdate() throws SQLException, ParseException {
        checkUpdateCreate();
        Calendar Today = Calendar.getInstance();
        Integer year,month,day,hour;
        openDataBase();
        Cursor see = myDataBase.rawQuery("SELECT year, month, day, hour FROM `update`", null);
        see.moveToFirst();
        year = see.getInt(0);
        month = see.getInt(1);
        day = see.getInt(2);
        hour = see.getInt(3);
        see.close();
        myDataBase.close();
        Boolean podminka = hour+3 < (Today.get(Today.HOUR_OF_DAY)) || month < (Today.get(Today.MONTH)) || day < Today.get(Today.DAY_OF_MONTH) || year < Today.get(Today.YEAR);
       /*** Dočasné pozastavení updatu ***/
        podminka = false;
        if(podminka) {
        return true;
           }
        else {
          return false;}
    }
    public void setDateTime() throws SQLException {
        openDataBase();
        Calendar Today = Calendar.getInstance();
        Integer year,month,day,hour;
        year = Today.get(Today.YEAR);
        month = Today.get(Today.MONTH);
        day = Today.get(Today.DAY_OF_MONTH);
        hour = Today.get(Today.HOUR_OF_DAY);
        myDataBase.execSQL("UPDATE `update` SET year ="+year+" ,month="+month+" ,day="+day+" ,hour="+hour+";");
        myDataBase.close();

    }
}
