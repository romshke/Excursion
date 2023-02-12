package com.example.excursion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.excursion.R;
import com.example.excursion.Sight;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Objects;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View window;
    private Context context;

    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
        window = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindow(Marker marker, View view) {
        ((TextView) view.findViewById(R.id.custom_info_window_sight_name)).setText(((Sight) marker.getTag()).getSightName());
        ((TextView) view.findViewById(R.id.custom_info_window_sight_address)).setText(((Sight) marker.getTag()).getSightAddress());
        ((ImageView) view.findViewById(R.id.custom_info_window_sight_image)).setImageResource(context.getResources().getIdentifier(((Sight) marker.getTag()).getSightImagePath(), null, Objects.requireNonNull(context).getPackageName()));
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindow(marker, window);
        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindow(marker, window);
        return window;
    }
}
