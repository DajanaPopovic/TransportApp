package com.transportapp.graph;

import com.transportapp.model.*;
import java.util.*;

/**
 * Represents a node in the transport graph.
 * Each node corresponds to a specific transport point in a city.
 * It is connected to other nodes via edges representing routes (departures).
 */
public class GraphNode {
    /** Unique identifier of the node   */
    private final String id;

    /** The city associated with this graph node */
    private final City city;

    /** List of edges (connections) to other graph nodes */
    private final List<GraphEdge> edges = new ArrayList<>();

    /**
     * Constructs a GraphNode with the given ID and associated city.
     *
     * @param id   the unique identifier of the node
     * @param city the city associated with this node
     */
    public GraphNode(String id, City city) {
        this.id = id;
        this.city = city;
    }

    /**
     * Returns the unique identifier of this node.
     *
     * @return node ID
     */
    public String getId() { return id; }


    /**
     * Returns the city associated with this node.
     *
     * @return the city object
     */
    public City getCity() { return city; }

    /**
     * Returns a list of edges (routes) connected to this node.
     *
     * @return list of edges
     */
    public List<GraphEdge> getEdges() { return edges; }

    /**
     * Adds a new edge (route) to this node.
     *
     * @param edge the graph edge to be added
     */
    public void addEdge(GraphEdge edge) {
        edges.add(edge);
    }

    /**
     * Returns the list of departures available from this node's station.
     *
     * @return list of departures
     */
    public List<Departure> getDeparturesFromCity(){
        return this.city.getStation().getDepartures();
    }

    @Override
    public String toString() {
        return id;
    }
}
