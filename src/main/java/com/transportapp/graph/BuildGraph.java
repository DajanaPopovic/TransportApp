package com.transportapp.graph;

import com.transportapp.model.*;
import java.util.*;

/**
 * Utility class responsible for constructing a graph from a list of cities and their transport data.
 * Nodes in the graph represent bus and train stations, and edges represent departures between them.
 */
public class BuildGraph {

    /**
     * Builds a graph from the provided list of cities and a given optimization criterion.
     *
     * @param cities   the list of cities containing station and departure information
     * @param criteria the optimization criterion (e.g. shortest travel time, lowest price)
     * @return the constructed Graph representing the transport network
     */
    public  Graph buildGraph(List<City> cities, Criteria criteria){
       Graph graph=new Graph();

        // Add graph nodes for each station (bus and train) in each city
       for(City city:cities) {

           String id1 = city.getStation().getBusStation();
           GraphNode node1 = new GraphNode(id1, city);
           if(!graph.containsNode(id1)) {
               graph.addNode(node1);
           }

           String id2 = city.getStation().getTrainStation();
           GraphNode node2 = new GraphNode(id2, city);
           if(!graph.containsNode(id2)) {
               graph.addNode(node2);
           }
       }

        // Add graph edges based on departures from each city's station
        for (City city : cities) {
            for (Departure departure : city.getStation().getDepartures()) {

                // Find the destination city for the departure
                Optional<City> targetCity = cities.stream()
                        .filter(c -> c.getName().equals(departure.getTo()))
                        .findFirst();

                if (targetCity.isPresent()) {
                    String toStationId;
                    if ("autobus".equals(departure.getType())) {
                        toStationId = targetCity.get().getStation().getBusStation();
                    } else if ("voz".equals(departure.getType())) {
                        toStationId = targetCity.get().getStation().getTrainStation();
                    } else {
                        System.out.println("Nepoznat tip prevoza: " + departure.getType());
                        continue;
                    }

                    // Determine edge cost based on the chosen optimization criterion
                    int cost;
                    if (criteria == Criteria.shortest_travel_time) {
                        cost = departure.getDuration();
                    } else if (criteria == Criteria.lowest_price) {
                        cost = departure.getPrice();
                    } else {
                        cost = 1; // if (criteria ==  Criteria.fewest_transfers)
                    }

                    graph.addEdge(departure.getFrom(), toStationId, cost);
                } else {
                    System.out.println("Grad nije pronadjen: " + departure.getTo());
                }
            }
        }

        return graph;
    }
}
