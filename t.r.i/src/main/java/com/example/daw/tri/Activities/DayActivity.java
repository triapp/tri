package com.example.daw.tri.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.example.daw.tri.Library.DatabaseHandler;
import com.example.daw.tri.Library.ExpandableAdapter;
import com.example.daw.tri.R;

import java.sql.SQLException;


public class DayActivity extends ActionBarActivity {

    ExpandableAdapter expandAdapter;
    ExpandableListView expandView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        DatabaseHandler database = new DatabaseHandler(getApplicationContext());
        Bundle b = getIntent().getExtras();
        Long id = b.getLong("dayId");
        expandView = (ExpandableListView) findViewById(R.id.expandableListView);
        try {
            expandAdapter = new ExpandableAdapter(this,database.getSectionList(id),database.getSectionPresentationMap(id),0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        expandView.setAdapter(expandAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day, menu);
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
