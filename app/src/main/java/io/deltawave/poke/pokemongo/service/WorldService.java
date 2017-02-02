package io.deltawave.poke.pokemongo.service;

import android.content.Context;

import java.util.HashSet;

import io.deltawave.poke.pokemongo.poke.Poke;

/**
 * Created by will on 8/3/16.
 */
public class WorldService {

    private HashSet<Poke> pokesSet;

    public WorldService() {
        this.pokesSet = new HashSet<>();
    }

    public HashSet<Poke> getPokesSet() {
        return pokesSet;
    }

    public void addPoke(Poke p) {
        this.pokesSet.add(p);
    }

    public void removePoke(Poke p) {
        this.pokesSet.remove(p);
    }

}
