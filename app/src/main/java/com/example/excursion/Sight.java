package com.example.excursion;

public class Sight {

    private final Integer sightID;
    private final String sightName, sightAddress,
            sightImagePath, sightDetails, sightSourceLink;
    private final Double sightLatitude, sightLongitude;

    public Sight(Integer sightID, String sightName, String sightAddress,
                 Double sightLatitude, Double sightLongitude, String sightImagePath,
                 String sightDetails, String sightSourceLink) {
        this.sightID = sightID;
        this.sightName = sightName;
        this.sightAddress = sightAddress;
        this.sightLatitude = sightLatitude;
        this.sightLongitude = sightLongitude;
        this.sightImagePath = sightImagePath;
        this.sightDetails = sightDetails;
        this.sightSourceLink = sightSourceLink;
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

    public String getSightDetails() {
        return sightDetails;
    }

    public String getSightSourceLink() {
        return sightSourceLink;
    }
}


