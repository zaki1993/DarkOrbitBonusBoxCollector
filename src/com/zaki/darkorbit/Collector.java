package com.zaki.darkorbit;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Collector {

    private static final int NEAR_LAST_POINT_DEST = 20;

    private int NEAR_YELLOW_RED_LOW;

    private int NEAR_YELLOW_GREEN_LOW;

    private int NEAR_YELLOW_BLUE_LOW;

    private int NEAR_YELLOW_RED_TOP;

    private int NEAR_YELLOW_GREEN_TOP;

    private int NEAR_YELLOW_BLUE_TOP;

    private int startX;

    private int startY;

    private int endX;

    private int endY;

    private CollectorsLogger logger = new CollectorsLogger();

    private int lastX;

    private int lastY;

    private MapVisiter mapVisiter;

    public Collector(int startX, int startY, int endX, int endY) throws IOException {

        setStartX(startX);
        setStartY(startY);
        setEndX(endX);
        setEndY(endY);
        mapVisiter = new MapVisiter();

        Analyzer analyzer = new Analyzer("resources/data");
        NEAR_YELLOW_RED_LOW = analyzer.getMinRed();
        NEAR_YELLOW_GREEN_LOW = analyzer.getMinGreen();
        NEAR_YELLOW_BLUE_LOW = analyzer.getMinBlue();
        NEAR_YELLOW_RED_TOP = analyzer.getMaxRed();
        NEAR_YELLOW_GREEN_TOP = analyzer.getMaxGreen();
        NEAR_YELLOW_BLUE_TOP = analyzer.getMaxBlue();

        System.out.println("Min rgb(" + NEAR_YELLOW_RED_LOW + ", " + NEAR_YELLOW_GREEN_LOW + ", " + NEAR_YELLOW_BLUE_LOW + ")");
        System.out.println("Max rgb(" + NEAR_YELLOW_RED_TOP + ", " + NEAR_YELLOW_GREEN_TOP + ", " + NEAR_YELLOW_BLUE_TOP + ")");

        lastX = 0;
        lastY = 0;
    }

    private void setStartX(int startX) {
        this.startX = startX;
    }

    private void setStartY(int startY) {
        this.startY = startY;
    }

    private void setEndX(int endX) {
        this.endX = endX;
    }

    private void setEndY(int endY) {
        this.endY = endY;
    }

    public void collect() throws AWTException {

        Robot r = new Robot();

        int nextCoordinateToVisitIndex = 0;

        while (true) {

            int[] coords = doubleCheckScreen(r.createScreenCapture(new Rectangle(endX, endY)));
            int xPos = coords[0];
            int yPos = coords[1];
            boolean userHasToClick = xPos != 0 && yPos != 0;
        /*
            int[] coords = new int[]{0, 0};
            int xPos = coords[0];
            int yPos = coords[1];
            boolean userHasToClick = xPos != 0 && yPos != 0;

            int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
            int y = (int) MouseInfo.getPointerInfo().getLocation().getY();

            Color c = new Color(r.createScreenCapture(new Rectangle(endX, endY)).getRGB(x, y));

            int red = c.getRed();
            int green = c.getGreen();
            int blue = c.getBlue();

            if (red >= NEAR_YELLOW_RED_LOW && green >= NEAR_YELLOW_GREEN_LOW && blue >= NEAR_YELLOW_BLUE_LOW &&
                    red <= NEAR_YELLOW_RED_TOP && green <= NEAR_YELLOW_GREEN_TOP && blue <= NEAR_YELLOW_BLUE_TOP) {
                System.out.println("OK MATCH");
            }
            System.out.println(red + " " + green + " " + blue);
        */

            if (!userHasToClick) {
                Point nextCoordinate = mapVisiter.getNextPoint();
                xPos = nextCoordinate.getX();
                yPos = nextCoordinate.getY();
            }

            int waitMs = userHasToClick ? 1200 : 4500;
            click(xPos, yPos, waitMs);
        }
    }

    private int[] doubleCheckScreen(BufferedImage screenCapture) throws AWTException {

        int[] coords = checkScreen(screenCapture);
        if (coords[0] == 0 && coords[1] == 0) {
            coords = checkScreen(screenCapture);
        }

        return coords;
    }

    private void click(int x, int y, int timeToWait) throws AWTException {

        Robot r = new Robot();

        r.mouseMove(x, y);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        try {
            Thread.sleep(timeToWait);
        } catch (Exception e) {

        }
    }

    private int[] checkScreen(BufferedImage screenImg) throws AWTException {

        Point.Direction lastPointDirection = mapVisiter.getLastPointDirection();

        boolean userHasToClick = false;
        int xPos = 0, yPos = 0;

        if (lastPointDirection == Point.Direction.RIGHT) {
            for (int y = startY; y < endY; y += 4) {
                for (int x = startX; x < endX; x += 3) {
                    // remove map pixels
                    if (x >= 1290 && y >= 630) {
                        // these are mini map coordinates
                        continue;
                    }

                    if (isPixelClickable(screenImg, x, y) && isPixelClickable(screenImg, x - 1, y) && isPixelClickable(screenImg, x + 1, y)) {

                        // check if we clicked near that point of 5px last time
                        boolean isNearLastPoint = x - NEAR_LAST_POINT_DEST <= lastX && lastX <= x + NEAR_LAST_POINT_DEST && y - NEAR_LAST_POINT_DEST <= lastY && lastY <= y + NEAR_LAST_POINT_DEST;
                        if (!isNearLastPoint) {
                            userHasToClick = true;
                            xPos = x;
                            yPos = y;
                            lastX = xPos;
                            lastY = yPos;

                            break;
                        }
                    }
                }
                if (userHasToClick) {
                    break;
                }
            }
        } else {
            for (int y = endY - 1; y >= startY; y -= 4) {
                for (int x = endX - 1; x >= startX ; x -= 3) {
                    // remove map pixels
                    if (x >= 1290 && y >= 630) {
                        // these are mini map coordinates
                        continue;
                    }

                    if (isPixelClickable(screenImg, x, y) && isPixelClickable(screenImg, x - 1, y) && isPixelClickable(screenImg, x + 1, y)) {

                        // check if we clicked near that point of 5px last time
                        boolean isNearLastPoint = x - NEAR_LAST_POINT_DEST <= lastX && lastX <= x + NEAR_LAST_POINT_DEST && y - NEAR_LAST_POINT_DEST <= lastY && lastY <= y + NEAR_LAST_POINT_DEST;
                        if (!isNearLastPoint) {
                            userHasToClick = true;
                            xPos = x;
                            yPos = y;
                            lastX = xPos;
                            lastY = yPos;

                            break;
                        }
                    }
                }
                if (userHasToClick) {
                    break;
                }
            }
        }

        return new int[] {xPos, yPos};
    }

    private boolean isPixelClickable(BufferedImage screenImg, int x, int y) {

        Color c = new Color(screenImg.getRGB(x, y), true);
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();

        boolean result = red >= NEAR_YELLOW_RED_LOW && green >= NEAR_YELLOW_GREEN_LOW && blue >= NEAR_YELLOW_BLUE_LOW &&
                         red <= NEAR_YELLOW_RED_TOP && green <= NEAR_YELLOW_GREEN_TOP && blue <= NEAR_YELLOW_BLUE_TOP;

        if (result) {
            logger.onCollect(red, green, blue);
        }

        return result;
    }
}
