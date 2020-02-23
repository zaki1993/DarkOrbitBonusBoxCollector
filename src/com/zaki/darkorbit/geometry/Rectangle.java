package com.zaki.darkorbit.geometry;

public class Rectangle {

    private Point x;
    private Point y;
    private Point z;
    private Point u;


    public Rectangle(Point x, Point y, Point z, Point u) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.u = u;
    }

    public Point getX() {
        return x;
    }

    public Point getY() {
        return y;
    }

    public Point getZ() {
        return z;
    }

    public Point getU() {
        return u;
    }
}
