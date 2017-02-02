package io.deltawave.poke.pokemongo.poke;

/**
 * Created by will on 8/3/16.
 */
public class Poke {

    public int timeLeft;

    public int type;
    public String name;

    public double latitude;
    public double longitude;

    public Poke() {
        //No-arg-constructor
    }

    public Poke(int timeLeft, int type, String name, double latitude, double longitude) {
        this.timeLeft = timeLeft;
        this.type = type;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int decrementTimeLeft() {
        return --timeLeft;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        return (int)((latitude*180+longitude)*100000000);
    }

    public boolean equals(Object o) {
        Poke p = (Poke) o;
        return this.latitude == p.latitude && this.longitude == p.longitude;
    }


}
