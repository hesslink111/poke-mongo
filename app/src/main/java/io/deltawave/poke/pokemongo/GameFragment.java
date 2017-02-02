package io.deltawave.poke.pokemongo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import io.deltawave.poke.pokemongo.controller.Controllers;
import io.deltawave.poke.pokemongo.controller.LocationController;
import io.deltawave.poke.pokemongo.controller.WorldController;
import io.deltawave.poke.pokemongo.bitmap.ImageLoader;
import io.deltawave.poke.pokemongo.poke.PokeTrace;
import io.deltawave.poke.pokemongo.service.LatLngListener;
import io.deltawave.poke.pokemongo.service.Services;
import io.deltawave.poke.pokemongo.poke.Poke;
import io.deltawave.poke.pokemongo.poke.PokeLocationListener;


public class GameFragment extends Fragment implements PokeLocationListener, LatLngListener {

    private MapView gmapView;
    private GoogleMap mMap;

    private GroundOverlay player;
    private Bitmap[] playerImages;

    private HashSet<Poke> pokesSet;
    private HashMap<Poke, GroundOverlay> pokeGroundOverlayMap;

    private Services services;
    private Controllers controllers;
    private WorldController worldController;
    private LocationController locationController;

    public GameFragment() {
        // Required empty public constructor
    }

    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        services = Services.getInstance(getContext());
        controllers = Controllers.getInstance(services);

        worldController = controllers.getWorldController();
        worldController.addPokeLocationListener(this);

        locationController = controllers.getLocationController();
        locationController.addLatLngListener(this);

