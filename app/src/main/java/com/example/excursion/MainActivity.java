package com.example.excursion;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RoutesFragment.makeRouteInterface {

    private static final String TAG = "MainActivity";
    private SparseArray<Fragment.SavedState> savedStateSparseArray = new SparseArray<>();
    private MapFragment mapFragment;
    private PlacesFragment placesFragment;
    private RoutesFragment routesFragment;
    private ChooseRouteFragment chooseRouteFragment;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        mapFragment = new MapFragment();
        placesFragment = new PlacesFragment();
        routesFragment = new RoutesFragment();
        chooseRouteFragment = new ChooseRouteFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mapFragment, "mapFragment")
                .add(R.id.fragment_container, placesFragment, "placesFragment").hide(placesFragment)
                .add(R.id.fragment_container, routesFragment, "routesFragment").hide(routesFragment)
                .add(R.id.fragment_container, chooseRouteFragment, "chooseRouteFragment").hide(chooseRouteFragment)
                .commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = new Fragment();
            List<Fragment> hiddenFragments = new ArrayList<>();

            switch (menuItem.getItemId()) {
                case R.id.nav_map:
                    selectedFragment = mapFragment;
                    hiddenFragments.add(placesFragment);
                    hiddenFragments.add(routesFragment);
                    hiddenFragments.add(chooseRouteFragment);
                    break;
                case R.id.nav_places:
                    selectedFragment = placesFragment;
                    hiddenFragments.add(mapFragment);
                    hiddenFragments.add(routesFragment);
                    hiddenFragments.add(chooseRouteFragment);
                    break;
                case R.id.nav_routes:
                    selectedFragment = routesFragment;
                    hiddenFragments.add(mapFragment);
                    hiddenFragments.add(placesFragment);
                    hiddenFragments.add(chooseRouteFragment);
                    break;
            }

            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction()
                    .show(selectedFragment)
                    .hide(hiddenFragments.get(0))
                    .hide(hiddenFragments.get(1))
                    .hide(hiddenFragments.get(2))
                    .commit();

            return  true;
        }
    };

    @Override
    public void setWaypoints(ArrayList<Sight> waypoints) {
        if (mapFragment != null) {
            mapFragment.placeWaypoints(waypoints);
        }
        onNavigationItemSelectedListener.onNavigationItemSelected(bottomNavigationView.getMenu().findItem(R.id.nav_map).setChecked(true));
        getSupportFragmentManager().beginTransaction()
                .show(mapFragment)
                .hide(routesFragment)
                .hide(chooseRouteFragment)
                .commit();
    }

}
