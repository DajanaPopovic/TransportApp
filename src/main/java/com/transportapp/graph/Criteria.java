package com.transportapp.graph;

/**
 * Enum representing different optimization criteria for route finding in the transport network.
 * These criteria are used to determine the best path between cities.
 */
public enum Criteria {
    shortest_travel_time, lowest_price, fewest_transfers;
}
