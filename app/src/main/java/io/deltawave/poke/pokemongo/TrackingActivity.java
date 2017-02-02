package io.deltawave.poke.pokemongo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import io.deltawave.poke.pokemongo.controller.Controllers;
import io.deltawave.poke.pokemongo.controller.WorldController;
import io.deltawave.poke.pokemongo.poke.Poke;
import io.deltawave.poke.pokemongo.bitmap.ImageLoader;
import io.deltawave.poke.pokemongo.poke.PokeLocationListener;
import io.deltawave.poke.pokemongo.poke.PokeTrace;
import io.deltawave.poke.pokemongo.service.Services;

public class TrackingActivity extends AppCompatActivity implements PokeLocationListener {

    private ListView trackingListView;
    private TextView noPokesTextView;

    private ArrayList<PokeTrace> pokeTraceList;
    private ArrayAdapter<PokeTrace> trackingAdapter;

    private Services services;
    private Controllers controllers;
    private WorldController worldController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        trackingListView = (ListView) findViewById(R.id.tracking_list);
        noPokesTextView = (TextView) findViewById(R.id.tracking_no_pokes);

        pokeTraceList = new ArrayList<>();
        trackingAdapter = new ArrayAdapter<PokeTrace>(this, R.layout.tracking_list_item, pokeTraceList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.tracking_list_item, parent, false);
                }

                PokeTrace pokeTrace = pokeTraceList.get(position);

                ImageView pokeIcon = (ImageView) convertView.findViewById(R.id.tracking_poke_icon);
                TextView pokeName = (TextView) convertView.findViewById(R.id.tracking_poke_name);
                TextView pokeDistance = (TextView) convertView.findViewById(R.id.tracking_poke_distance);
                TextView meters = (TextView) convertView.findViewById(R.id.tracking_poke_meters);

                pokeName.setText(String.valueOf(pokeTrace.name));

                Bitmap poke = ImageLoader.getPokeBitmap4x(getContext(), pokeTrace.type, ImageLoader.ORIENTATION_FRONT, ImageLoader.TYPE_NORMAL);
                pokeIcon.setImageBitmap(poke);

                if(pokeTrace.distance == 0) {
                    pokeDistance.setText("Visible");
                    meters.setText("");
                    meters.setPadding(0,0,0,0);
                } else {
                    pokeDistance.setText(String.format("%.3f", pokeTrace.distance));
                }

                return convertView;
            }
        };
        trackingListView.setAdapter(trackingAdapter);

        services = Services.getInstance(getApplicationContext());
        controllers = Controllers.getInstance(services);
        worldController = controllers.getWorldController();
        worldController.addPokeLocationListener(this);

        onPokeLocationsUpdated(worldController.getNearPokes(), worldController.getFarPokes());
    }

    @Override
    public void onPokeLocationsUpdated(final HashSet<Poke> nearPokesSet, final HashSet<PokeTrace> farPokesSet) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                trackingAdapter.clear();

                for(Poke poke: nearPokesSet) {
                    trackingAdapter.add(new PokeTrace(poke.type, poke.name, 0));
                }

                for(PokeTrace pokeTrace: farPokesSet) {
                    trackingAdapter.add(pokeTrace);
                }

                if(nearPokesSet.isEmpty() && farPokesSet.isEmpty()) {
                    trackingListView.setVisibility(View.GONE);
                    noPokesTextView.setVisibility(View.VISIBLE);
                } else {
                    noPokesTextView.setVisibility(View.GONE);
                    trackingListView.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        worldController.removePokeLocationListener(this);
        super.onDestroy();
    }
}
