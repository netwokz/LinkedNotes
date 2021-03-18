package com.netwokz.linkednotes;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements EditNameDialogFragment.NameCommunicator, GroceryItemDialogFragment.ItemCommunicator, SharedPreferences.OnSharedPreferenceChangeListener {

    String TAG = "MainActivity";

    ArrayList<GroceryListItem> mGroceryList;

    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    RecyclerView rvGrocery;
    ItemAdapter adapter;

    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        edit = prefs.edit();
        getSystemTheme();
        String theme = prefs.getString("theme", "MODE_NIGHT_FOLLOW_SYSTEM");
        changeTheme(theme);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Grocery");
        rvGrocery = findViewById(R.id.rv_grocery_list);

        prefs.registerOnSharedPreferenceChangeListener(this);
        edit = prefs.edit();
//        if (!prefs.getBoolean("FirstRun", false)) {
//            showNameDialog();
//        }

        mGroceryList = new ArrayList<>();
        userName = prefs.getString("name", "noName");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("MainActivity", "onDataChanged");
                mGroceryList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    GroceryListItem mItem = snapshot1.getValue(GroceryListItem.class);
                    mItem.setKey(snapshot1.getKey());
                    mItem.setPerson(userName);
                    edit.putBoolean(mItem.getKey(), mItem.isChecked());
                    mGroceryList.add(mItem);
                    Log.d("MainActivity:onDataChanged", snapshot1.getKey());
                    edit.commit();
                }

                Collections.sort(mGroceryList, new Comparator<GroceryListItem>() {
                    @Override
                    public int compare(GroceryListItem o1, GroceryListItem o2) {
                        return Boolean.compare(o1.isChecked(), o2.isChecked());
                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new ItemAdapter(mGroceryList, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GroceryListItem mListItem = mGroceryList.get(position);
                mDatabaseRef.child(mListItem.getKey()).child("checked").setValue(mListItem.isChecked());
                Log.d("SecondFragment", "onItemClick" + mListItem.getKey() + ": isChecked " + mListItem.isChecked());
            }

            @Override
            public void onEditClick(int position) {
                editItemDialog(mGroceryList.get(position).getItem());
                deleteListItem(mGroceryList.get(position).getKey());
            }

            @Override
            public void onDeleteClick(int position) {
                mDatabaseRef.child(mGroceryList.get(position).getKey()).removeValue();
            }
        });

        rvGrocery.setAdapter(adapter);
        rvGrocery.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showItemDialog();
            }
        });
    }

    private void getSystemTheme() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                edit.putString("theme", "UI_MODE_NIGHT_YES");
                edit.apply();
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                edit.putString("theme", "UI_MODE_NIGHT_NO");
                edit.apply();
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                edit.putString("theme", "UI_MODE_NIGHT_UNDEFINED");
                edit.apply();
                break;
        }
    }

    public void addGroceryItem(String item) {
        Log.d("MainActivity:addGroceryItem()", "Item: " + item);
        GroceryListItem mListItem = new GroceryListItem(userName, item);
        mGroceryList.add(mListItem);
        String mId = mDatabaseRef.push().getKey();
        mDatabaseRef.child(mId).setValue(mListItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void messageItem(String item) {
        addGroceryItem(item);
    }

    public void showNameDialog() {
        FragmentManager manager = getFragmentManager();
        EditNameDialogFragment mydialog = new EditNameDialogFragment();
        mydialog.show(manager, "mydialog");

    }

    public void showItemDialog() {
        FragmentManager manager = getFragmentManager();
        GroceryItemDialogFragment mydialog = new GroceryItemDialogFragment();
        mydialog.show(manager, "mydialog");
    }

    public void editItemDialog(String item) {
        FragmentManager manager = getFragmentManager();
        Bundle data = new Bundle();
        data.putString("item", item);
        GroceryItemDialogFragment mydialog = new GroceryItemDialogFragment();
        mydialog.setArguments(data);
        mydialog.show(manager, "mydialog");
    }

    @Override
    public void messageName(String user) {
        Toast.makeText(this, "Hello, " + user, Toast.LENGTH_SHORT).show();
        edit.putString("name", user);
        edit.putBoolean("FirstRun", true);
        edit.apply();
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
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_delete_db:
                mDatabaseRef.child("3").removeValue();
//                mDatabase.removeValue();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeTheme(String theme) {
        switch (theme) {
            case "MODE_NIGHT_YES":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "MODE_NIGHT_NO":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "MODE_NIGHT_FOLLOW_SYSTEM":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        userName = prefs.getString("name", "noName");
        String temp = prefs.getString("theme", "MODE_NIGHT_FOLLOW_SYSTEM");
        changeTheme(temp);
    }

    public void deleteListItem(String key) {
        mDatabaseRef.child(key).removeValue();
    }
}