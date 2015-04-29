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
import com.example.daw.tri.R;

import java.sql.SQLException;
import java.util.List;

public class HallActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHandler database = new DatabaseHandler(getApplicationContext());
        Bundle b = getIntent().getExtras();
        final Long idDay = b.getLong("dayId");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        List<String> hallOnDay = null;
        try {
            hallOnDay = database.getHallListbyDay(idDay);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final ListView hallView = (ListView) findViewById(R.id.hallListView);
        final String[] hallsAdapter = new String[hallOnDay.size()];
        Long[] hallId = new Long[0];
        try {
            hallId = database.getArrayIdHall(idDay);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int i = 0;
        for (String hall : hallOnDay) {
            hallsAdapter[i] = hall;
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, hallsAdapter);
        hallView.setAdapter(adapter);
        final Long[] finalHallId = hallId;
        hallView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                Intent intent = new Intent(HallActivity.this, DayActivity.class);
                Bundle b = new Bundle();
                b.putLong("hallId", finalHallId[itemPosition]);
                b.putLong("dayId", idDay);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hall, menu);
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
