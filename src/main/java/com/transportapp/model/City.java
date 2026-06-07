package com.transportapp.model;

import java.util.*;

/**
 * Represents a city within the transportation network.
 * Each city has a name, coordinates (x, y), and a stations (bus and train).
 */
public class City {
    /**
     * The name of the city.
     */
    private String name;

    /**
     * The x-coordinate of the city's location on the map.
     */
    private int x;

    /**
     * The y-coordinate of the city's location on the map.
     */
    private int y;

    /**
     * The stations located in the city.
     */
    private Station station;


    /**
     * Default constructor required for Jackson deserialization.
     */
    public City() {}

    /**
     * Constructs a new City with the specified name, coordinates, and station.
     *
     * @param name the name of the city
     * @param x the x-coordinate of the city on the map
     * @param y the y-coordinate of the city on the map
     * @param station the station located in the city
     */
    public City( String name, int x, int y, Station station) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.station=station;

    }

    /**
     * Returns the name of the city.
     *
     * @return the name of the city
     */
    public String getName() { return name; }

    /**
     * Returns the x-coordinate of the city.
     *
     * @return the x-coordinate
     */
    public int getX() { return x; }

    /**
     * Returns the y-coordinate of the city.
     *
     * @return the y-coordinate
     */
    public int getY() { return y; }

    /**
     * Returns the station located in the city.
     *
     * @return the station
     */
    public Station getStation(){return this.station;}

    /**
     * Sets the name of the city.
     *
     * @param name the new city name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Sets the x-coordinate of the city.
     *
     * @param x the new x-coordinate
     */
    public void setX(int x) { this.x = x; }

    /**
     * Sets the y-coordinate of the city.
     *
     * @param y the new y-coordinate
     */
    public void setY(int y) { this.y = y; }

    /**
     * Sets the station located in the city.
     *
     * @param station the new station
     */
    public void setStation(Station station){this.station=station;}

    @Override
    public String toString() {
        return this.name + " (" + x + "," + y + ") "+this.station.toString()  ;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        City other = (City) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

