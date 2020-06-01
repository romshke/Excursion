package com.example.excursion;

public class Sight {

    private Integer sightID;
    private String sightName, sightAddress, sightImagePath, sightInformation;
    private Double sightLatitude, sightLongitude;

    public Sight(Integer sightID, String sightName, String sightAddress, Double sightLatitude, Double sightLongitude, String sightImagePath) {
        this.sightID = sightID;
        this.sightName = sightName;
        this.sightAddress = sightAddress;
        this.sightLatitude = sightLatitude;
        this.sightLongitude = sightLongitude;
        this.sightImagePath = sightImagePath;
    }

    public Integer getSightID() {
        return sightID;
    }

    public String getSightName() {
        return sightName;
    }

    public String getSightAddress() {
        return sightAddress;
    }

    public Double getSightLatitude() {
        return sightLatitude;
    }

    public Double getSightLongitude() {
        return sightLongitude;
    }

    public String getSightImagePath() {
        return sightImagePath;
    }
}
