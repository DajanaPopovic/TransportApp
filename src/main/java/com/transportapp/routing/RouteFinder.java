package com.transportapp.routing;

import com.transportapp.graph.*;
import com.transportapp.model.*;
import java.util.*;
import java.util.stream.*;
import java.time.LocalTime;
import java.time.*;

/**
 * A utility class for finding optimal routes in a transport graph
 * based on various criteria such as lowest price, shortest travel time,
 * or fewest transfers.
 */
public class RouteFinder {

    /**
     * Finds K the best paths between two graph nodes using the specified optimization criteria.
     *
     * @param graph The transport graph containing nodes and edges.
     * @param startId The ID of the start station.
     * @param endId The ID of the destination station.
     * @param k The maximum number of top paths to find.
     * @param criteria The optimization criteria (e.g., price, time, transfers).
     * @return A list of top K paths that meet the criteria.
     */
    public List<Path> findTopKPaths(Graph graph, String startId, String endId, int k, Criteria criteria) {
        PriorityQueue<Path> queue = new PriorityQueue<>();
        List<Path> results = new ArrayList<>();
        Set<String> uniqueRoutes = new HashSet<>();

        final int MAX_PATH_LENGTH = 10;

        GraphNode start = graph.getNode(startId);
        if (start == null || graph.getNode(endId) == null) return results;

        queue.add(new Path(List.of(start), 0, 0));

        while (!queue.isEmpty() && results.size() < k) {
            Path currentPath = queue.poll();
            GraphNode lastNode = currentPath.getNodes().get(currentPath.getNodes().size() - 1);

            if (lastNode.getId().equals(endId)) {
                String pathSignature = currentPath.getNodes().stream()
                        .map(GraphNode::getId)
                        .collect(Collectors.joining("->"));

                if (!uniqueRoutes.contains(pathSignature)) {
                    uniqueRoutes.add(pathSignature);

                    int totalTime = calculateTotalDurationWithWait(currentPath.getDepartures());


                    results.add(new Path(
                            new ArrayList<>(currentPath.getNodes()),
                            currentPath.getCost(),
                            new ArrayList<>(currentPath.getDepartures()), totalTime
                    ));
                }

                continue;
            }

            for (GraphEdge edge : lastNode.getEdges()) {
                GraphNode neighbor = edge.getTarget();
                if (currentPath.getNodes().contains(neighbor)) continue;

                LocalTime earliestNextDepartureTime = null;

                if (!currentPath.getDepartures().isEmpty()) {
                    Departure lastDeparture = currentPath.getDepartures().get(currentPath.getDepartures().size() - 1);
                    earliestNextDepartureTime = lastDeparture.getArrivalTime().plusMinutes(lastDeparture.getMinTransferTime());
                }
                final LocalTime finalEarliestNextDepartureTime = earliestNextDepartureTime;

                // Choose the best departure based on selected criteria
                Optional<Departure> departureOpt = lastNode.getDeparturesFromCity().stream()
                        .filter(d -> d.getTo().equals(neighbor.getCity().getName()))
                        .filter(d -> d.getParsedDepartureTime() != null)
                        .min(Comparator.comparingInt(d -> {
                            LocalTime departureTime = d.getParsedDepartureTime();
                            LocalDateTime currentTime = LocalDateTime.now();
                            LocalDateTime earliest = currentTime;

                            if (finalEarliestNextDepartureTime != null) {
                                earliest = LocalDateTime.of(currentTime.toLocalDate(), finalEarliestNextDepartureTime);
                            }

                            LocalDateTime candidate = LocalDateTime.of(earliest.toLocalDate(), departureTime);
                            if (candidate.isBefore(earliest)) {
                                candidate = candidate.plusDays(1);
                            }

                            long waitTime = Duration.between(earliest, candidate).toMinutes();
                            return switch (criteria) {
                                case lowest_price -> d.getPrice();
                                case shortest_travel_time -> (int) (waitTime + d.getDuration());
                                case fewest_transfers -> currentPath.getDepartures().size();
                            };
                        }));


                if (departureOpt.isEmpty()) continue;

                Departure selectedDeparture = departureOpt.get();

                List<GraphNode> newPathNodes = new ArrayList<>(currentPath.getNodes());
                newPathNodes.add(neighbor);

                List<Departure> newDepartures = new ArrayList<>(currentPath.getDepartures());
                newDepartures.add(selectedDeparture);

                int totalTime = 0;

                totalTime = calculateTotalDurationWithWait(newDepartures);

                if (newPathNodes.size() > MAX_PATH_LENGTH) continue;

                int newCost = currentPath.getCost() + edge.getCost();
                queue.add(new Path(newPathNodes, newCost, newDepartures, totalTime));
            }
        }

        return results;
    }

