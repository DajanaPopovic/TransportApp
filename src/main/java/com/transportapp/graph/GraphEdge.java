package com.transportapp.graph;

/**
 * Represents a directed edge in the transport graph.
 * An edge connects two graph nodes and has travel cost
 * (e.g., time, price, or number of transfers).
 */
public class GraphEdge {
    /** The target node that this edge points to */
    private final GraphNode target;

    /** The cost of traveling along this edge */
    private final int cost;

    /**
     * Constructs a GraphEdge that points to the given target node with the specified cost.
     *
     * @param target the destination node this edge connects to
     * @param cost   the cost (e.g., time or price) to travel along this edge
     */
    public GraphEdge(GraphNode target, int cost) {
        this.target = target;
        this.cost = cost;
    }


    /**
     * Returns the target node that this edge points to.
     *
     * @return the destination GraphNode
     */
    public GraphNode getTarget() {
        return target;
    }

    /**
     * Returns the cost of traveling along this edge.
     *
     * @return the travel cost
     */
    public int getCost() {
        return cost;
    }
}
