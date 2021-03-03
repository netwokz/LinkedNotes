package com.netwokz.linkednotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class SecondFragment extends Fragment {

    ArrayList<GroceryListItem> mGroceryList;
    String[] foodArray = {"Bread", "Milk", "Beer", "Cheese", "Paper Towels", "Plates", "Cereal", "Water", "Steak", "Hot Dogs"};
    String[] names = {"Steve", "Taylor"};

    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference("Grocery/");

        RecyclerView rvGrocery = view.findViewById(R.id.rv_grocery_list);

        mGroceryList = generateDemoList(20);

        mDatabase.setValue(mGroceryList);
        ItemAdapter adapter = new ItemAdapter(mGroceryList);
        rvGrocery.setAdapter(adapter);
        rvGrocery.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public int getRandomArrayEntry(int number) {
        Random rand = new Random();
        return rand.nextInt(number);
    }

    public ArrayList<GroceryListItem> generateDemoList(int numOfEntries) {
        ArrayList<GroceryListItem> mList = new ArrayList<>();
        for (int i = 0; i < numOfEntries; i++) {
            GroceryListItem mListItem = new GroceryListItem(names[getRandomArrayEntry(2)], foodArray[getRandomArrayEntry(10)], false);
            mList.add(mListItem);
        }
        return mList;
    }
}