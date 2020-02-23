package com.zaki.darkorbit.geometry;

import javafx.util.Pair;

public class Point extends Pair<Integer, Integer> {

    public static final  Point DEFAULT_POINT_0_0 = new Point(0, 0);

    public enum Direction {
        LEFT, RIGHT, NONE
    }

    private Direction direction;

    public Point(int x, int y) {
        this(x, y, Direction.NONE);
    }

    public Point(int x, int y, Direction direction) {
        super(x, y);
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "(" + getKey() + ", " + getValue() + ")";
    }

    public int getX() {
        return getKey();
    }

    public int getY() {
        return getValue();
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isDefault() {
        return this.equals(DEFAULT_POINT_0_0);
    }
}
