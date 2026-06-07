package com.transportapp.routing;

import com.transportapp.graph.*;
import com.transportapp.model.*;
import java.util.*;

/**
 * Represents a path in the transport graph, consisting of a sequence of nodes,
 * a total cost, and a list of departures used along the way.
 *
 * Paths are comparable based on their total cost to support sorting and priority queues.
 */
public class Path implements Comparable<Path> {
    /** The list of graph nodes in the path. */
    private final List<GraphNode> nodes;

    /** The total cost of the path (based on selected criteria: time, price, etc.). */
    private final int cost;

    /** The list of departures used in this path. */
    private List<Departure> departures;

    /** Total travel time in minutes, including wait and travel times. */
    private final int totalTravelTimeInMinutes;


    /**
     * Constructs a path with given nodes and cost.
     *
     * @param nodes List of nodes in the path.
     * @param cost Total cost of the path.
     * @param totalTravelTimeInMinutes total travel time including waiting times.
     */
    public Path(List<GraphNode> nodes, int cost, int totalTravelTimeInMinutes) {
        this.nodes = nodes;
        this.cost = cost;
        this.departures = new ArrayList<>();
        this.totalTravelTimeInMinutes = totalTravelTimeInMinutes;
    }

    /**
     * Constructs a path with given nodes, cost, and a predefined list of departures.
     *
     * @param nodes List of nodes in the path.
     * @param cost Total cost of the path.
     * @param departures List of departures used in the path.
     * @param totalTravelTimeInMinutes total travel time including waiting times.
     */
    public Path(List<GraphNode> nodes, int cost, List<Departure> departures,int totalTravelTimeInMinutes) {
        this.nodes = nodes;
        this.cost = cost;
        this.departures = departures;
        this.totalTravelTimeInMinutes = totalTravelTimeInMinutes;
    }

    /**
     * Returns the list of graph nodes in the path.
     *
     * @return List of graph nodes.
     */
    public List<GraphNode> getNodes() {
        return nodes;
    }


    /**
     * Returns the total cost of the path.
     *
     * @return Path cost.
     */
    public int getCost() {
        return cost;
    }


    /**
     * Returns the list of departures used in the path.
     *
     * @return List of departures.
     */
    public List<Departure> getDepartures() {
        return departures;
    }

    /**
     * Adds a departure to the path.
     *
     * @param departure Departure to add.
     */
    public void addDeparture(Departure departure) {
        departures.add(departure);
    }

    /**
     * Returns the total travel time including waiting times.
     *
     * @return total travel time in minutes.
     */
    public int getTotalTravelTimeInMinutes() {
        return totalTravelTimeInMinutes;
    }

    @Override
    public int compareTo(Path other) {
        return Integer.compare(this.cost, other.cost);
    }

    @Override
    public String toString() {
        return "Path{" + "cost=" + cost + ", nodes=" + nodes + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Path path = (Path) o;
        return this.nodes.equals(path.nodes);
    }

    @Override
    public int hashCode() {
        return nodes.hashCode();
    }


}

