package com.example.daw.tri;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Home extends ActionBarActivity {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       TextView txtonline = (TextView) findViewById(R.id.txt_online);
           try {
               URL url = new URL("http://www.google.com");
               HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
               urlc.setConnectTimeout(3000);
               urlc.connect();
               if (urlc.getResponseCode() == 200) {

                   txtonline.setText("Online");
               }
               else{

                   txtonline.setText("Offline");
               }

           } catch (MalformedURLException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
           } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
