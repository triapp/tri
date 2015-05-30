package com.example.daw.tri.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.daw.tri.Library.DatabaseHandler;
import com.example.daw.tri.R;

import java.sql.SQLException;
import java.text.ParseException;

public class Navigation extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_navigation);
        final DatabaseHandler database = new DatabaseHandler(getApplicationContext());
        final ImageButton program = (ImageButton) findViewById(R.id.scientific);

        program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openProgram = new Intent(Navigation.this,ProgramActivity.class);
                startActivity(openProgram);
            }
        });
        // Buttony__________________________________________________________________________________
        ImageButton personal =(ImageButton) findViewById(R.id.personal);
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, Personal.class);
                Bundle b = new Bundle();
                b.putInt("expanded", -1);
                intent.putExtras(b);
                startActivity(intent);
            }
        });


        ImageButton generalInfo =(ImageButton) findViewById(R.id.generalInfo);
        generalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, GeneralInfo.class);
                startActivity(intent);
            }
        });

        ImageButton congress_venue =(ImageButton) findViewById(R.id.congress);
        congress_venue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, CongressVenue.class);
                startActivity(intent);
            }
        });

        ImageButton speakers =(ImageButton) findViewById(R.id.speakers);
        speakers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, Speakers.class);
                startActivity(intent);
            }
        });

        ImageButton accompanying =(ImageButton) findViewById(R.id.accompanying1);
        accompanying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, Accompanying.class);
                startActivity(intent);
            }
        });

        ImageButton exhibitors =(ImageButton) findViewById(R.id.exhibitors);
        exhibitors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, Exhibitors.class);
                startActivity(intent);
            }
        });

        ImageButton cochlear =(ImageButton) findViewById(R.id.cochlear);
        cochlear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, Cohlear.class);
                startActivity(intent);
            }
        });

        ImageButton posters =(ImageButton) findViewById(R.id.posters);
        posters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, Posters.class);
                startActivity(intent);
            }
        });

        //Funkce____________________________________________________________________________________

        try {
            String alertMessage = database.checkPersonalForCollisions();
            alertMessage += database.renewPersonal();
            if (alertMessage !=""){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(alertMessage)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
