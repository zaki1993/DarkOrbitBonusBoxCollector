package com.zaki.darkorbit;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Collector {

    private static class Point {

        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    private static List<Point> mapCoordinates = new ArrayList<>();

    // Builds List with mini map coordinates which the program will visit
    static {
        boolean goRightOnXAxis = true;
        // 692 is position 6 as Y in the mini map
        // 1413 is position 125 as Y in the mini map
        for (int y = 692; y <= 1413; y+=6) {

            // 1334 is position 6 as X in the mini map
            // 1510 is position 200 as X in the mini map
            if (goRightOnXAxis) {
                for (int x = 1334; x <= 1510; x += 12) {
                    mapCoordinates.add(new Point(x, y));
                }
            } else {
                // going backward on X axis
                // This should imitate process like
                // -> 1 2 3 4 5
                //   10 9 8 7 6  <-
                for (int x = 1510; x >= 1334; x -= 12) {
                    mapCoordinates.add(new Point(x, y));
                }
            }
            goRightOnXAxis = !goRightOnXAxis;
        }
    }

    private static final int NEAR_YELLOW_RED_LOW = 245;

    private static final int NEAR_YELLOW_GREEN_LOW = 230;

    private static final int NEAR_YELLOW_BLUE_LOW = 170;

    private static final int NEAR_YELLOW_RED_TOP = 255;

    private static final int NEAR_YELLOW_GREEN_TOP = 250;

    private static final int NEAR_YELLOW_BLUE_TOP = 255;

    private int startX;

    private int startY;

    private int endX;

    private int endY;

    private CollectorsLogger logger = new CollectorsLogger();

    public Collector(int startX, int startY, int endX, int endY) {
        setStartX(startX);
        setStartY(startY);
        setEndX(endX);
        setEndY(endY);
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
        boolean reverseCoordinateVisitation = false;

        while (true) {
            if (reverseCoordinateVisitation && nextCoordinateToVisitIndex == 0) {
                reverseCoordinateVisitation = false;
            } else {
                if (nextCoordinateToVisitIndex == mapCoordinates.size() - 1) {
                    reverseCoordinateVisitation = true;
                }
            }
            int[] coords = checkScreen(r.createScreenCapture(new Rectangle(endX, endY)), r);
            int xPos = coords[0];
            int yPos = coords[1];
            boolean userHasToClick = xPos != 0 && yPos != 0;

            /*int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
            int y = (int) MouseInfo.getPointerInfo().getLocation().getY();

            Color c = new Color(r.createScreenCapture(new Rectangle(endX, endY)).getRGB(x, y));

            int red = c.getRed();
            int green = c.getGreen();
            int blue = c.getBlue();

            if (red >= NEAR_YELLOW_RED_LOW && green >= NEAR_YELLOW_GREEN_LOW && blue >= NEAR_YELLOW_BLUE_LOW &&
                    red <= NEAR_YELLOW_RED_TOP && green <= NEAR_YELLOW_GREEN_TOP && blue <= NEAR_YELLOW_BLUE_TOP) {
                System.out.println("OK MATCH");
            }
            System.out.println(red + " " + green + " " + blue);*/

            if (!userHasToClick) {
                Point nextCoordinate = mapCoordinates.get(nextCoordinateToVisitIndex);
                xPos = nextCoordinate.x;
                yPos = nextCoordinate.y;
                if (reverseCoordinateVisitation) {
                    nextCoordinateToVisitIndex--;
                } else {
                    nextCoordinateToVisitIndex++;
                }
            }

            int waitMs = userHasToClick ? 2000 : 4500;
            click(xPos, yPos, waitMs);
        }
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

    private int[] checkScreen(BufferedImage screenImg, Robot r) throws AWTException {

        boolean userHasToClick = false;
        int xPos = 0, yPos = 0;
        for (int y = startY; y < endY; y+=4) {
            for (int x = startX; x < endX; x+=3) {
                // remove map pixels
                if (x >= 1290 && y >= 630) {
                    // these are mini map coordinates
                    continue;
                }

                if (isPixelClickable(screenImg, x, y) && isPixelClickable(screenImg, x - 1, y) && isPixelClickable(screenImg, x + 1, y)) {
                    userHasToClick = true;
                    xPos = x;
                    yPos = y;

                    break;
                }
            }
            if (userHasToClick) {
                break;
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
