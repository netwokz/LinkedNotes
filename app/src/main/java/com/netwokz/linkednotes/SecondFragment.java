package com.netwokz.linkednotes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class SecondFragment extends Fragment implements ItemAdapter.OnClickListener {

    ArrayList<GroceryListItem> mGroceryList;
    String[] foodArray = {"Bread", "Milk", "Beer", "Cheese", "Paper Towels", "Plates", "Cereal", "Water", "Steak", "Hot Dogs"};
    String[] names = {"Steve", "Taylor"};

    int mCount = 0;

    private DatabaseReference mDatabase;
    RecyclerView rvGrocery;
    ItemAdapter adapter;

    @Override
    public void onClick(Boolean clickedItem, int position) {
        Log.d("SecondFragment", "Interface OnClick");
        Log.d("SecondFragment", "Item " + position + " " + "isChecked: " + clickedItem);
        GroceryListItem mListItem = mGroceryList.get(position - 1);
        mListItem.setIsCurrent(clickedItem.toString());
        Log.d("SecondFragment", "mDatabase.child(String.valueOf(mCount)).child(\"active\").getKey" + " " + mDatabase.child(String.valueOf(mCount)).child("active").getKey());
        mDatabase.child(String.valueOf(mCount)).child("active").setValue(clickedItem.toString());
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateDatabase();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("Grocery");
        mGroceryList = new ArrayList<>();
        rvGrocery = view.findViewById(R.id.rv_grocery_list);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mGroceryList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String mID = snapshot1.getKey();
                    String id = snapshot1.child("id").getValue(String.class);
                    String active = snapshot1.child("active").getValue(String.class);
                    String item = snapshot1.child("item").getValue(String.class);
                    String person = snapshot1.child("person").getValue(String.class);
                    Log.d("Get Data()", "key: " + mID + ", " + "id: " + id + ", " + "active: " + active + ", " + "item: " + item + ", " + "person: " + person);

                    mCount = Integer.valueOf(mID);
                    if (mGroceryList.size() < mCount) {
                        GroceryListItem mListItem = new GroceryListItem(String.valueOf(mCount), person, item, active);
                        mGroceryList.add(mListItem);
                    }
                    adapter.notifyItemChanged(mCount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

//        populateDatabase(10);

        getData();
        adapter = new ItemAdapter(mGroceryList);
        adapter.setOnClickListener(this::onClick);
        rvGrocery.setAdapter(adapter);
        rvGrocery.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void getData() {
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    String mID = snapshot1.getKey();
//                    String id = snapshot1.child("id").getValue(String.class);
//                    String active = snapshot1.child("active").getValue(String.class);
//                    String item = snapshot1.child("item").getValue(String.class);
//                    String person = snapshot1.child("person").getValue(String.class);
//                    Log.d("Get Data()", "key: " + mID + ", " + "id: " + id + ", " + "active: " + active + ", " + "item: " + item + ", " + "person: " + person);
//
//                    mCount = Integer.valueOf(mID);
//                    if (mGroceryList.size() < mCount) {
//                        GroceryListItem mListItem = new GroceryListItem(String.valueOf(mCount), person, item, active);
//                        mGroceryList.add(mListItem);
//                    }
//                    adapter.notifyItemChanged(mCount);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public int getRandomArrayEntry(int number) {
        Random rand = new Random();
        return rand.nextInt(number);
    }

    public void populateDatabase(int numberOfEntries) {
        for (int i = 0; i < numberOfEntries; i++) {
            GroceryListItem mListItem = new GroceryListItem(String.valueOf(i + 1), names[getRandomArrayEntry(2)], foodArray[getRandomArrayEntry(10)], "false");
            mGroceryList.add(mListItem);
            mDatabase.child(String.valueOf(i + 1)).setValue(mListItem);
        }
    }

    public void populateDatabase() {
        mCount++;
        GroceryListItem mListItem = new GroceryListItem(String.valueOf(mCount), names[getRandomArrayEntry(2)], foodArray[getRandomArrayEntry(10)], "false");
        mGroceryList.add(mListItem);
        mDatabase.child(String.valueOf(mCount)).setValue(mListItem);
        adapter.notifyDataSetChanged();
    }

    public ArrayList<GroceryListItem> generateDemoList(int numOfEntries) {
        ArrayList<GroceryListItem> mList = new ArrayList<>();
        for (int i = 0; i < numOfEntries; i++) {
            GroceryListItem mListItem = new GroceryListItem(String.valueOf(i + 1), names[getRandomArrayEntry(2)], foodArray[getRandomArrayEntry(10)], "false");
            mList.add(mListItem);
        }
        return mList;
    }
}