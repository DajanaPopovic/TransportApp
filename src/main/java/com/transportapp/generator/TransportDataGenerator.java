package com.transportapp.generator;

import com.transportapp.model.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * A class responsible for generating transport data including
 * the map of cities, transport stations, and departures.
 */
public class TransportDataGenerator {
    /**
     * Number of columns in the map (width of the country).
     */
    private static  int X;

    /**
     * Number of rows in the map (height of the country).
     */
    private static  int Y;

    /**
     * Number of departures to generate per station.
     */
    private static final int DEPARTURES_PER_STATION = 5;

    /**
     * Random object for generating random values
     */
    private static final Random random = new Random();

    /**
     * Constructs a TransportDataGenerator for a country map of size X by Y.
     *
     * @param X the number of columns (width of the map)
     * @param Y the number of rows (height of the map)
     */
    public TransportDataGenerator(int X, int Y) {
        this.X=X;
        this.Y=Y;
    }

    /**
     * Generates a complete set of transport data including the country map,
     * list of stations, and list of departures.
     *
     * @return a {@link TransportData} object containing the generated data
     */
    public TransportData generateData() {
        TransportData data = new TransportData();
        data.countryMap = generateCountryMap();
        data.stations = generateStations();
        data.departures = generateDepartures(data.stations);
        return data;
    }

    /**
     * Generates a 2D array representing the map of the country.
     * Each city is labeled with a unique ID in the format "G_X_Y".
     *
     * @return a 2D String array representing the country map
     */
    public String[][] generateCountryMap() {
        String[][] countryMap = new String[X][Y];
        for (int x = 0; x < X; x++) {
            for (int y = 0; y < Y; y++) {
                countryMap[x][y] = "G_" + x + "_" + y;
            }
        }
        return countryMap;
    }

    /**
     * Generates transport stations (both bus and train) for each city on the map.
     * Bus stations are labeled with prefix "A_", and train stations with "Z_".
     *
     * @return a list of {@link Station} objects representing all stations
     */
    public List<Station> generateStations() {
        List<Station> stations = new ArrayList<>();
        for (int x = 0; x < X; x++) {
            for (int y = 0; y < Y; y++) {
                Station station = new Station();
                station.city = "G_" + x + "_" + y;
                station.busStation = "A_" + x + "_" + y;
                station.trainStation = "Z_" + x + "_" + y;
                stations.add(station);
            }
        }
        return stations;
    }

    /**
     * Generates a list of departures (bus and train trips) for each station.
     * Departure times, durations, and prices are randomly generated.
     *
     * @param stations the list of all stations for which to generate departures
     * @return a list of {@link Departure} objects representing all scheduled trips
     */
    public List<Departure> generateDepartures(List<Station> stations) {
        List<Departure> departures = new ArrayList<>();

        for (Station station : stations) {
            int x = Integer.parseInt(station.city.split("_")[1]);
            int y = Integer.parseInt(station.city.split("_")[2]);

            // generisanje polazaka autobusa
            for (int i = 0; i < DEPARTURES_PER_STATION; i++) {
                departures.add(generateDeparture("autobus", station.busStation, x, y));
            }

            // generisanje polazaka vozova
            for (int i = 0; i < DEPARTURES_PER_STATION; i++) {
                departures.add(generateDeparture("voz", station.trainStation, x, y));
            }
        }
        return departures;
    }

    /**
     * Generates a random departure for the specified transport type and origin city.
     *
     * @param type the type of transport ("bus" or "train")
     * @param from the origin station identifier (e.g., "A_2_3" or "Z_1_0")
     * @param x the x-coordinate of the city
     * @param y the y-coordinate of the city
     * @return a randomly generated Departure object
     */
    public Departure generateDeparture(String type, String from, int x, int y) {
        Departure departure = new Departure();
        departure.type = type;
        departure.from = from;

        // generisanje susjeda
        List<String> neighbors = getNeighbors(x, y);
        departure.to = neighbors.isEmpty() ? from : neighbors.get(random.nextInt(neighbors.size()));

        // generisanje vremena
        int hour = random.nextInt(24);
        int minute = random.nextInt(4) * 15; // 0, 15, 30, 45
        departure.departureTime = String.format("%02d:%02d", hour, minute);

        // geneirsanje cijene
        departure.duration = 30 + random.nextInt(151);
        departure.price = 100 + random.nextInt(901);

        // generisanje vremena transfera
        departure.minTransferTime = 5 + random.nextInt(26);

        return departure;
    }

    /**
     * Returns a list of neighboring cities (up, down, left, right) for the given city coordinates.
     *
     * @param x the x-coordinate of the current city
     * @param y the y-coordinate of the current city
     * @return a list of neighboring city names (e.g., "G_1_2")
     */
    public List<String> getNeighbors(int x, int y) {
        List<String> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 0 && nx < X && ny >= 0 && ny < Y) {
                neighbors.add("G_" + nx + "_" + ny);
            }
        }
        return neighbors;
    }


    /**
     * Saves the entire transport data (map, stations, departures) to a JSON file.
     *
     * @param data the transport data to be serialized
     * @param filename the name of the output JSON file
     */
    public void saveToJson(TransportData data, String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            StringBuilder json = new StringBuilder();
            json.append("{\n");

            // mapa drzave
            json.append("  \"countryMap\": [\n");
            for (int i = 0; i < X; i++) {
                json.append("    [");
                for (int j = 0; j < Y; j++) {
                    json.append("\"").append(data.countryMap[i][j]).append("\"");
                    if (j < Y - 1) json.append(", ");
                }
                json.append("]");
                if (i < X - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ],\n");

            // stanice
            json.append("  \"stations\": [\n");
            for (int i = 0; i < data.stations.size(); i++) {
                Station s = data.stations.get(i);
                json.append("    {\"city\": \"").append(s.city)
                        .append("\", \"busStation\": \"").append(s.busStation)
                        .append("\", \"trainStation\": \"").append(s.trainStation)
                        .append("\"}");
                if (i < data.stations.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ],\n");

            // vremena polazaka
            json.append("  \"departures\": [\n");
            for (int i = 0; i < data.departures.size(); i++) {
                Departure d = data.departures.get(i);
                json.append("    {\"type\": \"").append(d.type)
                        .append("\", \"from\": \"").append(d.from)
                        .append("\", \"to\": \"").append(d.to)
                        .append("\", \"departureTime\": \"").append(d.departureTime)
                        .append("\", \"duration\": ").append(d.duration)
                        .append(", \"price\": ").append(d.price)
                        .append(", \"minTransferTime\": ").append(d.minTransferTime)
                        .append("}");
                if (i < data.departures.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ]\n");

            json.append("}");
            file.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

