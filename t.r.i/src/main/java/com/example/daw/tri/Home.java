package com.example.daw.tri;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;


public class Home extends ActionBarActivity {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_home);
       final DatabaseHandler database = new DatabaseHandler(this);
       try {
           database.createDataBase();
       } catch (IOException ioe) {
           throw new Error("Unable to create database");
       }
       try {
           database.openDataBase();
       }catch(SQLException sqle){
       }

       TextView txtonline = (TextView) findViewById(R.id.txt_online);

       Network internet = new Network(getApplicationContext());
       if (internet.isOnline()) {
           txtonline.setText("online");
           database.dropDay();
           new downloadTables().execute();
       } else txtonline.setText("offline");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class downloadTables extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Home.this);
            pDialog.setTitle("Komunikace se serverem");
            pDialog.setMessage("Stahování dat");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... args) {
            Communicator talkie = new Communicator();
            JSONArray json = talkie.getTables();
            return json;
        }
        @Override
        protected void onPostExecute(JSONArray json) {
            try {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                for (int i = 0 ; i < json.length(); i++) {
                    JSONObject obj = json.getJSONObject(i);
                    JSONArray tableDay = obj.getJSONArray("day");
                    for (int i1 = 0; i < tableDay.length(); i1++){
                        JSONObject CurrentDay = tableDay.getJSONObject(i1);
                        db.insertDay(CurrentDay.getInt("id"),CurrentDay.getString("day"));
                    }
                    JSONArray tableHall = obj.getJSONArray("hall");
                    for (int i1 = 0; i < tableHall.length(); i1++){
                        JSONObject CurrentHall = tableHall.getJSONObject(i1);
                        db.insertHall(CurrentHall.getInt("id"),CurrentHall.getString("name"));
                    }
                    JSONArray tablePresentation = obj.getJSONArray("presentation");
                    for (int i1 = 0; i < tablePresentation.length(); i1++){
                        JSONObject CurrentPresentation = tablePresentation.getJSONObject(i1);
                        db.insertHall(CurrentPresentation.getInt("id"),CurrentPresentation.getString("name"));
                    }
                    JSONArray tableSection = obj.getJSONArray("section");
                    for (int i1 = 0; i < tableSection.length(); i1++){
                        JSONObject CurrentSection = tableSection.getJSONObject(i1);
                        db.insertSection(CurrentSection.getInt("id"),CurrentSection.getInt("id_hall"),CurrentSection.getInt("id_day"),CurrentSection.getString("name"),CurrentSection.getString("chairman"),CurrentSection.getString("time_from"),CurrentSection.getString("time_to"),CurrentSection.getString("type"));
                    }
                }
                pDialog.dismiss();

            } catch (JSONException e) {
               e.printStackTrace();
            }

        }}
}
