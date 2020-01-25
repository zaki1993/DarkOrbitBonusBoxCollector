package com.zaki.darkorbit;

public class Point {

    public enum Direction {
        LEFT, RIGHT, NONE
    }

    private int x;
    private int y;

    private Direction direction;

    Point(int x, int y) {
        this(x, y, Direction.NONE);
    }

    Point(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }
}