    /**
     * Returns a list of station IDs (bus and train) for the given city.
     *
     * @param city The city for which to retrieve station IDs.
     * @return List of station IDs (bus and train).
     */
    private List<String> getStationIdsForCity(City city) {
        List<String> ids = new ArrayList<>();
        ids.add(city.getStation().getBusStation());
        ids.add(city.getStation().getTrainStation());
        return ids;
    }

    /**
     * Finds the top 5 optimal paths between two cities using all possible combinations
     * of bus and train stations.
     *
     * @param graph The transport graph.
     * @param cities The list of cities.
     * @param startCityName The name of the starting city.
     * @param endCityName The name of the destination city.
     * @param criteria The optimization criteria.
     * @return A list of the best 5 paths between the two cities.
     */
    public List<Path> findTop5RoutesByCity(Graph graph, List<City> cities, String startCityName, String endCityName, Criteria criteria) {
        RouteFinder finder = new RouteFinder();
        List<Path> allPaths = new ArrayList<>();

        City startCity = null, endCity = null;

        for (City c : cities) {
            if (c.getName().equals(startCityName)) startCity = c;
            if (c.getName().equals(endCityName)) endCity = c;
        }

        if (startCity == null || endCity == null) {
            System.out.println("Jedan od gradova nije pronadjen.");
            return allPaths;
        }

        List<String> startStations = getStationIdsForCity(startCity);
        List<String> endStations = getStationIdsForCity(endCity);

        for (String from : startStations) {
            for (String to : endStations) {
                List<Path> paths = finder.findTopKPaths(graph, from, to, 5, criteria);
                allPaths.addAll(paths);
            }
        }

        Comparator<Path> comparator = switch (criteria) {
            case lowest_price -> Comparator.comparingInt(p -> p.getDepartures().stream().mapToInt(Departure::getPrice).sum());
            case shortest_travel_time -> Comparator.comparingInt(Path::getTotalTravelTimeInMinutes);
            case fewest_transfers -> Comparator.comparingInt(p -> p.getDepartures().size());
        };

        allPaths.sort(comparator);
        return allPaths.stream().distinct().limit(5).toList();

    }

    /**
     * Determines whether a departure time is after a specified arrival time with wait.
     * This method checks if the departure time is after the given arrival time after wait,
     * or if there is no arrival time with wait (i.e., the arrival time is null).
     *
     * @param arrivalTimeWithWait The arrival time with any additional waiting time included.
     *                            If this value is null, the departure time is considered valid.
     * @param departureTime The departure time to compare against the arrival time with wait.
     * @return true if the departure time is after the arrival time with wait or if the arrival time is null.
     *         Otherwise, returns false.
     */
    private boolean isDepartureAfterWait(LocalTime arrivalTimeWithWait, LocalTime departureTime) {
        if (arrivalTimeWithWait == null) return true;

        return !departureTime.isBefore(arrivalTimeWithWait);
    }

    /**
     * Calculates the total duration of a series of departures, including waiting time between them.
     * This method computes the total time taken from the first departure to the last, factoring in the
     * waiting times between consecutive departures as well as their individual durations.
     *
     * @param departures A list of departures to calculate the total duration for.
     *                   Each departure contains a departure time and a duration (in minutes).
     * @return The total duration in minutes, including the wait times between consecutive departures.
     *         If the list of departures is empty, returns 0.
     */
    private int calculateTotalDurationWithWait(List<Departure> departures) {
        if (departures.isEmpty()) return 0;

        int totalMinutes = 0;
        LocalDateTime currentTime = LocalDateTime.of(LocalDate.now(), departures.get(0).getParsedDepartureTime());

        for (Departure d : departures) {
            LocalDateTime departureTime = LocalDateTime.of(currentTime.toLocalDate(), d.getParsedDepartureTime());

            if (departureTime.isBefore(currentTime)) {
                departureTime = departureTime.plusDays(1);
            }

            Duration wait = Duration.between(currentTime, departureTime);
            totalMinutes += wait.toMinutes();

            totalMinutes += d.getDuration();

            currentTime = departureTime.plusMinutes(d.getDuration());
        }

        return totalMinutes;
    }

}
