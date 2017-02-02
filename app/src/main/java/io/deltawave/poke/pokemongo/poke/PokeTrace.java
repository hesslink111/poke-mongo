package io.deltawave.poke.pokemongo.poke;

/**
 * Created by will on 8/6/16.
 */
public class PokeTrace {
    public int type;
    public String name;
    public double distance;

    public PokeTrace() {
        //No-arg-constructor
    }

    public PokeTrace(int type, String name, double distance) {
        this.type = type;
        this.name = name;
        this.distance = distance;
    }

    @Override
    public int hashCode() {
        return (int)((type*1000+distance)*100000000);
    }

    public boolean equals(Object o) {
        PokeTrace pt = (PokeTrace) o;
        return this.type == pt.type && this.distance == pt.distance;
    }
}
