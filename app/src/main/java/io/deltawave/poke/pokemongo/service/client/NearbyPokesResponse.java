package io.deltawave.poke.pokemongo.service.client;

import java.util.HashSet;

import io.deltawave.poke.pokemongo.poke.Poke;
import io.deltawave.poke.pokemongo.poke.PokeTrace;

/**
 * Created by will on 8/3/16.
 */
public class NearbyPokesResponse {
    public HashSet<Poke> nearPokesSet;
    public HashSet<PokeTrace> farPokesSet;
}

