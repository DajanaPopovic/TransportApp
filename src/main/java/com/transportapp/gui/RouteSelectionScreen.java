package com.transportapp.gui;

import com.transportapp.model.*;
import com.transportapp.routing.*;
import com.transportapp.graph.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Screen for selecting routes between cities with optimization criteria.
 * Provides UI elements for choosing start/end cities, optimization options,
 * and displays the resulting routes in a table.
 */
public class RouteSelectionScreen {

    /** ComboBox to select the departure city */
    private ComboBox<String> fromComboBox;

    /** ComboBox to select the destination city */
    private ComboBox<String> toComboBox;

    /** ChoiceBox to select optimization criteria (e.g. shortest time, lowest price) */
    private ChoiceBox<String> optimizationChoiceBox;

    /** Transport data model containing cities, stations, and routes */
    private TransportData transportData;


    /**
     * Constructor that initializes the screen with transport data.
     * @param transportData The transport data model.
     */
    public RouteSelectionScreen(TransportData transportData) {
        this.transportData = transportData;
    }

    /**
     * Shows the route selection screen on the given stage.
     * Sets up UI controls, event handlers and displays the stage.
     *
     * @param stage The primary stage for this screen.
     */
    public void show(Stage stage) {

        fromComboBox = new ComboBox<>();
        toComboBox = new ComboBox<>();
        optimizationChoiceBox = new ChoiceBox<>();

        Map<String, Integer> salesSummary = loadSalesSummary();
        Label salesInfoLabel = new Label("Ukupno prodanih karata: " + salesSummary.get("receipts")
                + " | Ukupan prihod: " + salesSummary.get("income") + " novcanih jedinica");
        salesInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-border-color: black; -fx-border-width: 2; -fx-padding: 10; -fx-background-color: #f0f8ff;");


        transportData.addDeparturesToStations();
        List<City> cities = transportData.getCities();
        List<String> cityNames = cities.stream().map((city)->{return city.getName();}).collect(Collectors.toList());
        fromComboBox.getItems().addAll(cityNames);
        toComboBox.getItems().addAll(cityNames);

        optimizationChoiceBox.getItems().addAll("Najkrace vrijeme putovanja", "Najniza cijena", "Najmanji broj presjedanja");

        Button findRouteButton = new Button("Pronadji rutu");

        TableView<Departure> routeTable = new TableView<>();
        routeTable.setPrefHeight(200);

        TableColumn<Departure, String> departureCol = new TableColumn<>("Polazak");
        departureCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFrom() + " (" + data.getValue().getDepartureTime() + ")"));

        TableColumn<Departure, String> arrivalCol = new TableColumn<>("Dolazak");
        arrivalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getTo() + " (" + data.getValue().getArrivalTime() + ")"));

        TableColumn<Departure, String> typeCol = new TableColumn<>("Tip");
        typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));

        TableColumn<Departure, String> priceCol = new TableColumn<>("Cijena");
        priceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getPrice())));

        routeTable.getColumns().addAll(departureCol, arrivalCol, typeCol, priceCol);

        Label summaryLabel = new Label();

        Button showMoreRoutesButton = new Button("Prikaz dodatnih ruta");
        Button buyTicketButton = new Button("Kupi kartu");
        Button showGraphButton = new Button("Prikazi graf rute");
        HBox buttonsBox = new HBox(10, showMoreRoutesButton, buyTicketButton, showGraphButton);

        findRouteButton.setOnAction(e -> {
            String fromCity = fromComboBox.getValue();
            String toCity = toComboBox.getValue();
            String optimizationCriteria = optimizationChoiceBox.getValue();

            if (fromCity == null || toCity == null) {
                showAlert("Greska", "Morate izabrati oba grada.");
                return;
            }

            if (fromCity.equals(toCity)) {
                showAlert("Greska", "Pocetni i krajnji grad ne mogu biti isti.");
                return;
            }

            if (optimizationCriteria == null) {
                showAlert("Greska", "Morate izabrati kriterijum optimizacije.");
                return;
            }

            Criteria criteria = switch (optimizationCriteria) {
                case "Najkrace vrijeme putovanja" -> Criteria.shortest_travel_time;
                case "Najniza cijena" -> Criteria.lowest_price;
                case "Najmanji broj presjedanja" -> Criteria.fewest_transfers;
                default -> throw new IllegalArgumentException("Nepoznat kriterijum: " + optimizationCriteria);
            };

            BuildGraph builder = new BuildGraph();
            Graph graph = builder.buildGraph(cities, criteria);
            RouteFinder finder = new RouteFinder();
            List<Path> topPaths = finder.findTop5RoutesByCity(graph, cities, fromCity, toCity, criteria);

            if (topPaths.isEmpty()) {
                routeTable.getItems().clear();
                summaryLabel.setText("Nije pronadjena nijedna ruta izmedju " + fromCity + " i " + toCity + ".");
            } else {
                Path bestPath = topPaths.get(0);
                List<Departure> departures = bestPath.getDepartures();

                routeTable.getItems().setAll(departures);

                int totalMinutes = bestPath.getTotalTravelTimeInMinutes();
                int totalPrice = departures.stream().mapToInt(Departure::getPrice).sum();

                int hours = totalMinutes / 60;
                int minutes = totalMinutes % 60;

                summaryLabel.setText("Ukupno vrijeme putovanja (voznja + cekanje): "
                        + hours + "h " + minutes + "min, "
                        + "Ukupna cijena: " + totalPrice + " novcanih jedinica.");

            }
        });

        buyTicketButton.setOnAction(e -> {
            List<Departure> selectedDepartures = routeTable.getItems();
            if (selectedDepartures == null || selectedDepartures.isEmpty()) {
                showAlert("Greska", "Nema prikazane rute za kupovinu.");
                return;
            }
            generateReceipt(selectedDepartures);
        });


        showMoreRoutesButton.setOnAction(e -> {
            String fromCity = fromComboBox.getValue();
            String toCity = toComboBox.getValue();
            String optimizationCriteria = optimizationChoiceBox.getValue();

            if (fromCity == null || toCity == null || optimizationCriteria == null) {
                showAlert("Greska", "Morate izabrati gradove i kriterijum optimizacije.");
                return;
            }

            Criteria criteria = switch (optimizationCriteria) {
                case "Najkrace vrijeme putovanja" -> Criteria.shortest_travel_time;
                case "Najniza cijena" -> Criteria.lowest_price;
                case "Najmanji broj presjedanja" -> Criteria.fewest_transfers;
                default -> throw new IllegalArgumentException("Nepoznat kriterijum: " + optimizationCriteria);
            };

            BuildGraph builder = new BuildGraph();
            Graph graph = builder.buildGraph(cities, criteria);
            RouteFinder finder = new RouteFinder();
            List<Path> topPaths = finder.findTop5RoutesByCity(graph, cities, fromCity, toCity, criteria);

            if (topPaths.isEmpty()) {
                showAlert("Informacija", "Nema dodatnih ruta za prikaz.");
                return;
            }

            Stage popupStage = new Stage();
            popupStage.setTitle("Dodatne rute");

            VBox routesBox = new VBox(10);
            routesBox.setPadding(new javafx.geometry.Insets(10));

            for (Path path : topPaths) {
                List<Departure> deps = path.getDepartures();
                String nodePath = deps.stream()
                        .map(dep -> dep.getFrom())
                        .collect(Collectors.joining(" -> ")) + " -> " + deps.get(deps.size() - 1).getTo();

                int totalMinutes = path.getTotalTravelTimeInMinutes();
                int totalPrice = deps.stream().mapToInt(Departure::getPrice).sum();
                int transfers = deps.size() - 1;

                int hours = totalMinutes / 60;
                int minutes = totalMinutes % 60;

                Label timeLabel = new Label("Vrijeme: " + hours + "h " + minutes + "min");
                Label priceLabel = new Label("Cijena: " + totalPrice + " novcanih jedinica");
                Label transferLabel = new Label("Presjedanja: " + transfers);
                Label nodesLabel = new Label("Ruta: " + nodePath);

                Button buyButton = new Button("Kupi kartu");
                buyButton.setOnAction(ev -> {
                    generateReceipt(deps);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Karta kupljena");
                    alert.setHeaderText(null);
                    alert.setContentText("Uspjesno ste kupili kartu za ovu rutu.");
                    alert.showAndWait();
                });

                VBox routeBox = new VBox(5, nodesLabel, timeLabel, priceLabel, transferLabel, buyButton);

                routeBox.setStyle("-fx-border-color: gray; -fx-border-radius: 5; -fx-padding: 10;");
                routesBox.getChildren().add(routeBox);
            }

            ScrollPane scrollPane = new ScrollPane(routesBox);
            scrollPane.setFitToWidth(true);

            Scene scene = new Scene(scrollPane, 400, 500);
            popupStage.setScene(scene);
            popupStage.show();
        });

        showGraphButton.setOnAction(e -> {
            List<Departure> selectedRoute = routeTable.getItems();
            if (selectedRoute == null || selectedRoute.isEmpty()) {
                showAlert("Greska", "Nema prikazane rute za graficki prikaz.");
                return;
            }

            Stage graphStage = new Stage();
            graphStage.setTitle("Graf sa rutom");

            Canvas canvas = new Canvas(800, 800);
            GraphicsContext gc = canvas.getGraphicsContext2D();

            GraphVisualizer visualizer = new GraphVisualizer(transportData, gc);
            visualizer.drawGraphWithRoute(selectedRoute);

            ScrollPane scrollPane = new ScrollPane(canvas);
            Scene scene = new Scene(scrollPane);

            graphStage.setScene(scene);
            graphStage.show();
        });


        VBox root = new VBox(10,
                salesInfoLabel,
                new Label("Pocetni grad:"), fromComboBox,
                new Label("Odredisni grad:"), toComboBox,
                new Label("Kriterijum optimizacije:"), optimizationChoiceBox,
                findRouteButton,
                new Label("Optimalna ruta:"),
                routeTable,
                summaryLabel,
                buttonsBox
        );
        root.setPadding(new javafx.geometry.Insets(20));

        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Odabir rute");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Shows an alert dialog with given title and message.
     *
     * @param title Dialog title
     * @param msg Message content
     */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Generates a receipt file for the purchased ticket(s).
     *
     * @param departures List of departures included in the ticket
     */
    private void generateReceipt(List<Departure> departures) {
        StringBuilder sb = new StringBuilder();

        sb.append("===== RACUN =====\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
        sb.append("Datum: ").append(LocalDateTime.now().format(formatter)).append("\n\n");

        sb.append("Relacija: ")
                .append(departures.get(0).getFrom())
                .append(" -> ")
                .append(departures.get(departures.size() - 1).getTo())
                .append("\n");

        sb.append("Stanice:\n");
        for (Departure dep : departures) {
            sb.append("  ").append(dep.getFrom())
                    .append(" -> ").append(dep.getTo())
                    .append(" (").append(dep.getDepartureTime())
                    .append(" - ").append(dep.getArrivalTime())
                    .append(") | ").append(dep.getType())
                    .append(" | ").append(dep.getPrice()).append(" novcanih jedinica\n");
        }

        int totalMinutes = departures.stream().mapToInt(Departure::getDuration).sum();
        int totalPrice = departures.stream().mapToInt(Departure::getPrice).sum();
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        sb.append("\nUkupno vrijeme: ").append(hours).append("h ").append(minutes).append("min\n");
        sb.append("Ukupna cijena: ").append(totalPrice).append(" novcanih jedinica\n");

        try {
            java.io.File dir = new java.io.File("racuni");
            if (!dir.exists()) dir.mkdirs();

            String fileName = "racuni/racun_" + System.currentTimeMillis() + ".txt";
            java.nio.file.Files.writeString(java.nio.file.Path.of(fileName), sb.toString());

            showAlert("Racun sacuvan", "Racun je sacuvan u fajl: " + fileName);
        } catch (Exception ex) {
            showAlert("Greska pri cuvanju", "Neuspjelo cuvanje racuna: " + ex.getMessage());
        }
    }

    /**
     * Loads sales summary from a file.
     * Returns map with keys "tickets" and "income".
     *
     * @return Map with sales data
     */
    private Map<String, Integer> loadSalesSummary() {
        int totalReceipts = 0;
        int totalIncome = 0;

        File dir = new File("racuni");
        if (!dir.exists() || !dir.isDirectory()) {
            return Map.of("receipts", 0, "income", 0);
        }

        File[] receiptFiles = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (receiptFiles == null) {
            return Map.of("receipts", 0, "income", 0);
        }

        totalReceipts = receiptFiles.length;

        for (File file : receiptFiles) {
            try {
                List<String> lines = java.nio.file.Files.readAllLines(file.toPath());

                for (String line : lines) {
                    if (line.startsWith("Ukupna cijena:")) {
                        String[] parts = line.split(":");
                        if (parts.length == 2) {
                            totalIncome += Integer.parseInt(parts[1].trim().split(" ")[0]);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Ne mogu da procitam fajl: " + file.getName());
            }
        }

        return Map.of("receipts", totalReceipts, "income", totalIncome);
    }

}
