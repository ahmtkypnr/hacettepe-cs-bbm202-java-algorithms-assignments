import java.io.Serializable;
import java.util.*;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;
    
    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }


    /**
     * Function calculate the fastest route from the user's desired starting point to 
     * the desired destination point, taking into consideration the hyperloop train
     * network. 
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {
        List<RouteDirection> routeDirections = new ArrayList<>();
        
        // TODO: Your code goes here

        class Graph {
            private final Map<Station, List<Edge>> adjStations = new HashMap<>();

            public void addStation(Station station) {
                adjStations.putIfAbsent(station, new ArrayList<>());
            }

            public void addEdge(Station station1, Station station2, double weight) {
                adjStations.get(station1).add(new Edge(station2, weight));
                adjStations.get(station2).add(new Edge(station1, weight));
            }

            public List<Edge> getAdjStations(Station station) {
                return adjStations.get(station);
            }

            public Set<Station> getStations() {
                return adjStations.keySet();
            }

            class Edge {
                Station target;
                double weight;

                public Edge(Station target, double weight) {
                    this.target = target;
                    this.weight = weight;
                }
            }
        }


        class DijkstraAlgorithm {

            public Map<Station, Double> dijkstra(Graph graph, Station start, Map<Station, Station> previous) {
                Map<Station, Double> distances = new HashMap<>();
                PriorityQueue<StationDistance> pq = new PriorityQueue<>(Comparator.comparingDouble(sd -> sd.distance));

                for (Station station : graph.getStations()) {
                    distances.put(station, Double.MAX_VALUE);
                    previous.put(station, null);
                }
                distances.put(start, 0.0);
                pq.add(new StationDistance(start, 0.0));

                while (!pq.isEmpty()) {
                    StationDistance current = pq.poll();
                    Station currentStation = current.station;

                    for (Graph.Edge edge : graph.getAdjStations(currentStation)) {
                        Station neighbor = edge.target;
                        double newDist = distances.get(currentStation) + edge.weight;
                        if (newDist < distances.get(neighbor)) {
                            distances.put(neighbor, newDist);
                            pq.add(new StationDistance(neighbor, newDist));
                            previous.put(neighbor, currentStation);
                        }
                    }
                }

                return distances;
            }

            class StationDistance {
                Station station;
                double distance;

                public StationDistance(Station station, double distance) {
                    this.station = station;
                    this.distance = distance;
                }
            }
        }
        class PathReconstruction {

            public List<Station> reconstructPath(Station start, Station end, Map<Station, Station> previous) {
                LinkedList<Station> path = new LinkedList<>();
                for (Station at = end; at != null; at = previous.get(at)) {
                    path.addFirst(at);
                }
                if (path.getFirst().equals(start)) {
                    return path;
                }
                return null;
            }
        }


        Graph graph = new Graph();

        graph.addStation(network.startPoint);
        graph.addStation(network.destinationPoint);
        for (TrainLine trainLine : network.lines) {
            for (Station station : trainLine.trainLineStations) {
                graph.addStation(station);
            }
        }

        double speed = network.averageTrainSpeed;
        boolean trainLine1 = false;
        boolean trainLine2 = false;
        for (Station station : graph.getStations()) {
            if (station.description.equals("Starting Point") || station.description.equals("Final Destination")) {
                trainLine1 = false;
            }
            else {
                trainLine1 = true;
            }
            for (Station station2 : graph.getStations()) {
                if (station2.description.equals("Starting Point") || station2.description.equals("Final Destination")) {
                    trainLine1 = false;
                }
                else {
                    trainLine2 = true;
                }
                if (trainLine1 || trainLine2) {
                    speed = network.averageTrainSpeed;
                }
                else {
                    speed = network.averageWalkingSpeed;
                }
                double weight = Math.sqrt(Math.pow((station.coordinates.x - station2.coordinates.x), 2) + Math.pow((station.coordinates.y - station2.coordinates.y), 2)) / speed;
                graph.addEdge(station, station2, weight);
            }
        }

        Map<Station, Station> previous = new HashMap<>();
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm();
        Map<Station, Double> shortestPaths = dijkstraAlgorithm.dijkstra(graph, network.startPoint, previous);

        Station endStation = network.destinationPoint;
        PathReconstruction pathReconstruction = new PathReconstruction();
        List<Station> path = pathReconstruction.reconstructPath(network.startPoint, endStation, previous);

        for (int i = 0; i < path.size() - 1; i++) {
            Station station = path.get(i);
            Station station2 = path.get(i + 1);
            boolean trainLine = false;
            double sp;
            if (station.description.equals("Starting Point") || station.description.equals("Final Destination")) {
                trainLine = false;
            }
            else {
                if (station2.description.equals("Starting Point") || station2.description.equals("Final Destination")) {
                    trainLine = false;
                }
                else {
                    trainLine = true;
                }
            }

            if (trainLine) {
                sp = network.averageTrainSpeed;
            }
            else {
                sp = network.averageWalkingSpeed;
            }
            double weight = Math.sqrt(Math.pow((station.coordinates.x - station2.coordinates.x), 2) + Math.pow((station.coordinates.y - station2.coordinates.y), 2)) / sp;
            routeDirections.add(new RouteDirection(station.description, station2.description, weight, trainLine));
        }

        return routeDirections;
    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        
        // TODO: Your code goes here

        double sum = 0.0;
        for (RouteDirection direction : directions) {
            sum += direction.duration;
        }

        System.out.println("The fastest route takes " + Math.round(sum) + " minute(s).");
        System.out.println("Directions");
        System.out.println("---------");

        for (int i = 0; i < directions.size(); i++) {
            String type = "";
            if (directions.get(i).trainRide) {
                type = ". Get on the train from ";
            }
            else {
                type = ". Walk from ";
            }
            double duration = directions.get(i).duration;
            String formattedValue = String.format("%.2f", duration);
            System.out.println((i + 1) + type + directions.get(i).startStationName + " to " + directions.get(i).endStationName + " for " + formattedValue + " minutes.");
        }
    }
}