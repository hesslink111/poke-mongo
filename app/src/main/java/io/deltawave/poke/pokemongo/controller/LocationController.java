package io.deltawave.poke.pokemongo.controller;

import io.deltawave.poke.pokemongo.service.LatLngListener;
import io.deltawave.poke.pokemongo.service.LocationService;
import io.deltawave.poke.pokemongo.service.Services;

/**
 * Created by will on 8/4/16.
 */
public class LocationController {

    private LocationService locationService;

    public LocationController(Services services) {
        locationService = services.getLocationService();
    }

    public void addLatLngListener(LatLngListener latLngListener) {
        locationService.addLatLngListener(latLngListener);
    }

    public void removeLatLngListener(LatLngListener latLngListener) {
        locationService.removeLatLngListener(latLngListener);
    }
}
