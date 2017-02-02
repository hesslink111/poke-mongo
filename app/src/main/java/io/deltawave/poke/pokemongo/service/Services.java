package io.deltawave.poke.pokemongo.service;

import android.content.Context;

import io.deltawave.poke.pokemongo.service.client.ServerConnectionService;

/**
 * Created by will on 8/3/16.
 */
public class Services {

    private static Services instance;

    private Context context;
    private WorldService worldService;
    private ServerConnectionService serverConnectionService;
    private LocationService locationService;

    public static Services getInstance(Context context) {
        if(instance == null) {
            instance = new Services(context);
        }
        return instance;
    }

    private Services(Context context) {
        this.context = context;
        this.worldService = new WorldService();
        this.serverConnectionService = new ServerConnectionService();
        this.locationService = new LocationService(context);
    }

    public WorldService getWorldService() {
        return worldService;
    }

    public ServerConnectionService getServerConnectionService() {
        return serverConnectionService;
    }

    public LocationService getLocationService() {
        return locationService;
    }

    public Context getContext() {
        return context;
    }
}
