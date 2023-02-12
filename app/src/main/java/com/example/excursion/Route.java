package com.example.excursion;

import java.util.ArrayList;

public class Route {

    private Integer routeID;
    private ArrayList<Sight> sights;

    public Route(Integer routeID, ArrayList<Sight> sights) {
        this.routeID = routeID;
        this.sights = sights;
    }

    public Integer getRouteID() {
        return routeID;
    }

    public ArrayList<Sight> getSights() {
        return sights;
    }
}
