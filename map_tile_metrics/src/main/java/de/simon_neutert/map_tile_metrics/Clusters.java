/**
 * Clusters.java
 */
package de.simon_neutert.map_tile_metrics;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * The Clusters class provides methods to calculate and process clusters of
 * points.
 * 
 * <p>
 * This class includes methods to detect clusters of points, group them into
 * clusters, and find the maximum clusters based on their sizes. It returns the
 * results in a HashMap containing the clusters and the maximum clusters.
 * </p>
 * 
 * <p>
 * Usage example:
 * </p>
 * 
 * <pre>
 * {@code
 * HashSet<Point> points = new HashSet<>();
 * Clusters clusters = new Clusters(points);
 * // Add points to the set
 * HashMap<String, ArrayList<HashSet<Point>>> results = clusters.calculate();
 * ArrayList<HashSet<Point>> clustersList = results.get("clusters");
 * ArrayList<HashSet<Point>> maxClustersList = results.get("maxClusters");
 * }
 * </pre>
 * 
 * @see Point
 * 
 */
public class Clusters {
    private HashSet<Point> points;

    public Clusters(HashSet<Point> points) {
        this.points = points;
    }

    public HashMap<String, ArrayList<HashSet<Point>>> calculate() {
        HashMap<String, ArrayList<HashSet<Point>>> results = new HashMap<>();

        // Calculate clusters
        HashSet<Point> clusterPoints = detectClusterPoints(getPoints());
        ArrayList<HashSet<Point>> clusters = groupClusters(clusterPoints);
        results.put("clusters", clusters);

        // Calculate max clusters
        results.put("maxClusters", maxClusters(clusters));

        return results;
    }

    ArrayList<HashSet<Point>> maxClusters(ArrayList<HashSet<Point>> clusters) {
        if (clusters.isEmpty()) {
            return clusters;
        }

        int maxSizeCluster = clusters.stream()
                .max((a, b) -> a.size() - b.size())
                .get()
                .size();

        return clusters.stream()
                .filter(cluster -> cluster.size() == maxSizeCluster)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    HashSet<Point> detectClusterPoints(HashSet<Point> points) {
        ConcurrentLinkedQueue<Point> clustersQ = new ConcurrentLinkedQueue<Point>();

        points.parallelStream().forEach(point -> {
            if (validFourNeighbors(getNeighborsForPoint(point), points)) {
                clustersQ.add(point);
            }
        });
        HashSet<Point> clusters = new HashSet<Point>();
        clustersQ.forEach(clusters::add);
        return clusters;
    }

    ArrayList<HashSet<Point>> groupClusters(HashSet<Point> clusterPoints) {
        ArrayList<HashSet<Point>> clusters = new ArrayList<>();
        HashSet<Point> visited = new HashSet<>();

        for (Point point : clusterPoints) {
            if (visited.contains(point)) {
                continue;
            }
            HashSet<Point> cluster = new HashSet<Point>();
            HashSet<Point> toVisit = new HashSet<Point>();
            toVisit.add(point);

            while (!toVisit.isEmpty()) {
                Point current = toVisit.iterator().next();
                toVisit.remove(current);

                if (visited.contains(current)) {
                    continue;
                }

                visited.add(current);
                cluster.add(current);

                ArrayList<Point> neighbors = getNeighborsForPoint(current);
                for (Point neighbor : neighbors) {
                    if (clusterPoints.contains(neighbor)) {
                        toVisit.add(neighbor);
                    }
                }
            }
            clusters.add(cluster);
        }
        return clusters;
    }

    ArrayList<Point> getNeighborsForPoint(Point point) {
        ArrayList<Point> neighbors = new ArrayList<Point>();

        try {
            neighbors.add(new Point(point.x() - 1, point.y()));
        } catch (Exception e) {
            // skip this point
        }
        try {
            neighbors.add(new Point(point.x(), point.y() - 1));
        } catch (Exception e) {
            // skip this point
        }
        neighbors.add(new Point(point.x() + 1, point.y()));
        neighbors.add(new Point(point.x(), point.y() + 1));

        return neighbors;
    }

    boolean validFourNeighbors(ArrayList<Point> neighbors, HashSet<Point> points) {
        if (neighbors.size() != 4) {
            return false;
        }
        return neighbors.stream().allMatch(neighbor -> points.contains(neighbor));
    }

    public HashSet<Point> getPoints() {
        return this.points;
    }
}
