package com.example.daw.tri.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.daw.tri.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;


public class Home extends ActionBarActivity {
    TextView networkError;
    Button nextActivity;

    DatabaseHandler database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(" ORL - HNS 2015");
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_home);
       networkError = (TextView) findViewById(R.id.Connect);
       nextActivity = (Button) findViewById(R.id.button);

      Button exit = (Button) findViewById(R.id.exitBTN);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       networkError.setVisibility(View.GONE);
       nextActivity.setVisibility(View.GONE);

        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Navigation.class);
                startActivity(intent);
                finish();
            }
        });
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
        new NetCheck().execute();
    }

    public void isOnline(){
        tryUpdate();
    }

    public void tryUpdate(){
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        networkError.setVisibility(View.GONE);
        nextActivity.setVisibility(View.GONE);

       if (network==true) {
           database.dropAll();
           new downloadTables().execute();
        } else {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            TextView Connect = (TextView) findViewById(R.id.Connect);
            Connect.setText("You are not connected to the Internet network");
           Connect.setVisibility(View.VISIBLE);
            networkError.setVisibility(View.VISIBLE);
            nextActivity.setVisibility(View.VISIBLE);

        }

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


        return super.onOptionsItemSelected(item);
    }

    private class downloadTables extends AsyncTask<String, String, JSONArray> {
        private TextView status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           status =(TextView) findViewById(R.id.Connect);
           status.setText("Getting data...");
        }

        @Override
        protected JSONArray doInBackground(String... args) {

            Communicator talkie = new Communicator();
            JSONArray json = talkie.getTables();
            try {
                //   status.setText("Inserting data...");
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

                    JSONArray tableHallDay = obj.getJSONArray("day_hall");
                    amount = tableHallDay.length();
                    done = 0;
                    do{
                        JSONObject CurrentHallDay = tableHallDay.getJSONObject(done);
                        db.insertHallDay(CurrentHallDay.getInt("id_day"),CurrentHallDay.getInt("id_hall"));
                        done++;
                    } while(done < amount);
                }
                Intent intent = new Intent(Home.this, Navigation.class);
                startActivity(intent);
                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return json;
        }


        @Override
        protected void onPostExecute(JSONArray json) {
            status.setText("Launching application..");
        }

    }
    boolean network;



private class NetCheck extends AsyncTask<String,String,Boolean>
{
    private ProgressDialog nDialog;

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        nDialog = new ProgressDialog(Home.this);
        nDialog.setTitle("Checking Internet connection");
        nDialog.setMessage("Checking...");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(false);
        nDialog.show();
    }
    /**
     * Gets current device state and checks for working internet connection by trying Google.
     **/
    @Override
    protected Boolean doInBackground(String... args){



        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL("http://www.tri.cz");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(1000);
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                }
            } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;

    }
    @Override
    protected void onPostExecute(Boolean th){

        if(th == true){
            nDialog.dismiss();
            network = true;
            isOnline();
        }
        else{
            nDialog.dismiss();
            network = false;
            isOnline();
        }
    }
}

}