package de.simon_neutert.map_tile_metrics;

/**
 * A record representing a point in a 2D space.
 *
 * @param x the x-coordinate of the point
 * @param y the y-coordinate of the point
 */
public record Point(int x, int y) {
    public Point {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("x and y must be positive");
        }
    }
}
