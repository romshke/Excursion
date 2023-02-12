package com.example.excursion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RoutesFragment extends Fragment {

    private static final String TAG = "RoutesFragment";
    private Button button;
    private TextView textView;
    private MapView mapView;
    private GoogleMap googleMap;


    public interface addNewMarkerInterface {
        void addMarker(MarkerOptions s);
    }

    addNewMarkerInterface addNewMarkerInterface;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity;
        activity = getActivity();
        try {
            addNewMarkerInterface = (addNewMarkerInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "какой-то текст");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_routes, container, false);

        textView = view.findViewById(R.id.routes_text);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Button clicked");
                textView.setText("Кнопка нажата");
//                addNewMarkerInterface.addMarker(new MarkerOptions().position(new LatLng(59.220056, 39.885194
//                )).title("ЦУМ"));
            }
        });

        ((TextView) view.findViewById(R.id.routes_text)).setText(HtmlCompat.fromHtml("<p>This is the awesome place to gain</p><p><strong>awesomeness </strong>and <em>deliciuosness. </em>very<em> </em><u>nice</u></p><p><a href='http://qr35.ru/ru/obekty/O3510063004'>Колокольня софийского собора</a></p>", HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
        
        return view;
    }


}
