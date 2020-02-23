package com.zaki.darkorbit;

import java.awt.*;

public class Main {

    public static void main(String[] args) throws Exception {

        // get screen resolution
        int endX = 1500;
        int endY = 800;
        int startX = 0;
        int startY = 80;

/*
        while (true)
            System.out.println(MouseInfo.getPointerInfo().getLocation());
*/

        Collector c = new Collector(startX, startY, endX, endY);
        while (true) {
            try {
                c.collect();
            } catch (Exception e) {
                // TODO
                e.printStackTrace();
            }
        }
    }
}
