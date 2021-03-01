package com.netwokz.linkednotes;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = null;
    String TAG = "MainActivity";
    String LIST = "list";
    Integer currentListCountID;
    DocumentReference myGroceryList;
    Map<String, Object> groceryList = null;
    String[] foodArray = {"Bread", "Milk", "Beer", "Cheese", "Paper Towels", "Plates", "Cereal", "Water", "Steak", "Hot Dogs"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (db == null) {
            initializeDataBase();
        }
        retreiveDB();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testDBEntry();
            }
        });
    }

    public void initializeDataBase() {
        // Access a Cloud Firestore instance from your Activity
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        myGroceryList = db.collection("Lists").document("Grocery's");
        // Retrieve list and IDs, set counter accordingly
        retreiveDB();
        currentListCountID = 0;
    }

    private void retreiveDB() {
        myGroceryList.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        groceryList = document.getData();
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                        groceryList = new HashMap<>();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        Map<String, Object> groceryList = new HashMap<>();
    }

    public String generateListID() {
        int id = currentListCountID;
        id++;
        currentListCountID = id;
        return String.valueOf(id);
    }

    public int getRandomArrayEntry() {
        Random rand = new Random();
        return rand.nextInt(10);
    }

    public void testDBEntry() {
//        Map<String, Object> groceryList = new HashMap<>();
//        for (int i = 0; i < 15; i++) {
//            groceryList.put(generateListID(), foodArray[getRandomArrayEntry()]);
//        }
//        myGroceryList.set(groceryList);
        for (int i = 0; i < 15; i++) {
            groceryList.put(generateListID(), foodArray[getRandomArrayEntry()]);
        }
        myGroceryList.set(groceryList);

    }

    public void testDBDelete(String entry) {
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}