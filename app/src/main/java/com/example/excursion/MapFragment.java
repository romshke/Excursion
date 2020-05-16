package com.example.excursion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class MapFragment extends Fragment {

    private final String MAPKIT_API_KEY = "49c3c22c-61da-4fef-885c-2f8c701e4622";
    private final Point TARGET_LOCATION = new Point(59.945933, 30.320045);
    private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapview);
        mapView.getMap().move(
                new CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);
        return view;
    }

    @Override
    public void onStop() {
        // Activity onStop call must be passed to both MapView and MapKit instance.
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        // Activity onStart call must be passed to both MapView and MapKit instance.
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }
}