        pokesSet = new HashSet<>();
        pokeGroundOverlayMap = new HashMap<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_game, container, false);

        Button button = (Button) v.findViewById(R.id.tracking_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), TrackingActivity.class);
                startActivity(i);
            }
        });


        gmapView = (MapView) v.findViewById(R.id.map);
        gmapView.onCreate(savedInstanceState);

        gmapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                showMap();
            }
        });

        return v;
    }


    public void showMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        TileOverlayOptions options = new TileOverlayOptions();
        options.tileProvider(new UrlTileProvider(512, 512) {
            @Override
            public URL getTileUrl(int x, int y, int z) {
                try {
                    //String f = "http://tile.stamen.com/watercolor/%d/%d/%d.jpg";
                    String f = "https://api.mapbox.com/styles/v1/hesslink111/cire4ftb7000zganejogw4483/tiles/512/%d/%d/%d?access_token=pk.eyJ1IjoiaGVzc2xpbmsxMTEiLCJhIjoiY2lyZTRldW8yMDAyaWc1bmtjaGc0b3JzYyJ9.Xj3N0kKSuHUw0QofHbA8zA";
                    return new URL(String.format(f, z, x, y));
                }
                catch (MalformedURLException e) {
                    return null;
                }
            }
        });

        mMap.getUiSettings().setCompassEnabled(true);

        mMap.addTileOverlay(options);
        mMap.getUiSettings().setAllGesturesEnabled(false);

        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);

        final LatLng PORTLAND = new LatLng(43.6666,-70.2666);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(PORTLAND)
                .zoom(19)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        final LatLng HOUSE = new LatLng(43.666666666666, -70.28316);

        playerImages = ImageLoader.getCharacterImages(getContext(), "birch");
        player = mMap.addGroundOverlay(new GroundOverlayOptions()
                .position(HOUSE, 5)
                .zIndex(10)
                .image(BitmapDescriptorFactory.fromBitmap(playerImages[ImageLoader.DOWN_0]))
        );

        mMap.setOnGroundOverlayClickListener(new GoogleMap.OnGroundOverlayClickListener() {
            @Override
            public void onGroundOverlayClick(GroundOverlay groundOverlay) {
                //Intent startCapture = new Intent(getContext(), CaptureActivity.class);

                //if(groundOverlay.equals(bulbasaur)) {
                //    startCapture.putExtra("poke", "bulbasaur");
                //    startActivity(startCapture);

                //} else if(groundOverlay.equals(charmander)) {
                //    startCapture.putExtra("poke", "charmander");
                //    startActivity(startCapture);

                //} else if(groundOverlay.equals(eevee)) {

                //    startCapture.putExtra("poke", "eevee");
                //    startActivity(startCapture);
                //}

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        if(gmapView != null) {
            gmapView.onResume();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(gmapView != null) {
            gmapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(gmapView != null) {
            gmapView.onLowMemory();
        }
    }

    @Override
    public void onPokeLocationsUpdated(HashSet<Poke> updatedNearPokesSet, HashSet<PokeTrace> updatedFarPokesSet) {
        if(mMap == null) {
            return;
        }

        if(!GameFragment.this.isVisible()) {
            return;
        }

        final ArrayList<Poke> removedPokes = new ArrayList<>();
        removedPokes.addAll(pokesSet);
        removedPokes.removeAll(updatedNearPokesSet);

        final ArrayList<Poke> newPokes = new ArrayList<>();
        newPokes.addAll(updatedNearPokesSet);
        newPokes.removeAll(pokesSet);


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(Poke poke: removedPokes) {
                    hidePoke(poke);
                    pokesSet.remove(poke);
                }

                for(Poke poke: newPokes) {
                    displayPoke(poke);
                    pokesSet.add(poke);
                }
            }
        });
    }

    public void displayPoke(Poke poke) {
        if(mMap == null) {
            return;
        }

        GroundOverlay pokeOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                .position(new LatLng(poke.getLatitude(), poke.getLongitude()), 12)
                .image(BitmapDescriptorFactory.fromBitmap(ImageLoader.getPokeBitmap4x(
                        getContext(),
                        poke.type,
                        ImageLoader.ORIENTATION_FRONT,
                        ImageLoader.TYPE_NORMAL)))
                .zIndex(11)
                .clickable(true)
        );
        pokeGroundOverlayMap.put(poke, pokeOverlay);
    }

    public void hidePoke(Poke poke) {
        if(mMap == null) {
            return;
        }

        GroundOverlay groundOverlay = pokeGroundOverlayMap.get(poke);
        groundOverlay.remove();
        pokeGroundOverlayMap.remove(poke);
    }

    @Override
    public void onLatLngChanged(final LatLng latLng) {

        if(gmapView != null && player != null) {
            animateOverlay(player, player.getPosition(), latLng, 1800);
            CameraPosition cp = CameraPosition.builder(mMap.getCameraPosition()).zoom(19.5f).target(latLng).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp), 1000, null);
        }
    }

    public void animateOverlay(final GroundOverlay go, final LatLng oldPos, final LatLng newPos, final int time) {

        double latDifference = newPos.latitude-oldPos.latitude;
        double longDifference = newPos.longitude-oldPos.longitude;
        double angle = Math.atan2(latDifference, longDifference);

        final int orientation;
        if(angle < -Math.PI*3/4 || angle > Math.PI*3/4) {
            orientation = ImageLoader.LEFT_0;
        } else if(angle < -Math.PI/2) {
            orientation = ImageLoader.DOWN_0;
        } else if(angle > Math.PI/2) {
            orientation = ImageLoader.UP_0;
        } else {
            orientation = ImageLoader.RIGHT_0;
        }
        go.setImage(BitmapDescriptorFactory.fromBitmap(playerImages[orientation]));

        final boolean shouldRun;
        double distance = Math.sqrt(latDifference*latDifference + longDifference*longDifference);
        if(distance > 0.0001) {
            shouldRun = true;
        } else {
            shouldRun = false;
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int timeLeft = time;
            int currentImg = 0;

            @Override
            public void run() {
                timeLeft -= 20;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        go.setPosition(new LatLng(
                                oldPos.latitude + (newPos.latitude - oldPos.latitude) * (time - timeLeft) / time,
                                oldPos.longitude + (newPos.longitude - oldPos.longitude) * (time - timeLeft) / time
                        ));
                        if (timeLeft % 300 == 0 && shouldRun) {
                            currentImg++;
                            currentImg %= 4;
                            go.setImage(BitmapDescriptorFactory.fromBitmap(playerImages[orientation + currentImg]));
                        }
                        if (timeLeft <= 0) {
                            go.setPosition(newPos);
                            go.setImage(BitmapDescriptorFactory.fromBitmap(playerImages[orientation]));
                        }
                    }
                });
                if (timeLeft < 0) {
                    this.cancel();
                }
            }
        }, 0, 20);
    }
}
