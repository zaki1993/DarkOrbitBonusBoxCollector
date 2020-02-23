package com.zaki.darkorbit;

import com.zaki.darkorbit.geometry.Point;
import com.zaki.darkorbit.geometry.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DarkOrbitMap {

    public enum MapType {
        MMO_1_2("1.2"),
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

    private static final Map<MapType, Point> safePortal = new HashMap<>();

    static {
        safePortal.put(MapType.MMO_1_2, new Point(1341, 702));
        safePortal.put(MapType.MMO_1_8, new Point(1341, 702)); // TODO verify this
        safePortal.put(MapType.MMO_4_1, new Point(1341, 702)); // TODO verify this
    }

    private static final Map<MapType, List<Rectangle>> skipCoordinates = new HashMap<>();

    static {
        skipCoordinates.put(MapType.MMO_1_2, List.of(new Rectangle(new Point(1330,692), new Point(1353,692), new Point(1353,711), new Point(1330,711)),
                                                     new Rectangle(new Point(1484,692), new Point(1510,692), new Point(1510,711), new Point(1484,711)),
                                                     new Rectangle(new Point(1484,781), new Point(1510,781), new Point(1510,801), new Point(1484,801))));
       // skipCoordinates.put(MapType.MMO_1_8, List.of(new Point(1330, 727), new Point(1356,727), new Point(1356, 767),new Point(1330, 767)));
    }

    /**
     * Returns list of coordinates which will be traversed by the ship.
     */
    public static List<Point> getMap(MapType mapType) {

        List<Point> result = new ArrayList<>();

        // Get all coordinates which should be skipped such as bases, portals, etc.
        List<Rectangle> coordinatesToSkip = skipCoordinates.get(mapType);

        boolean goRightOnXAxis = true;
        // 692 is position 6 as Y in the mini map
        // 1413 is position 125 as Y in the mini map
        for (int y = 692; y <= 1350; y+=8) {

            // 1334 is position 6 as X in the mini map
            // 1510 is position 200 as X in the mini map
            if (goRightOnXAxis) {
                for (int x = 1330; x <= 1510; x += 12) {

                    boolean isYInBase = false;
                    boolean isXInBase = false;
                    /*for (Rectangle r : coordinatesToSkip) {
                        Point xB = r.getX();
                        Point yB = r.getY();
                        Point zB = r.getZ();
                        isYInBase |= xB.getY() <= y && zB.getY() <= y;
                        isXInBase |= xB.getX() <= x && x <= yB.getX();
                    }
                    if (!isXInBase && !isYInBase) {*/
                        result.add(new Point(x, y, Point.Direction.RIGHT));
                   // }
                }
            } else {
                // going backward on X axis
                // This should imitate process like
                // -> 1 2 3 4 5
                //   10 9 8 7 6  <-
                for (int x = 1510; x >= 1330; x -= 12) {
                    /*boolean isYInBase = false;
                    boolean isXInBase = false;
                    for (Rectangle r : coordinatesToSkip) {
                        Point xB = r.getX();
                        Point yB = r.getY();
                        Point zB = r.getZ();
                        isYInBase |= xB.getY() <= y && zB.getY() <= y;
                        isXInBase |= xB.getX() <= x && x <= yB.getX();
                    }
                    if (!isXInBase || !isYInBase) {*/
                        result.add(new Point(x, y, Point.Direction.LEFT));
                   // }
                }
            }
            goRightOnXAxis = !goRightOnXAxis;
        }

        return result;
    }

    public static Point getSafePortal(MapType mapType) {
        return safePortal.get(mapType);
    }
}
