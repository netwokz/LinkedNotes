package com.netwokz.linkednotes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
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
        mGroceryList = new ArrayList<>();
        RecyclerView rvGrocery = view.findViewById(R.id.rv_grocery_list);

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
//                    String[] oldList = new String[Math.toIntExact(task.getResult().getChildrenCount())];
                    ArrayList<String[]> oldDBList = new ArrayList<>();
                    for (int i = 1; i <= task.getResult().getChildrenCount(); i++) {
//                        String str[] = Arrays.stream(task.getResult().child(String.valueOf(i)).getValue())
//                        oldDBList.add(Arrays.stream(task.getResult().child(String.valueOf(i)).getValue()))
                        Log.d("firebase", task.getResult().child(String.valueOf(i)).getValue().toString());
                    }
                    Log.d("firebase", String.valueOf(task.getResult().getChildrenCount()));
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });

//        mGroceryList = generateDemoList(20);
//        mDatabase.setValue(mGroceryList);
//        populateDatabase(10);
        ItemAdapter adapter = new ItemAdapter(mGroceryList);
        rvGrocery.setAdapter(adapter);
        rvGrocery.setLayoutManager(new LinearLayoutManager(getContext()));
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

    public ArrayList<GroceryListItem> generateDemoList(int numOfEntries) {
        ArrayList<GroceryListItem> mList = new ArrayList<>();
        for (int i = 0; i < numOfEntries; i++) {
            GroceryListItem mListItem = new GroceryListItem(String.valueOf(i + 1), names[getRandomArrayEntry(2)], foodArray[getRandomArrayEntry(10)], "false");
            mList.add(mListItem);
        }
        return mList;
    }
}