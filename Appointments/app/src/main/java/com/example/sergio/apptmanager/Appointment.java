package com.example.sergio.apptmanager;

/**
 * Created by Sergio on 10/10/2017.
 */

public class Appointment {
    public String appId;
    public String locationName;
    public String startTime;
    public String address;
    public String transportationType;
    public String transportationProvider;
    public Boolean wheelchair;
    public String doctorName;


    public Appointment(String appId, String locationName, String startTime, String address, String transportationType, String transportationProvider, Boolean wheelchair, String doctorName) {
        this.appId = appId;
        this.locationName = locationName;
        this.startTime = startTime;
        this.address = address;
        this.transportationType = transportationType;
        this.transportationProvider = transportationProvider;
        this.wheelchair = wheelchair;
        this.doctorName = doctorName;
    }

public Appointment(){};

public String toString()
{
    return this.appId + ";" + this.locationName + ";" +this.startTime + ";" + this.address + ";"+ this.transportationType + ";" + this.transportationProvider + ";" + this.wheelchair + ";" +this.doctorName;
}

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(String transportationType) {
        this.transportationType = transportationType;
    }

    public String getTransportationProvider() {
        return transportationProvider;
    }

    public void setTransportationProvider(String transportationProvider) {
        this.transportationProvider = transportationProvider;
    }

    public Boolean getWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(Boolean wheelchair) {
        this.wheelchair = wheelchair;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
