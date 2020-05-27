package com.example.excursion;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";

    private MapView mapView;
    private GoogleMap mMap;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private ArrayList<String> sightName;
    private ArrayList<Double> sightLatitude, sightLongitude;
    private List<MarkerOptions> markers;
    private Iterator iteratorName, iteratorLatitude, iteratorLongitude, iteratorPlaces, iteratorMarkers;
    private ArrayList<LatLng> places;
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



        sightName = new ArrayList<>();
        sightLatitude = new ArrayList<>();
        sightLongitude = new ArrayList<>();
        places = new ArrayList<>();
        markers = new ArrayList<>();

        storeDataInArrays();

        iteratorLatitude = sightLatitude.iterator();
        iteratorLongitude = sightLongitude.iterator();
        iteratorName = sightName.iterator();

        setLatLngs();
        iteratorPlaces = places.iterator();

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
        mMap = googleMap;


        while (iteratorMarkers.hasNext()) {
            mMap.addMarker((MarkerOptions) iteratorMarkers.next());
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.220496, 39.891523), 12));


        if(geoApiContext == null){
            geoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_api_key))
                    .build();
        }

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                markers.get(0).getPosition().latitude,
                markers.get(0).getPosition().longitude
        );


        DirectionsApiRequest directions = DirectionsApi.newRequest(geoApiContext);

        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        markers.get(4).getPosition().latitude,
                        markers.get(4).getPosition().longitude
                )
        );

        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "onResult: routes: " + result.routes[0].toString());
                Log.d(TAG, "onResult: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "onFailure: " + e.getMessage() );

            }
        });


        System.out.println(directions);
        System.out.println("asdsadsadsada");



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
//        //Преобразование итогового пути в набор точек
//                List<com.google.maps.model.LatLng> path = result.routes[0].overviewPolyline.decodePath();
//
//        //Линия которую будем рисовать
//                PolylineOptions line = new PolylineOptions();
//
//                LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
//
//        //Проходимся по всем точкам, добавляем их в Polyline и в LanLngBounds.Builder
//                for (int i = 0; i < path.size(); i++) {
//                    line.add(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
//                    latLngBuilder.include(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
//                }
//
//        //Делаем линию более менее симпатичное
//                line.width(16f).color(R.color.colorPrimary);
//
//        //Добавляем линию на карту
//                mMap.addPolyline(line);



    }

    private void setLatLngs() {
        while (iteratorLatitude.hasNext() && iteratorLongitude.hasNext()) {
            places.add(new LatLng((Double) iteratorLatitude.next(), (Double) iteratorLongitude.next()));
        }
    }

    private void setMarkers() {
        while (iteratorPlaces.hasNext()) {
            markers.add(new MarkerOptions().position((LatLng) iteratorPlaces.next()).title(iteratorName.next().toString()));
        }
    }

    private void storeDataInArrays() {
        Cursor cursor = databaseHelper.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this.getContext(), "No data.", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                sightName.add(cursor.getString(1));
                sightLatitude.add(cursor.getDouble(3));
                sightLongitude.add(cursor.getDouble(4));
            }
        }
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


}
