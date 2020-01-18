package com.zaki.darkorbit;

import java.awt.*;

public class Main {

    public static void main(String[] args) throws AWTException {

        // get screen resolution
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int width = dim.width;
        int height = dim.height;
        int startY = 80;

        Collector c = new Collector(0, startY, width, height - startY);
        c.collect();
    }
}
