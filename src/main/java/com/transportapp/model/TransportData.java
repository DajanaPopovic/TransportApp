package com.transportapp.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Represents the transport data for a country including its map, stations, and departures.
 * Provides utility methods for data manipulation and retrieval.
 */
public class TransportData {
    /**
     * A 2D array representing the country map where each cell holds the name of a city.
     * For example: countryMap[i][j] = "CityName" at position (i, j).
     */
    public String[][] countryMap;

    /**
     * A list of station objects corresponding to cities.
     */
    public List<Station> stations;


    /**
     * A list of departures (either bus or train).
     */
    public List<Departure> departures;

    /**
     * Returns the country map as a 2D array of city names.
     * @return 2D string array representing the map of the country
     */
    public String[][] getCountryMap() {
        return this.countryMap;
    }

    /**
     * Sets the country map.
     * @param countryMap 2D array representing city positions in the map
     */
    public void setCountryMap(String[][] countryMap) {
        this.countryMap = countryMap;
    }

    /**
     * Returns the list of stations.
     * @return list of station objects
     */
    public List<Station> getStations() {
        return this.stations;
    }


    /**
     * Sets the list of stations.
     * @param stations list of station objects
     */
    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    /**
     * Returns the list of departures.
     * @return list of departures
     */
    public List<Departure> getDepartures() {
        return this.departures;
    }

    /**
     * Sets the list of departures.
     * @param departures list of departures
     */
    public void setDepartures(List<Departure> departures) {
        this.departures = departures;
    }

    /**
     * Associates departures with the corresponding station.
     * Buses are matched with bus stations, trains with train stations.
     */
    public void addDeparturesToStations(){
        for(Departure departure:departures){
           String type= departure.getType();
           if("autobus".equals(type)){
              for(Station station: stations){
                  if (station.getBusStation().equals(departure.getFrom())){
                      station.addDeparture(departure);
                  }
              }
           }else if("voz".equals(type)){
               for(Station station: stations){
                   if (station.getTrainStation().equals(departure.getFrom())){
                       station.addDeparture(departure);
                   }
               }
           }
        }
    }

    /**
     * Returns a list of City objects, created based on the country map and available stations.
     * Each city includes its coordinates and the associated station.
     *
     * @return list of cities with coordinates and stations
     */
    public List<City> getCities() {
        List<City> cities = new ArrayList<>();
        for (int i = 0; i < countryMap.length; i++) {
            for (int j = 0; j < countryMap[i].length; j++) {
                String cityName = countryMap[i][j];
                Station cityStation=null;
                for(Station s: stations){
                    if (cityName.equals(s.getCity())){
                        cityStation=s;
                    }
                }

                City city = new City(cityName, i, j, cityStation);
                cities.add(city);
            }
        }
        return cities;
    }

    /**
     * Reads a JSON file and maps it to a TransportData object.
     *
     * @param filePath the path to the JSON file
     * @return a TransportData object populated with values from the file
     * @throws RuntimeException if the file cannot be read or parsed
     */
    public static TransportData readValue(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(filePath), TransportData.class);
        } catch (IOException e) {
            throw new RuntimeException("Greska pri ucitavanju fajla: " + filePath, e);
        }
    }

//     public void printDepartures(){
//        for(Departure d:departures) {
//            System.out.println(d);
//        }
//     }
}

