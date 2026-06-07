package com.transportapp.gui;

import com.transportapp.model.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The class is responsible for rendering a visual
 * representation of a transport graph, including cities as nodes and departures as edges,
 * using JavaFX's {@link GraphicsContext}.
 */
public class GraphVisualizer {
    /**
     * Transport data that contains cities and departures to be visualized.
     */
    private TransportData data;

    /**
     * The {@link GraphicsContext} used to draw on the JavaFX canvas.
     */
    private GraphicsContext gc;


    /**
     * The scale used to position cities and edges proportionally on the canvas.
     */
    private double scale = 80;

    /**
     * The radius of the circles representing cities (nodes) on the graph.
     */
    private double radius = 10;

    /**
     * Constructs a GraphVisualizer with the given transport data and graphics context.
     *
     * @param data the transport data model containing cities and departures
     * @param gc the JavaFX graphics context for drawing
     */
    public GraphVisualizer(TransportData data, GraphicsContext gc) {
        this.data = data;
        this.gc = gc;
    }

    /**
     * Draws the transport graph with a highlighted route.
     *
     * @param route a list of departures that represent the selected or optimal route
     *
     * All connections in the route are drawn in red with a thicker line.
     * Other connections are shown in gray.
     */
    public void drawGraphWithRoute(List<Departure> route) {
        Map<String, City> cityMap = data.getCities().stream()
                .collect(Collectors.toMap(City::getName, c -> c));

        Set<String> routeEdges = route.stream()
                .map(d -> d.getFrom() + "->" + d.getTo())
                .collect(Collectors.toSet());

        for (Departure dep : data.getDepartures()) {
            String fromCityName = "G" + dep.getFrom().substring(1);
            String toCityName = "G" + dep.getTo().substring(1);

            City fromCity = cityMap.get(fromCityName);
            City toCity = cityMap.get(toCityName);

            if (fromCity != null && toCity != null) {
                double x1 = fromCity.getX() * scale + scale / 2;
                double y1 = fromCity.getY() * scale + scale / 2;
                double x2 = toCity.getX() * scale + scale / 2;
                double y2 = toCity.getY() * scale + scale / 2;

                String edgeKey = dep.getFrom() + "->" + dep.getTo();

                if (routeEdges.contains(edgeKey)) {
                    gc.setStroke(Color.RED);
                    gc.setLineWidth(3);
                } else {
                    gc.setStroke(Color.GRAY);
                    gc.setLineWidth(1);
                }

                gc.strokeLine(x1, y1, x2, y2);
            }
        }

        for (City city : data.getCities()) {
            double x = city.getX() * scale + scale / 2;
            double y = city.getY() * scale + scale / 2;

            gc.setFill(Color.LIGHTGRAY);
            gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

            gc.setFill(Color.BLACK);
            gc.fillText(city.getName(), x - 15, y - radius - 5);
        }
    }


}

