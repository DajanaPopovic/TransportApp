package com.transportapp.graph;

import java.util.*;

/**
 * Represents a graph structure used for modeling transport routes between cities or stations.
 * Each node represents a station in city, and edges represent connections (e.g. departures).
 */
public class Graph {
    /** A map of node IDs to their corresponding GraphNode instances */
    private final Map<String, GraphNode> nodes = new HashMap<>();


    /**
     * Adds a new node to the graph.
     *
     * @param node the GraphNode to be added
     */
    public void addNode(GraphNode node) {
        nodes.put(node.getId(), node);
    }

    /**
     * Retrieves a node from the graph by its ID.
     *
     * @param id the unique identifier of the node
     * @return the GraphNode with the specified ID, or null if not found
     */
    public GraphNode getNode(String id) {
        return nodes.get(id);
    }

    /**
     * Returns a collection of all nodes in the graph.
     *
     * @return a Collection of all GraphNode objects
     */
    public Collection<GraphNode> getAllNodes() {
        return nodes.values();
    }


    /**
     * Adds a directed edge between two nodes in the graph.
     * If either node does not exist, the edge is not added.
     *
     * @param fromId the ID of the source node
     * @param toId   the ID of the destination node
     * @param cost   the cost of traveling from source to destination
     */
    public void addEdge(String fromId, String toId, int cost) {
        GraphNode from = nodes.get(fromId);
        GraphNode to = nodes.get(toId);
        if (from != null && to != null) {
            from.addEdge(new GraphEdge(to, cost));
        }
    }

    /**
     * Checks if a node with the specified ID exists in the graph.
     *
     * @param id the ID of the node
     * @return true if the node exists, false otherwise
     */
    public boolean containsNode(String id) {
        return nodes.containsKey(id);
    }


}
