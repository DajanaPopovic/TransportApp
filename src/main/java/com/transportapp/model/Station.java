package com.transportapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a station in a city, which includes both a bus and a train station.
 * Also maintains a list of scheduled departures.
 */
public class Station {

    /**
     * The name of the city this station belongs to.
     */
    public String city;

    /**
     * The name or identifier of the bus station.
     */
    public String busStation;

    /**
     * The name or identifier of the train station.
     */
    public String trainStation;

    /**
     * A list of departures (bus and train) from this station.
     */
    public List<Departure> departures=new ArrayList<>();

    /**
     * Default constructor required for Jackson deserialization.
     */
    public Station() {
    }

    /**
     * Constructs a new Station with the specified city, bus station, and train station names.
     *
     * @param city         the name of the city
     * @param busStation   the name of the bus station
     * @param trainStation the name of the train station
     */
    public Station(String city, String busStation, String trainStation) {

        this.city = city;
        this.busStation=busStation;
        this.trainStation=trainStation;

    }

    /**
     * Sets the city name.
     * @param city the city name
     */
    public void setCity(String city){this.city=city;}

    /**
     * Gets the city name.
     * @return the city name
     */
    public String getCity(){return this.city;}


    /**
     * Sets the bus station name.
     * @param busStation the name of the bus station
     */
    public void setBusStation(String busStation){this.busStation=busStation;}

    /**
     * Gets the bus station name.
     * @return the name of the bus station
     */
    public String getBusStation(){return this.busStation;}

    /**
     * Sets the train station name.
     * @param trainStation the name of the train station
     */
    public void setTrainStation(String trainStation){this.trainStation=trainStation;}


    /**
     * Gets the train station name.
     * @return the name of the train station
     */
    public String getTrainStation(){return this.trainStation;}

    /**
     * Gets the list of departures from this station.
     * @return a list of departures
     */
    public List<Departure> getDepartures() { return departures; }

    /**
     * Sets the list of departures for this station.
     * @param departures the list of departures
     */
    public void setDepartures(List<Departure> departures) { this.departures = departures; }

    /**
     * Adds a single departure to the list of departures.
     * @param departure the departure to add
     */
    public void addDeparture(Departure departure) {
        this.departures.add(departure);
    }

    @Override
    public String toString(){
        return this.city+ " "+this.busStation+" "+trainStation;
    }
}


