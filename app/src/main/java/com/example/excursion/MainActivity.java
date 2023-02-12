package com.example.excursion;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RoutesFragment.addNewMarkerInterface {

    private static final String TAG = "MainActivity";
    private SparseArray<Fragment.SavedState> savedStateSparseArray = new SparseArray<>();
//    Fragment mapFragment, placesFragment, routesFragment;
    private MapFragment mapFragment;
    private PlacesFragment placesFragment;
    private RoutesFragment routesFragment;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: started");

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        mapFragment = new MapFragment();
        placesFragment = new PlacesFragment();
        routesFragment = new RoutesFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mapFragment)
                .add(R.id.fragment_container, placesFragment).hide(placesFragment)
                .add(R.id.fragment_container, routesFragment).hide(routesFragment)
                .commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = new Fragment();
            List<Fragment> hiddenFragments = new ArrayList<>();

            switch (menuItem.getItemId()) {
                case R.id.nav_map:
//                    selectedFragment = new MapFragment();
                    selectedFragment = mapFragment;
                    hiddenFragments.add(placesFragment);
                    hiddenFragments.add(routesFragment);
                    break;
                case R.id.nav_places:
//                    selectedFragment = new PlacesFragment();
                    selectedFragment = placesFragment;
                    hiddenFragments.add(mapFragment);
                    hiddenFragments.add(routesFragment);
                    break;
                case R.id.nav_routes:
//                    selectedFragment = new RoutesFragment();
                    selectedFragment = routesFragment;
                    hiddenFragments.add(mapFragment);
                    hiddenFragments.add(placesFragment);
                    break;
            }

            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction()
                    .show(selectedFragment)
                    .hide(hiddenFragments.get(0))
                    .hide(hiddenFragments.get(1))
                    .commit();

            return  true;
        }
    };

    @Override
    public void addMarker(MarkerOptions s) {

        if (mapFragment != null) {
            mapFragment.placeMarker(s);
        }

        onNavigationItemSelectedListener.onNavigationItemSelected(bottomNavigationView.getMenu().findItem(R.id.nav_map).setChecked(true));

//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, map).commit();
//        ((TextView) map.getView().findViewById(R.id.maptext)).setText(s);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
//       ((TextView) getSupportFragmentManager().findFragmentById(R.id.map_fragment).getView().findViewById(R.id.maptext)).setText(s);
    }

}
