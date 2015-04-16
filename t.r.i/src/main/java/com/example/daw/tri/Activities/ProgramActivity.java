package com.example.daw.tri.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.daw.tri.Library.DatabaseHandler;
import com.example.daw.tri.Objects.Day;
import com.example.daw.tri.R;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;


public class ProgramActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        DatabaseHandler database = new DatabaseHandler(getApplicationContext());
        ArrayList<Day> allDays = null;
        //database.insertDay(10,"bam");
        try {
            allDays = database.selectDay();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final ListView dayView = (ListView) findViewById(R.id.listView);
        final String[] daysAdapter = new String[allDays.size()];
        final Long[] daysID = new Long[allDays.size()];
        int i = 0;
        for (Day day : allDays) {
            daysID[i] = day.getId();
            daysAdapter[i] = day.toString();
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, daysAdapter);
        dayView.setAdapter(adapter);
        dayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                int itemPosition = position;
                Intent intent = new Intent(ProgramActivity.this, DayActivity.class);
                Bundle b = new Bundle();
                b.putLong("id",daysID[itemPosition]);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_program, menu);
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
