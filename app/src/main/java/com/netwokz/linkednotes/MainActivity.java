package com.netwokz.linkednotes;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    String[] foodArray = {"Bread", "Milk", "Beer", "Cheese", "Paper Towels", "Plates", "Cereal", "Water", "Steak", "Hot Dogs"};

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference("Grocery/");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy 'at' h:mm:ss");
                String currentDateAndTime = sdf.format(new Date());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_delete_db:
                mDatabase.child("3").removeValue();
//                mDatabase.removeValue();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public int getRandomNumber(int number) {
        Random rand = new Random();
        return rand.nextInt(number);
    }
}