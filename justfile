default:
  @just --list

build:
  ./gradlew test && rm -rf map_tile_metrics/build/libs && ./gradlew build

inspect_build:
  jar tf map_tile_metrics/build/libs/map_tile_metrics-*.jar 

inspect_manifest:
  jar xf map_tile_metrics/build/libs/map_tile_metrics-*.jar META-INF/MANIFEST.MF && cat META-INF/MANIFEST.MF && rm -rf META-INF

test:
  ./gradlew test