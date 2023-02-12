package com.example.excursion;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.excursion.adapters.CustomInfoWindowAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapFragment";

    private MapView mapView;
    private GoogleMap googleMap;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private ArrayList<Sight> sights;
    private ArrayList<Marker> sightsMarkers, waypointsMarkers;
    private List<com.google.maps.model.LatLng> path;
    private GeoApiContext geoApiContext;
    private Button closeRoute;
    Polyline routeLine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        path = new ArrayList<>();
        sightsMarkers = new ArrayList<>();
        waypointsMarkers = new ArrayList<>();
        closeRoute = view.findViewById(R.id.close_route_button);

        storeData();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(59.220496, 39.891523), 12));

        if(geoApiContext == null){
            geoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_api_key))
                    .build();
        }

        setSightsMarkers(sights);

    }

    private void storeData() {
        Cursor cursor = databaseHelper.readAllSightsData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this.getContext(), "No data.", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                sights.add(new Sight(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)));
            }
        }
    }

    private void setCustomInfoWindow() {
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));
        googleMap.setOnInfoWindowClickListener(this);

    }

    private void setSightsMarkers(ArrayList<Sight> sights) {
        googleMap.clear();
        for (Sight sight: sights) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(sight.getSightLatitude(),
                    sight.getSightLongitude())).title(sight.getSightName()));
            marker.setTag(sight);
            sightsMarkers.add(marker);
        }
        setCustomInfoWindow();
    }

    private void setWaypointsMarkers(ArrayList<Sight> waypoints) {
        googleMap.clear();
        for (Sight waypoint: waypoints) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(waypoint.getSightLatitude(),
                    waypoint.getSightLongitude())).title(waypoint.getSightName()));
            marker.setTag(waypoint);
            waypointsMarkers.add(marker);
        }
        setCustomInfoWindow();
    }



    public void placeWaypoints(final ArrayList<Sight> waypoints) {
        clearRoute();
        setWaypointsMarkers(waypoints);
        createDirection(convertLatLngListToArray(getPointsArrayList(waypointsMarkers)));
        Toast.makeText(getContext(), String.valueOf(path), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), String.valueOf(routeLine), Toast.LENGTH_SHORT).show();
        if (path.size() != 0) {
            closeRoute.setVisibility(View.VISIBLE);
            closeRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSightsMarkers(sights);
                    waypointsMarkers.clear();
                    routeLine.remove();
                    closeRoute.setVisibility(View.GONE);
                }
            });
        } else {
            setSightsMarkers(sights);
        }
    }

    private ArrayList<com.google.maps.model.LatLng> getPointsArrayList(ArrayList<Marker> markers) {
        ArrayList<com.google.maps.model.LatLng> points = new ArrayList<>();

        for (Marker marker : markers) {
            points.add(new com.google.maps.model.LatLng(marker.getPosition().latitude,
                    marker.getPosition().longitude));
        }

        return points;
    }

    private com.google.maps.model.LatLng[]
    convertLatLngListToArray(ArrayList<com.google.maps.model.LatLng> list){
        com.google.maps.model.LatLng[] array =
                list.toArray(new com.google.maps.model.LatLng[list.size()]);

        return array;
    }

    private void clearRoute() {
        path.clear();
        if (routeLine != null) {
            routeLine.remove();
        }
    }

    public void createDirection(com.google.maps.model.LatLng[] pointsArray) {
        if (pointsArray.length > 1) {
            com.google.maps.model.LatLng origin =
                    new com.google.maps.model.LatLng(pointsArray[0].lat, pointsArray[0].lng);
            com.google.maps.model.LatLng destination =
                    new com.google.maps.model.LatLng(pointsArray[pointsArray.length - 1].lat,
                            pointsArray[pointsArray.length - 1].lng);
            com.google.maps.model.LatLng[] waypoints =
                    Arrays.copyOfRange(pointsArray, 1, pointsArray.length - 1);

            DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                    .mode(TravelMode.WALKING)
                    .alternatives(true)
                    .origin(origin)
                    .optimizeWaypoints(true)
                    .waypoints(waypoints)
                    .destination(destination)
                    .awaitIgnoreError();

            clearRoute();

            if (result != null) {

                //Преобразование итогового пути в набор точек
//                List<com.google.maps.model.LatLng>

                path = result.routes[0].overviewPolyline.decodePath();

                //Линия которую будем рисовать
                PolylineOptions polylineOptions = new PolylineOptions();

                LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

                //Проходимся по всем точкам, добавляем их в Polyline и в LanLngBounds.Builder
                for (int i = 0; i < path.size(); i++) {
                    polylineOptions.add(new com.google.android.gms.maps.model
                            .LatLng(path.get(i).lat, path.get(i).lng));
                    latLngBuilder.include(new com.google.android.gms.maps.model
                            .LatLng(path.get(i).lat, path.get(i).lng));
                }

                //Делаем линию более менее симпатичное
                polylineOptions.width(10f).color(R.color.violet);

                //Добавляем линию на карту
                routeLine = googleMap.addPolyline(polylineOptions);
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getContext(), PlaceDetailActivity.class);
        intent.putExtra("sightName", ((Sight) marker.getTag()).getSightName());
        intent.putExtra("sightAddress", ((Sight) marker.getTag()).getSightAddress());
        intent.putExtra("sightImagePath", ((Sight) marker.getTag()).getSightImagePath());
        intent.putExtra("sightDetails", ((Sight) marker.getTag()).getSightDetails());
        intent.putExtra("sightSourceLink", ((Sight) marker.getTag()).getSightSourceLink());
        getContext().startActivity(intent);
    }

}
