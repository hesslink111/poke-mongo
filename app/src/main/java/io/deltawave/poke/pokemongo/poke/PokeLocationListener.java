package io.deltawave.poke.pokemongo.poke;

import java.util.HashSet;

/**
 * Created by will on 8/3/16.
 */
public interface PokeLocationListener {

    public void onPokeLocationsUpdated(HashSet<Poke> nearPokesSet, HashSet<PokeTrace> farPokesSet);

}
