package io.deltawave.poke.pokemongo.service;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by will on 8/4/16.
 */
public class LocationService implements LocationListener {

    private Context context;
    private LocationManager locationManager;

    private double latitude;
    private double longitude;

    private ArrayList<LatLngListener> latLngListeners;

    private Location mostRecentLocation;

    public LocationService(Context context) {
        this.context = context;

        latLngListeners = new ArrayList<>();

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2500, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
        } catch (SecurityException ex) {
            System.out.println("Did not get permission to check location");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Got location: " + location.getLatitude() + ", " + location.getLongitude());

        Location useLocation;
        if(mostRecentLocation == null || location.getAccuracy() < mostRecentLocation.getAccuracy()) {
            useLocation = location;
        } else {
            useLocation = mostRecentLocation;
        }

        latitude = useLocation.getLatitude();
        longitude = useLocation.getLongitude();

        for(LatLngListener lll: latLngListeners) {
            lll.onLatLngChanged(new LatLng(latitude, longitude));
        }
        
        mostRecentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void addLatLngListener(LatLngListener latLngListener) {
        latLngListeners.add(latLngListener);
    }

    public void removeLatLngListener(LatLngListener latLngListener) {
        latLngListeners.remove(latLngListener);
    }
}
