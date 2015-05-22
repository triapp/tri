package com.example.daw.tri.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.daw.tri.Library.DatabaseHandler;
import com.example.daw.tri.Library.SpeakerPresentationAdapter;
import com.example.daw.tri.R;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class PresentationsActivity extends ActionBarActivity {
    Long id;
    DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentations);
        final DatabaseHandler database = new DatabaseHandler(getApplicationContext());
        Bundle b = getIntent().getExtras();
        final String author = b.getString("author");
        List<String> presentationBySpeaker = null;
        try {
            presentationBySpeaker = database.getPresentationBySpeaker(author);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final ListView presentationView = (ListView) findViewById(R.id.presentationView);
        Long[] presentationId = new Long[0];
        try {
            presentationId = database.getArrayIdPresentationByAuthor(author);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SpeakerPresentationAdapter adapter = new SpeakerPresentationAdapter(this,presentationBySpeaker,author);
        presentationView.setAdapter(adapter);
        presentationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox =(CheckBox) view.findViewById(R.id.checkBox);
                try {
                    Long section = database.getSectionIdByPresentationPosition(author,position);
                    Long presentation = database.getPresentationIdByPresentationPosition(author,position);
                    if (checkBox.isChecked()){
                        database.removePresentationFromPersonal(presentation);
                        if(!database.doesHavePersonalSectionPresentations(section)){
                            database.removeSectionFromPersonal(section);
                            Toast.makeText(getApplicationContext(), "Section was removed from your program.", Toast.LENGTH_SHORT).show();
                        }
                        checkBox.setChecked(!checkBox.isChecked());
                    }  else {
                        if (database.isSectionInPersonal(section)) {
                            database.insertPersonalPresentation(presentation);
                        } else {
                            database.insertPersonalSection(section);
                            database.insertPersonalPresentation(presentation);
                            Toast.makeText(getApplicationContext(),"Section was added to your program.",Toast.LENGTH_SHORT).show();
                        }
                        checkBox.setChecked(!checkBox.isChecked());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

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
