# Map Tile Metrics

This is a simple tool to calculate metrics for a set of given points/tiles. Both, a tile and a point, are represented by x, y coordinates. The tool calculates the following metrics:

Run the test suite or [check out the companion CLI app](https://github.com/simonneutert/java-map-tile-metrics-cli).

Requires Java 17 or higher.

## Definitions

- **Tile**: A tile is a point in a 2D grid.
- **Point**: A point is a pair of coordinates (x, y).

Both are used interchangeably in this document.

## Scores

- **Cluster Tile**: A (visitied) point/tile in a cluster is defined by having neighbors top/bottom/left/right. Points/tiles that are of the same cluster are connected by a path of neighbors.
- **Clusters**: A cluster is a set of points that are close to each other. 
- **Max Clusters**: The clusters with the most points/tiles.
- **Max Squares**: This scores describes the maximum number of points/tiles that can be connected by a path of neighbors, forming a square.

## A Visualisation is Worth a Thousand Words

The following table shows a grid of points/tiles. 

Coordinates are represented as (x, y) where x is the column and y is the row. The origin (0, 0) is at the top left corner.

- `V` is a (visited) point/tile.
- `c` is a cluster point/tile.
- `C` is a cluster point/tile that is part of the max cluster.
- `Cs` is a cluster point/tile that is part of the max square.
- `Vs` is a (visited) point/tile that is part of the max square.

| 0x0<br>👇 | -   | -   | -   | -   | -   | -   | -   | -   | -   | -   | -   | -   | -   |
| -------- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| V        | V   | V   |     |     |     |     |     |     |     |     |     |     |     |
| V        | Cs  | Cs  | Vs  | Vs  | V   | V   |     |     |     |     |     |     |     |
| V        | Cs  | C   | C   | Vs  |     | V   | V   | V   | V   | V   | V   | V   | V   |
| V        | Cs  | C   | C   | Vs  |     |     |     |     | V   | c   | c   | c   | V   |
|          | Vs  | Vs  | Vs  | Vs  |     |     |     |     |     | V   | V   | V   |     |
|          |     |     |     |     |     |     | V   |     |     |     |     |     |     |
|          |     |     |     |     |     |     | V   |     |     |     |     |     |     |
|          |     |     |     |     |     |     | V   |     |     |     |     |     |     |
|          |     |     |     |     |     |     | Vs  | Vs  | Vs  | Vs  |     |     |     |
|          |     |     |     |     |     |     | Vs  | c   | c   | Vs  |     |     |     |
|          |     |     |     |     |     |     | Vs  | c   | c   | Vs  |     |     |     |
|          |     |     |     |     |     |     | Vs  | Vs  | Vs  | Vs  |     |     |     |

- Max Squares have a score of 4. 
  - One of two max squares in the example located with its top left corner at x=1, y=1.
  - The other's top left tile is located at x=7, y=8.
- Max Clusters have a score of 7. There is just one cluster with size 7.
- Clusters have a score of 7, 4 and 3. There are three clusters.

## Todo

- https://docs.gradle.org/current/samples/sample_building_java_libraries.html#review_the_project_files
- https://docs.gradle.org/current/userguide/publishing_setup.html
