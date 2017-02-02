package io.deltawave.poke.pokemongo.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import io.deltawave.poke.pokemongo.poke.Poke;
import io.deltawave.poke.pokemongo.poke.PokeLocationListener;
import io.deltawave.poke.pokemongo.poke.PokeTrace;
import io.deltawave.poke.pokemongo.service.LocationService;
import io.deltawave.poke.pokemongo.service.Services;
import io.deltawave.poke.pokemongo.service.WorldService;
import io.deltawave.poke.pokemongo.service.client.NearbyPokesRequest;
import io.deltawave.poke.pokemongo.service.client.NearbyPokesResponse;
import io.deltawave.poke.pokemongo.service.client.Request;
import io.deltawave.poke.pokemongo.service.client.Response;
import io.deltawave.poke.pokemongo.service.client.ServerConnectionService;
import io.deltawave.poke.pokemongo.service.client.ServerResponseListener;

/**
 * Created by will on 8/4/16.
 */
public class WorldController implements ServerResponseListener {

    private Context context;
    private ServerConnectionService serverConnectionService;
    private WorldService worldService;
    private LocationService locationService;

    private ArrayList<PokeLocationListener> pokeLocationListeners;

    private HashSet<Poke> nearPokes;
    private HashSet<PokeTrace> farPokes;

    public WorldController(Services services) {
        context = services.getContext();
        serverConnectionService = services.getServerConnectionService();
        worldService = services.getWorldService();
        locationService = services.getLocationService();

        pokeLocationListeners = new ArrayList<>();

        nearPokes = new HashSet<>();
        farPokes = new HashSet<>();

        registerPokeClasses();
        startPokeLocationRequests();

        serverConnectionService.addServerResponseListener(this);
    }

    public void registerPokeClasses() {
        serverConnectionService.registerClass(Request.class);
        serverConnectionService.registerClass(Response.class);
        serverConnectionService.registerClass(NearbyPokesRequest.class);
        serverConnectionService.registerClass(NearbyPokesResponse.class);
        serverConnectionService.registerClass(HashSet.class);
        serverConnectionService.registerClass(Poke.class);
        serverConnectionService.registerClass(PokeTrace.class);
    }

    public void startPokeLocationRequests() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(serverConnectionService.isConnected()) {
                    NearbyPokesRequest npr = new NearbyPokesRequest();
                    npr.latitude = locationService.getLatitude();
                    npr.longitude = locationService.getLongitude();
                    serverConnectionService.sendTCP(npr);
                }
            }
        }, 0, 5000);

    }

    public void addPokeLocationListener(PokeLocationListener pll) {
        pokeLocationListeners.add(pll);
    }

    public void removePokeLocationListener(PokeLocationListener pll) {
        pokeLocationListeners.remove(pll);
    }

    @Override
    public void onReceivedResponse(Object o) {
        if(o instanceof NearbyPokesResponse) {
            NearbyPokesResponse nprspns = (NearbyPokesResponse) o;

            nearPokes = nprspns.nearPokesSet;
            farPokes = nprspns.farPokesSet;

            for(PokeLocationListener pll: pokeLocationListeners) {
                pll.onPokeLocationsUpdated(nprspns.nearPokesSet, nprspns.farPokesSet);
            }
        }
    }

    public HashSet<Poke> getNearPokes() {
        return nearPokes;
    }

    public HashSet<PokeTrace> getFarPokes() {
        return farPokes;
    }
}
