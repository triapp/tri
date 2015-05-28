package com.example.daw.tri.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.daw.tri.Library.DatabaseHandler;
import com.example.daw.tri.Library.PersonalExpandableAdapter;
import com.example.daw.tri.R;

import java.sql.SQLException;
import java.text.ParseException;

public class Personal extends ActionBarActivity {

    ExpandableListView expandView;
    PersonalExpandableAdapter expandAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Personal programme");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        final DatabaseHandler database = new DatabaseHandler(getApplicationContext());
        expandView = (ExpandableListView) findViewById(R.id.expandableListView2);
        try {
            expandAdapter = new PersonalExpandableAdapter(this,database.getPersonalList(),database.getPersonalPresentationMap());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expandView.setAdapter(expandAdapter);

        expandView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                CheckBox checkBox =(CheckBox) v.findViewById(R.id.checkBox);
                try {
                    Long section = database.getNthSectionFromPersonal(groupPosition);
                    Long presentation = database.getNthPresentationFromPersonal(section, childPosition);
                    if (checkBox.isChecked()){
                        Toast.makeText(getApplicationContext(), "Presentation was removed from your personal programme.", Toast.LENGTH_SHORT).show();
                        database.removePresentationFromPersonal(presentation);
                        if(!database.doesHavePersonalSectionPresentations(section)){
                            database.removeSectionFromPersonal(section);
                            Intent intent = new Intent(getApplicationContext(), Personal.class);
                            startActivity(intent);
                            finish();
                        }
                        checkBox.setChecked(!checkBox.isChecked());
                    }  else {
                        if (database.isSectionInPersonal(section)) {
                            database.insertPersonalPresentation(presentation);
                        } else {
                            database.insertPersonalSection(section);
                            database.insertPersonalPresentation(presentation);
                        }
                        Toast.makeText(getApplicationContext(),"Presentation was added to your personal programme.",Toast.LENGTH_SHORT).show();
                        checkBox.setChecked(!checkBox.isChecked());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });


        expandView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastGroupClicked = -1;
            @Override
            public void onGroupExpand(int i) {
                if (lastGroupClicked !=-1 && i  != lastGroupClicked){
                    expandView.collapseGroup(lastGroupClicked);
                }
                lastGroupClicked = i;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_personal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.goBack) {
            finish();
            return true;
        } else if (id == R.id.showMenu) {
            Intent intent = new Intent(Personal.this, Navigation.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
