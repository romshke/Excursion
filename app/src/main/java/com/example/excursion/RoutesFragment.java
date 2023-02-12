package com.example.excursion;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.excursion.adapters.RoutesRecyclerViewAdapter;
import com.example.excursion.adapters.WaypointsAutoCompleteAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class RoutesFragment extends Fragment {

    private static final String TAG = "RoutesFragment";

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private ArrayList<Sight> sights, waypoints;
    private AutoCompleteTextView autoCompleteTextView;
    private RoutesRecyclerViewAdapter recyclerViewAdapter;
    private WaypointsAutoCompleteAdapter autoCompleteAdapter;
    private Button makeRouteButton, chooseRouteButton;

    public interface makeRouteInterface {
        void setWaypoints(ArrayList<Sight> waypoints);
    }

    makeRouteInterface makeRouteInterface;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity;
        activity = getActivity();
        try {
            makeRouteInterface = (makeRouteInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "какой-то текст");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_routes, container, false);

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

        makeRouteButton = view.findViewById(R.id.make_route_button);
        chooseRouteButton = view.findViewById(R.id.choose_route_button);
        sights = new ArrayList<>();
        waypoints = new ArrayList<>();
        storeData();

        autoCompleteTextView = view.findViewById(R.id.waypoint_autocomplete_text);
        autoCompleteAdapter = new WaypointsAutoCompleteAdapter(getContext(), sights);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);

        RecyclerView recyclerView = view.findViewById(R.id.routes_recyclerview);
        recyclerViewAdapter = new RoutesRecyclerViewAdapter(this.getContext(), waypoints);
        recyclerView.setAdapter(recyclerViewAdapter);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println(waypoints.size());
//                Toast.makeText(getContext(), String.valueOf(waypoints.size()), Toast.LENGTH_SHORT).show();
                if (waypoints.size() < 25) {
                    Sight sight = (Sight) autoCompleteTextView.getAdapter().getItem(position);
                    waypoints.add(sight);
                    recyclerViewAdapter.notifyDataSetChanged();
                    autoCompleteTextView.getText().clear();
                } else {
                    Toast.makeText(getContext(), "Введено максимальное количество точек маршрута", Toast.LENGTH_SHORT).show();
                }
            }
        });

        makeRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println(waypoints.size());
//                Toast.makeText(getContext(), String.valueOf(waypoints.size()), Toast.LENGTH_SHORT).show();
                if (waypoints.size() > 1 && waypoints.size() < 26) {
                    Log.d(TAG, "onClick: Button clicked");
                    if (getActivity().getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }

                    makeRouteInterface.setWaypoints(waypoints);
                    waypoints.clear();
                } else if (waypoints.size() < 2) {
                    Toast.makeText(getContext(), "Введите минимум 2 точки маршрута", Toast.LENGTH_SHORT).show();
                }
//                else if (waypoints.size() > 25) {
//                    Toast.makeText(getContext(), "Максимальное количество точек маршрута 25", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        chooseRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ChooseRouteFragment chooseRouteFragment = (ChooseRouteFragment) getActivity().getSupportFragmentManager().findFragmentByTag("chooseRouteFragment");
            getActivity().getSupportFragmentManager().beginTransaction()
                .show(chooseRouteFragment)
                .hide(requireActivity().getSupportFragmentManager().findFragmentByTag("routesFragment"))
                .addToBackStack(null)
                .commit();
            }
        });

        return view;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            waypoints.remove(viewHolder.getAdapterPosition());
            recyclerViewAdapter.notifyDataSetChanged();
        }
    };

    private void storeData() {
        Cursor cursorSights = databaseHelper.readAllSightsData();

        if (cursorSights.getCount() == 0) {
            Toast.makeText(this.getContext(), "No data.", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursorSights.moveToNext()) {
                sights.add(new Sight(
                        cursorSights.getInt(0),
                        cursorSights.getString(1),
                        cursorSights.getString(2),
                        cursorSights.getDouble(3),
                        cursorSights.getDouble(4),
                        cursorSights.getString(5),
                        cursorSights.getString(6),
                        cursorSights.getString(7)));
            }
        }

    }

}
