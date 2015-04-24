package com.example.daw.tri.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Toast;

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
        final DatabaseHandler database = new DatabaseHandler(getApplicationContext());
        Bundle b = getIntent().getExtras();
        Long id = b.getLong("dayId");
        expandView = (ExpandableListView) findViewById(R.id.expandableListView);
        try {
            expandAdapter = new ExpandableAdapter(this,database.getSectionList(id),database.getSectionPresentationMap(id),0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        expandView.setAdapter(expandAdapter);

        expandView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
               CheckBox checkBox =(CheckBox) v.findViewById(R.id.checkBox);
               try {
                    Long section = database.getNthSection(groupPosition);
                    Long presentation = database.getNthPresentation(section,childPosition);
                    if (checkBox.isChecked()){
                        database.removePresentationFromPersonal(presentation);
                        checkBox.setChecked(!checkBox.isChecked());
                    }  else {
                        if (database.isSectionInPersonal(section)) {
                            database.insertPersonalPresentation(presentation);
                            checkBox.setChecked(!checkBox.isChecked());
                        } else {
                            Toast.makeText(getApplicationContext(),"First add the section into your personal program.",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
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
