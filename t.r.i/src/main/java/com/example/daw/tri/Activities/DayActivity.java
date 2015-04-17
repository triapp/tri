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
import com.example.daw.tri.Objects.Section;
import com.example.daw.tri.R;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;


public class DayActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        DatabaseHandler database = new DatabaseHandler(getApplicationContext());
        ArrayList<Section> allSections = null;
        Bundle b = getIntent().getExtras();
        Long id = b.getLong("dayId");
        try {
            allSections = database.selectSectionByDay(id);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final ListView sectionView = (ListView) findViewById(R.id.listView1);
        final String[] sectionAdapter = new String[allSections.size()];
        final Long[] sectionsID = new Long[allSections.size()];
        int i = 0;
        for (Section section : allSections) {
            sectionsID[i] = section.getId();
            sectionAdapter[i] = section.toString();
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, sectionAdapter);
        sectionView.setAdapter(adapter);
        sectionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                Intent intent = new Intent(DayActivity.this, PresentationsActivity.class);
                Bundle b = new Bundle();
                b.putLong("id", sectionsID[itemPosition]);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
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
