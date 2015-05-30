package com.example.daw.tri.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.example.daw.tri.R;

public class CongressVenue extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Congress Venue");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congress_venue);

        WebView congress = (WebView) findViewById(R.id.webView);
        congress.loadUrl("file:///android_asset/congress/02.html");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_congress_venue, menu);
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
            Intent intent = new Intent(CongressVenue.this, Navigation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
