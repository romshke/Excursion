package com.example.excursion;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class PlacesFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private ArrayList<String> sightName, sightAddress;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);

        databaseHelper = new DatabaseHelper(this.getContext());

        try {
            databaseHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("Unable to update database");
        }

        try {
            database = databaseHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        sightName = new ArrayList<>();
        sightAddress = new ArrayList<>();

        storeDataInArrays();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.getContext(), sightName, sightAddress);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }

    private void storeDataInArrays() {
        Cursor cursor = databaseHelper.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this.getContext(), "No data.", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                sightName.add(cursor.getString(1));
                sightAddress.add(cursor.getString(2));
            }
        }
    }

}