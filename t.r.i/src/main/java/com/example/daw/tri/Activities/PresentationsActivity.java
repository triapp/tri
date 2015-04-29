package com.example.daw.tri.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.daw.tri.Library.DatabaseHandler;
import com.example.daw.tri.Objects.Presentation;
import com.example.daw.tri.R;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class PresentationsActivity extends ActionBarActivity {
    Long id;
    DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentations);
        database = new DatabaseHandler(getApplicationContext());
        ArrayList<Presentation> allPresentations = null;
        Bundle b = getIntent().getExtras();
        id = b.getLong("id");
        try {
            allPresentations = database.selectPresentationById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_presentations, menu);
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
