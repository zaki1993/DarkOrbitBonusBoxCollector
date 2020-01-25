package com.zaki.darkorbit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DarkOrbitMap {

    public enum MapType {
        MMO_1_8("1.8"),
        MMO_4_1("4.1");

        private String name;

        MapType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final Map<MapType, List<Point>> baseCoordinates = new HashMap<>();

    static {
        baseCoordinates.put(MapType.MMO_1_8, List.of(new Point(1330, 727), new Point(1356,727), new Point(1356, 767),new Point(1330, 767)));
        baseCoordinates.put(MapType.MMO_4_1, List.of(new Point(0, 0), new Point(0,0), new Point(0, 0),new Point(0, 0)));
    }

    public static List<Point> getMap(MapType mapType) {

        List<Point> result = new ArrayList<>();

        // Base coordinates on mini map
        List<Point> base = baseCoordinates.get(mapType);
        Point xB = base.get(0);
        Point yB = base.get(1);
        Point zB = base.get(2);
        Point uB = base.get(3);

        boolean goRightOnXAxis = true;
        // 692 is position 6 as Y in the mini map
        // 1413 is position 125 as Y in the mini map
        for (int y = 692; y <= 1413; y+=6) {

            boolean isYInBase = xB.getY() <= y && zB.getY() <= y;
            // 1334 is position 6 as X in the mini map
            // 1510 is position 200 as X in the mini map
            if (goRightOnXAxis) {
                for (int x = 1330; x <= 1510; x += 12) {
                    boolean isXInBase = xB.getX() <= x && x <= yB.getX();
                    if (!isXInBase || !isYInBase) {
                        result.add(new Point(x, y, Point.Direction.RIGHT));
                    }
                }
            } else {
                // going backward on X axis
                // This should imitate process like
                // -> 1 2 3 4 5
                //   10 9 8 7 6  <-
                for (int x = 1510; x >= 1330; x -= 12) {
                    boolean isXInBase = xB.getX() >= x && x >= yB.getX();
                    if (!isXInBase || !isYInBase) {
                        result.add(new Point(x, y, Point.Direction.LEFT));
                    }
                }
            }
            goRightOnXAxis = !goRightOnXAxis;
        }

        return result;
    }
}
