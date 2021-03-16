package com.netwokz.linkednotes;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class SecondFragment extends Fragment implements GroceryItemDialogFragment.ItemCommunicator {

    ArrayList<GroceryListItem> mGroceryList;
    String[] foodArray = {"Bread", "Milk", "Beer", "Cheese", "Paper Towels", "Plates", "Cereal", "Water", "Steak", "Hot Dogs"};
    String[] names = {"Steve", "Taylor"};

//    int mCount = -1;

    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    RecyclerView rvGrocery;
    ItemAdapter adapter;

    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    String userName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGroceryList = new ArrayList<>();
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        userName = prefs.getString("name", "noName");

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                populateDatabase();
                showDialog();
            }
        });

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Grocery");
        rvGrocery = view.findViewById(R.id.rv_grocery_list);

        adapter = new ItemAdapter(mGroceryList, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GroceryListItem mListItem = mGroceryList.get(position);
                mDatabaseRef.child(mListItem.getKey()).child("checked").setValue(mListItem.isChecked());
                Log.d("SecondFragment", "onItemClick" + mListItem.getKey() + ": isChecked " + mListItem.isChecked());
            }

            @Override
            public void onEditClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                mDatabaseRef.child(mGroceryList.get(position).getKey()).removeValue();
            }
        });

        rvGrocery.setAdapter(adapter);
        rvGrocery.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mGroceryList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    edit = prefs.edit();
                    GroceryListItem mItem = snapshot1.getValue(GroceryListItem.class);
                    mItem.setKey(snapshot1.getKey());
                    edit.putBoolean(mItem.getKey(), mItem.isChecked());
                    mGroceryList.add(mItem);
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
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDialog() {
        FragmentManager manager = getActivity().getFragmentManager();
        GroceryItemDialogFragment mydialog = new GroceryItemDialogFragment();
        mydialog.show(manager, "mydialog");
    }

    public int getRandomArrayEntry(int number) {
        Random rand = new Random();
        return rand.nextInt(number);
    }

    public void populateDatabase() {
//        mCount++;
        GroceryListItem mListItem = new GroceryListItem(names[getRandomArrayEntry(2)], foodArray[getRandomArrayEntry(10)]);
        mGroceryList.add(mListItem);
        String mId = mDatabaseRef.push().getKey();
        mDatabaseRef.child(mId).setValue(mListItem);
//        adapter.notifyDataSetChanged();
    }

    public void addCroceryItem(String item) {
        Log.d("SecondFragment:addGroceryItem()", "Item: " + item);
        GroceryListItem mListItem = new GroceryListItem(userName, item);
        mGroceryList.add(mListItem);
        String mId = mDatabaseRef.push().getKey();
        mDatabaseRef.child(mId).setValue(mListItem);
    }

    @Override
    public void messageItem(String item) {
        addCroceryItem(item);
    }
}