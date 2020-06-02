package com.example.excursion;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";

    private MapView mapView;
    private GoogleMap googleMap;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private ArrayList<Sight> sights;
    private List<MarkerOptions> markers;
    private Iterator iteratorMarkers, iteratorSights;
    private List<com.google.maps.model.LatLng> path;
    private GeoApiContext geoApiContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

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

        sights = new ArrayList<>();
        markers = new ArrayList<>();

        storeData();
        iteratorSights = sights.iterator();

        setMarkers();
        iteratorMarkers = markers.iterator();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        while (iteratorMarkers.hasNext()) {
            googleMap.addMarker((MarkerOptions) iteratorMarkers.next());
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.220496, 39.891523), 12));

        if(geoApiContext == null){
            geoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_api_key))
                    .build();
        }

    }

    private void setMarkers() {
        for (Sight sight: sights) {
            markers.add(new MarkerOptions().position(new LatLng(sight.getSightLatitude(), sight.getSightLongitude())).title(sight.getSightName()));
        }
    }

    private void storeData() {
        Cursor cursor = databaseHelper.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this.getContext(), "No data.", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                sights.add(new Sight(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
            }
        }
    }

    public void placeMarker(MarkerOptions markerOptions) {
        if (googleMap != null) {
            googleMap.addMarker(markerOptions);
            createDirection();
//            LatLng marker = new LatLng(lat, lon);
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15));
//            googleMap.addMarker(new MarkerOptions().title(title).position(marker));
        }
    }

    public void createDirection() {
        com.google.maps.model.LatLng[] ways = {new com.google.maps.model.LatLng(59.222759, 39.887372), new com.google.maps.model.LatLng(59.222449, 39.901095), new com.google.maps.model.LatLng(59.220056, 39.885194)};

//        ways.add(new com.google.maps.model.LatLng(59.222759, 39.887372));
//        ways.add(new com.google.maps.model.LatLng(59.222449, 39.901095));
//        System.out.println(ways.get(0).toString());
        DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.WALKING)
                .alternatives(true)
                .origin(new com.google.maps.model.LatLng(59.224021, 39.882833))
//                .waypoints(new com.google.maps.model.LatLng(59.222759, 39.887372), new com.google.maps.model.LatLng(59.222449, 39.901095))
//                .optimizeWaypoints(true)
                .waypoints(ways)
                .destination(new com.google.maps.model.LatLng(59.212828, 39.893248))
                .awaitIgnoreError();

        //Преобразование итогового пути в набор точек
        List<com.google.maps.model.LatLng> path = result.routes[0].overviewPolyline.decodePath();

        //Линия которую будем рисовать
        PolylineOptions line = new PolylineOptions();

        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

        //Проходимся по всем точкам, добавляем их в Polyline и в LanLngBounds.Builder
        for (int i = 0; i < path.size(); i++) {
            line.add(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
            latLngBuilder.include(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
        }

        //Делаем линию более менее симпатичное
        line.width(10f).color(R.color.colorAccent);

        //Добавляем линию на карту
        googleMap.addPolyline(line);
    }







//    private void calculateDirections(Marker marker){
//        Log.d(TAG, "calculateDirections: calculating directions.");
//
//        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
//                marker.getPosition().latitude,
//                marker.getPosition().longitude
//        );
//        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);
//
//        directions.alternatives(true);
//        directions.origin(
//                new com.google.maps.model.LatLng(
//                        mUserPosition.getGeo_point().getLatitude(),
//                        mUserPosition.getGeo_point().getLongitude()
//                )
//        );
//        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
//        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
//            @Override
//            public void onResult(DirectionsResult result) {
//                Log.d(TAG, "onResult: routes: " + result.routes[0].toString());
//                Log.d(TAG, "onResult: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
//            }
//
//            @Override
//            public void onFailure(Throwable e) {
//                Log.e(TAG, "onFailure: " + e.getMessage() );
//
//            }
//        });
//    }


    //        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
//                markers.get(0).getPosition().latitude,
//                markers.get(0).getPosition().longitude
//        );
//
//
//        DirectionsApiRequest directions = DirectionsApi.newRequest(geoApiContext);
//
//        directions.alternatives(true);
//        directions.origin(
//                new com.google.maps.model.LatLng(
//                        markers.get(4).getPosition().latitude,
//                        markers.get(4).getPosition().longitude
//                )
//        );
//
//        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
//            @Override
//            public void onResult(DirectionsResult result) {
//                Log.d(TAG, "onResult: routes: " + result.routes[0].toString());
//                Log.d(TAG, "onResult: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
//            }
//
//            @Override
//            public void onFailure(Throwable e) {
//                Log.e(TAG, "onFailure: " + e.getMessage() );
//
//            }
//        });

//        //Здесь будет наш итоговый путь состоящий из набора точек
//                DirectionsResult  result = null;
//                try {
//                    result = DirectionsApi.newRequest(geoApiContext)
//                            .origin(String.valueOf(places.get(0)))//Место старта
//                            .destination(String.valueOf(places.get(places.size() - 1))).await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (com.google.maps.errors.ApiException e) {
//                    e.printStackTrace();
//                }
//

}
