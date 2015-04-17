package com.example.daw.tri.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.daw.tri.Library.Communicator;
import com.example.daw.tri.Library.DatabaseHandler;
import com.example.daw.tri.Library.Network;
import com.example.daw.tri.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;


public class Home extends ActionBarActivity {
    TextView networkError;
    Button nextActivity;
    Button update;
    DatabaseHandler database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_home);
       networkError = (TextView) findViewById(R.id.textView);
       nextActivity = (Button) findViewById(R.id.button);
       update = (Button) findViewById(R.id.button1);
       networkError.setVisibility(View.GONE);
       nextActivity.setVisibility(View.GONE);
       update.setVisibility(View.GONE);
       database = new DatabaseHandler(this);
       try {
           database.createDataBase();
       } catch (IOException ioe) {
           throw new Error("Unable to create database");
       }
       try {
           database.openDataBase();
       }catch(SQLException sqle){
       }
        tryUpdate();
        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ProgramActivity.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryUpdate();
            }
        });


    }

    public void tryUpdate(){
        networkError.setVisibility(View.GONE);
        nextActivity.setVisibility(View.GONE);
        update.setVisibility(View.GONE);
        Network internet = new Network(getApplicationContext());
        if (internet.isOnline()) {
           database.dropAll();
           new downloadTables().execute();
        } else {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            networkError.setVisibility(View.VISIBLE);
            nextActivity.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);
        }
        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ProgramActivity.class);
                startActivity(intent);
            }
        });

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
        private TextView status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            status =(TextView) findViewById(R.id.textView);
            status.setText("Contacting server...");
        }

        @Override
        protected JSONArray doInBackground(String... args) {
            status.setText("Getting data...");
            Communicator talkie = new Communicator();
            JSONArray json = talkie.getTables();
            return json;
        }
        @Override
        protected void onPostExecute(JSONArray json) {
            try {
                status.setText("Inserting data...");
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                for (int i = 0 ; i < json.length(); i++) {
                    JSONObject obj = json.getJSONObject(i);

                    JSONArray tableDay = obj.getJSONArray("day");
                    int amount = tableDay.length();
                    int done  = 0;
                    do{
                        JSONObject CurrentDay = tableDay.getJSONObject(done);
                        db.insertDay(CurrentDay.getInt("id"),CurrentDay.getString("day"));
                        done++;
                    } while(done < amount);

                    JSONArray tableHall = obj.getJSONArray("hall");
                    amount = tableHall.length();
                    done = 0;
                    do{
                        JSONObject CurrentHall = tableHall.getJSONObject(done);
                        db.insertHall(CurrentHall.getInt("id"),CurrentHall.getString("name"));
                        done++;
                    } while(done < amount);

                    JSONArray tablePresentation = obj.getJSONArray("presentation");
                    amount = tablePresentation.length();
                    done = 0;
                    do{
                        JSONObject CurrentPresentation = tablePresentation.getJSONObject(done);
                        db.insertPresentation(CurrentPresentation.getInt("id"), CurrentPresentation.getInt("id_section"), CurrentPresentation.getString("name"), CurrentPresentation.getString("author"));
                        done++;
                    } while(done < amount);

                    JSONArray tableSection = obj.getJSONArray("section");
                    amount = tableSection.length();
                    done = 0;
                    do{
                        JSONObject CurrentSection = tableSection.getJSONObject(done);
                        db.insertSection(CurrentSection.getInt("id"),CurrentSection.getInt("id_hall"),CurrentSection.getInt("id_day"),CurrentSection.getString("name"),CurrentSection.getString("chairman"),CurrentSection.getString("time_from"),CurrentSection.getString("time_to"),CurrentSection.getString("type"));
                        done++;
                    } while(done < amount);
                }

                Intent intent = new Intent(Home.this, ProgramActivity.class);
                startActivity(intent);
                finish();

            } catch (JSONException e) {
               e.printStackTrace();
            }

        }}
}