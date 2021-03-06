package com.zaki.darkorbit;

import com.zaki.darkorbit.geometry.Point;

import java.util.List;

public class MapVisiter {

    private static List<Point> mapCoordinates = DarkOrbitMap.getMap(DarkOrbitMap.MapType.MMO_1_2);

    private int nextCoordinateToVisitIndex;

    private boolean reverseCoordinateVisitation;

    private Point lastPoint;

    public MapVisiter() {
        this.nextCoordinateToVisitIndex = 0;
        this.reverseCoordinateVisitation = false;
        this.lastPoint = null;
    }

    private boolean isReverseVisitation() {

        if (reverseCoordinateVisitation && nextCoordinateToVisitIndex == 0) {
            reverseCoordinateVisitation = false;
        } else {
            if (nextCoordinateToVisitIndex == mapCoordinates.size() - 1) {
                reverseCoordinateVisitation = true;
            }
        }

        return reverseCoordinateVisitation;
    }

    public Point getNextPoint() {

        Point result = mapCoordinates.get(nextCoordinateToVisitIndex);

        if (isReverseVisitation()) {
            nextCoordinateToVisitIndex--;
        } else {
            nextCoordinateToVisitIndex++;
        }

        this.lastPoint = result;

        return result;
    }

    public Point.Direction getLastPointDirection() {
        return lastPoint == null ? Point.Direction.NONE : lastPoint.getDirection();
    }

    public Point getSafePortal() {
        return DarkOrbitMap.getSafePortal(DarkOrbitMap.MapType.MMO_1_2); // TODO get this as input
    }

    public void reset() {
        this.nextCoordinateToVisitIndex = 0;
        this.reverseCoordinateVisitation = false;
        this.lastPoint = null;
    }
}
