package com.orl.chigger.tri.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orl.chigger.tri.Library.DatabaseHandler;
import com.orl.chigger.tri.R;

import java.sql.SQLException;
import java.util.List;

public class HallActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHandler database = new DatabaseHandler(getApplicationContext());
        Bundle b = getIntent().getExtras();
        final Long idDay = b.getLong("dayId");
        final String date = b.getString("date");
        setTitle(date);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        List<String> hallOnDay = null;
        try {
            hallOnDay = database.getHallListbyDay(idDay);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final ListView hallView = (ListView) findViewById(R.id.hallListView);
        Long[] hallId = new Long[0];
        try {
            hallId = database.getArrayIdHall(idDay);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, hallOnDay);
        hallView.setAdapter(adapter);
        final Long[] finalHallId = hallId;
        hallView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                Intent intent = new Intent(HallActivity.this, Sections.class);
                Bundle b = new Bundle();
                b.putLong("hallId", finalHallId[itemPosition]);
                b.putLong("dayId", idDay);
                String title = date.substring(0,6);
                title += " "+ ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
                b.putString("title", title);
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
        if (id == R.id.goBack) {
            finish();
            return true;
        } else if (id == R.id.showMenu) {
            Intent intent = new Intent(HallActivity.this, Navigation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
