package io.deltawave.poke.pokemongo.controller;

import io.deltawave.poke.pokemongo.service.Services;

/**
 * Created by will on 8/4/16.
 */
public class Controllers {

    private static Controllers instance;

    private Services services;
    private WorldController worldController;
    private LocationController locationController;

    public static Controllers getInstance(Services services) {
        if(instance == null) {
            instance = new Controllers(services);
        }
        return instance;
    }

    private Controllers(Services services) {
        this.services = services;
        this.worldController = new WorldController(services);
        this.locationController = new LocationController(services);
    }

    public WorldController getWorldController() {
        return worldController;
    }

    public LocationController getLocationController() {
        return locationController;
    }
}
