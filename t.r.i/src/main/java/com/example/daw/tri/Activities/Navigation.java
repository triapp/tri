package com.example.daw.tri.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.daw.tri.Library.DatabaseHandler;
import com.example.daw.tri.R;

import java.sql.SQLException;
import java.text.ParseException;

public class Navigation extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        final DatabaseHandler database = new DatabaseHandler(getApplicationContext());
        final Button program = (Button) findViewById(R.id.button2);
        final Button exit = (Button) findViewById(R.id.button3);
        program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openProgram = new Intent(Navigation.this,ProgramActivity.class);
                startActivity(openProgram);
            }
        });

        Button personal =(Button) findViewById(R.id.button5);
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, Personal.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            String alertMessage = database.checkPersonalForCollisions();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
