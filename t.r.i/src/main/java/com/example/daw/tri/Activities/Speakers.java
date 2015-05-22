package com.example.daw.tri.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.daw.tri.Library.DatabaseHandler;
import com.example.daw.tri.R;

import java.sql.SQLException;
import java.util.List;

public class Speakers extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final DatabaseHandler database = new DatabaseHandler(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speakers);

        List<String> listOfSpeakers = null;
        try {
             listOfSpeakers = database.getSpeakers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final ListView speakerView = (ListView) findViewById(R.id.speakerListView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, listOfSpeakers);
        speakerView.setAdapter(adapter);

        final EditText search = (EditText) findViewById(R.id.searchView);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                List<String> listOfQuerySpeakers = null;
                try {
                    listOfQuerySpeakers = database.getSpeakersSearch(search.getText().toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, listOfQuerySpeakers);
                speakerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        speakerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Speakers.this, PresentationsActivity.class);
                Bundle b = new Bundle();
                b.putString("author", view.toString());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_speakers, menu);
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
