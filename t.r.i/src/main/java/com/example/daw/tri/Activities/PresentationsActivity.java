package com.example.daw.tri.Activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        final ListView presentationView = (ListView) findViewById(R.id.listView2);
        final String[] presentationAdapter = new String[allPresentations.size()];
        final Long[] presentationID = new Long[allPresentations.size()];
        int i = 0;
        for (Presentation presentation : allPresentations) {
            presentationID[i] = presentation.getId();
            presentationAdapter[i] = presentation.toString();
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, presentationAdapter);
        presentationView.setAdapter(adapter);
        int mNotificationId = 001;

        Intent intent = new Intent (this,Home.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

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
