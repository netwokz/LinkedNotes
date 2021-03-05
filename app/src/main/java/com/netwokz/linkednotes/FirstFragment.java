package com.netwokz.linkednotes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstFragment extends Fragment {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    private TextView mRetrieveTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mDatabaseReference = mFirebaseDatabase.getReference("Grocery");
//        mRetrieveTV = view.findViewById(R.id.idTVRetriveData);

//        getData();
    }

    private void getData() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String id = snapshot1.child("id").getValue(String.class);
                    String active = snapshot1.child("active").getValue(String.class);
                    String item = snapshot1.child("item").getValue(String.class);
                    String person = snapshot1.child("person").getValue(String.class);
                    Log.d("First Fragment", "id: " + id + ", " + "active: " + active + ", " + "item: " + item + ", " + "person: " + person);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}