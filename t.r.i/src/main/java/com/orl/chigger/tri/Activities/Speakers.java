package com.orl.chigger.tri.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.orl.chigger.tri.Library.DatabaseHandler;
import com.orl.chigger.tri.R;

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
                b.putString("author", ((TextView)view.findViewById(android.R.id.text1)).getText().toString());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                search.setTextColor(Color.parseColor("#000000"));
                if (search.getText().toString().equals( " Search")){
                    search.setText("");

                }
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
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
        if (id == R.id.goBack) {
            finish();
            return true;
        } else if (id == R.id.showMenu) {
            Intent intent = new Intent(Speakers.this, Navigation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
