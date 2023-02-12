package com.example.excursion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class ChooseRouteFragment extends Fragment {

    Button text1, text2, text3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_route, container, false);

        text1 = view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);
        text3 = view.findViewById(R.id.text3);

        text1.setText("test test test");


        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "on click text", Toast.LENGTH_SHORT).show();
                System.out.println("println");
            }
        });

        return view;
    }

//    @Override
//    public void setWaypoints(ArrayList<Sight> waypoints) {
//        MapFragment mapFragment = (MapFragment) getActivity().getSupportFragmentManager().findFragmentByTag("mapFragment");
//        RoutesFragment routesFragment = (RoutesFragment) getActivity().getSupportFragmentManager().findFragmentByTag("routesFragment");
//
//        if (mapFragment != null) {
//            mapFragment.placeWaypoints(waypoints);
//        }
//
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .show(mapFragment)
//                .hide(routesFragment)
//                .commit();
//        getActivity().getSupportFragmentManager().beginTransaction().remove(this);
//    }
}
