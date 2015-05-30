package com.example.daw.tri.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.example.daw.tri.Library.DatabaseHandler;
import com.example.daw.tri.Library.PosterExpandableAdapter;
import com.example.daw.tri.R;

import java.sql.SQLException;
import java.util.List;

public class Posters extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posters);
        final ExpandableListView postersView = (ExpandableListView) findViewById(R.id.posters);
        final DatabaseHandler database = new DatabaseHandler(this);
        PosterExpandableAdapter posterAdapter = null;
        try {
            posterAdapter = new PosterExpandableAdapter(this,database.getPosterList(""),database.getPosterSectionPosterMap(" "));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        postersView.setAdapter(posterAdapter);

        final EditText search = (EditText) findViewById(R.id.searchBar);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    List<String> listOfPosters = database.getPosterList(search.getText().toString());
                    PosterExpandableAdapter posterAdapter = new PosterExpandableAdapter(getApplicationContext(),listOfPosters,database.getPosterSectionPosterMap(search.getText().toString()));
                    postersView.setAdapter(posterAdapter);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        postersView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastGroupClicked = -1;

            @Override
            public void onGroupExpand(int i) {
                if (lastGroupClicked !=-1 && i  != lastGroupClicked){
                    postersView.collapseGroup(lastGroupClicked);
                }
                lastGroupClicked = i;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_posters, menu);
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
