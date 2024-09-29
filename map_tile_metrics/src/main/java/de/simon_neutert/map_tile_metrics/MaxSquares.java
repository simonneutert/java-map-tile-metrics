
package de.simon_neutert.map_tile_metrics;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import java.util.Comparator;

/**
 * The MaxSquares class is responsible for calculating the maximum square sizes
 * that can be formed within clusters of points.
 * 
 * <p>
 * This class takes in a list of clusters and a set of points, and provides
 * a method to calculate the maximum square sizes for each cluster. It also
 * includes functionality to add borders to clusters before performing the
 * calculations.
 * </p>
 * 
 * <p>
 * Usage example:
 * </p>
 * 
 * <pre>
 * {@code
 * HashSet<Point> points = new HashSet<Point>();
 * ArrayList<HashSet<Point>> clusters = new ArrayList<HashSet<Point>>();
 * MaxSquares maxSquares = new MaxSquares(clusters, points);
 * ArrayList<HashMap<Point, Integer>> result = maxSquares.calculate();
 * }
 * </pre>
 * 
 * @see Point
 * @see Clusters
 * 
 */
public class MaxSquares {
    /**
     * A list of clusters, where each cluster is represented as a HashSet of Points.
     */
    public ArrayList<HashSet<Point>> clusters;
    /**
     * A set of points that are part of the clusters.
     */
    public HashSet<Point> points;
    /**
     * A flag indicating whether borders have been added to the clusters.
     */
    public boolean bordersAdded;

    /**
     * Constructs a new MaxSquares object with the specified clusters and points.
     * 
     * @param clusters the list of clusters
     * @param points   the set of points
     */
    public MaxSquares(ArrayList<HashSet<Point>> clusters, HashSet<Point> points) {
        this.clusters = clusters;
        this.points = points;
        this.bordersAdded = false;
    }

    /**
     * Calculates the maximum square sizes for each cluster.
     * 
     * <p>
     * This method adds borders to the clusters and then calculates the maximum
     * square sizes that can be formed within each cluster. The result is returned
     * as a list of HashMaps, where each HashMap maps a Point to the size of the
     * maximum square that can be formed at that point.
     * </p>
     * 
     * @return a list of HashMaps containing the maximum square sizes for each
     *         cluster
     */
    public ArrayList<HashMap<Point, Integer>> calculate() {
        if (getClusters().isEmpty()) {
            return new ArrayList<HashMap<Point, Integer>>();
        }

        addBorderToClusters();
        ArrayList<HashMap<Point, Integer>> maxSquares = new ArrayList<HashMap<Point, Integer>>();
        ArrayList<Integer> maxSquaresSizes = new ArrayList<Integer>();

        for (HashSet<Point> cluster : clusters) {
            for (Point point : cluster) {
                int maxSquareSize = 0;
                while (createGrid(point, maxSquareSize).stream().allMatch(p -> cluster.contains(p))) {
                    maxSquareSize++;
                }
                if (maxSquareSize > 3) {
                    HashMap<Point, Integer> newTile = new HashMap<Point, Integer>();
                    newTile.put(point, maxSquareSize - 1);
                    maxSquares.add(newTile);
                    maxSquaresSizes.add(maxSquareSize - 1);
                }
            }
        }

        int maxSquareSize = maxSquaresSizes.stream()
                .max(Comparator.naturalOrder())
                .get();
        maxSquares = maxSquares.stream()
                .filter(tile -> tile.values().contains(maxSquareSize))
                .collect(Collectors.toCollection(ArrayList::new));
        return maxSquares;
    }

    /**
     * Creates a grid of points starting from the given point and extending
     * a specified number of steps in both the x and y directions.
     * 
     * <p>
     * This method generates a square grid of points with the top-left corner
     * at the specified point. The size of the grid is determined by the number
     * of steps provided.
     * </p>
     * 
     * @param point the starting point of the grid
     * @param steps the number of steps to extend the grid in both x and y
     *              directions
     * @return a HashSet of Points representing the grid
     */
    HashSet<Point> createGrid(Point point, int steps) {
        HashSet<Point> grid = new HashSet<Point>();
        for (int i = 0; i < steps; i++) {
            for (int j = 0; j < steps; j++) {
                grid.add(new Point(point.x() + i, point.y() + j));
            }
        }
        return grid;
    }

    /**
     * Adds borders to the clusters by including neighboring points.
     * 
     * <p>
     * This method checks if borders have already been added. If not, it iterates
     * through each cluster and adds neighboring points to form a border around the
     * cluster. The updated clusters are then set and returned.
     * </p>
     * 
     * @return an ArrayList of HashSets containing the updated clusters with borders
     * @throws RuntimeException if borders have already been added
     */
    ArrayList<HashSet<Point>> addBorderToClusters() {
        if (getBordersAdded()) {
            throw new RuntimeException("Borders already added");
        }
        ArrayList<HashSet<Point>> nextClusters = new ArrayList<HashSet<Point>>();

        for (HashSet<Point> cluster : getClusters()) {
            HashSet<Point> nextCluster = new HashSet<Point>();
            nextClusters.add(nextCluster);

            for (Point point : cluster) {
                nextCluster.add(point);
                ArrayList<Point> neighbors = getNeighborsForPoint(point);
                for (Point neighbor : neighbors) {
                    if (getPoints().contains(neighbor)) {
                        nextCluster.add(neighbor);
                    }
                }
            }
        }
        setBordersAdded(true);
        setClusters(nextClusters);
        return nextClusters;
    }

    /**
     * Retrieves the neighboring points for a given point.
     * 
     * <p>
     * This method generates a list of neighboring points surrounding the specified
     * point. It includes points to the left, right, above, below, and diagonally
     * adjacent to the given point. If an exception occurs while creating a point,
     * it is silently ignored.
     * </p>
     * 
     * @param point the point for which to find the neighbors
     * @return an ArrayList of Points representing the neighbors of the given point
     */
    ArrayList<Point> getNeighborsForPoint(Point point) {
        ArrayList<Point> neighbors = new ArrayList<Point>();
        try {
            neighbors.add(new Point(point.x() - 1, point.y()));
        } catch (Exception e) {
            // do nothing if point is invalid
        }
        try {
            neighbors.add(new Point(point.x(), point.y() - 1));
        } catch (Exception e) {
            // do nothing if point is invalid
        }
        try {
            neighbors.add(new Point(point.x() - 1, point.y() - 1));
        } catch (Exception e) {
            // do nothing if point is invalid
        }
        try {
            neighbors.add(new Point(point.x() + 1, point.y() - 1));
        } catch (Exception e) {
            // do nothing if point is invalid
        }
        try {
            neighbors.add(new Point(point.x() - 1, point.y() + 1));
        } catch (Exception e) {
            // do nothing if point is invalid
        }

        neighbors.add(new Point(point.x() + 1, point.y() + 1));
        neighbors.add(new Point(point.x() + 1, point.y()));
        neighbors.add(new Point(point.x(), point.y() + 1));
        return neighbors;
    }

    public HashSet<Point> getPoints() {
        return points;
    }

    public boolean getBordersAdded() {
        return bordersAdded;
    }

    public void setBordersAdded(boolean bordersAdded) {
        this.bordersAdded = bordersAdded;
    }

    public ArrayList<HashSet<Point>> getClusters() {
        return this.clusters;
    }

    public void setClusters(ArrayList<HashSet<Point>> clusters) {
        this.clusters = clusters;
    }
}
