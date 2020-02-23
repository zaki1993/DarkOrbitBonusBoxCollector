package com.zaki.darkorbit;

import com.zaki.darkorbit.exception.EnemyOnMapException;
import com.zaki.darkorbit.exception.PortalOnMapException;
import com.zaki.darkorbit.geometry.Point;
import javafx.util.Pair;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Collector {

    // TODO check for 1 to 5 enemies, because it changes the color
    private static final RGB enemyColor = new RGB(252,172,44);

    private static final RGB portalJumpIconColor = new RGB(43, 32, 15);

    private static final int NEAR_LAST_POINT_DEST = 30;

    private RGB nearYellowLow;
    private RGB nearYellowHigh;

    private com.zaki.darkorbit.geometry.Point start;
    private com.zaki.darkorbit.geometry.Point end;

    private CollectorsLogger logger = new CollectorsLogger();

    private com.zaki.darkorbit.geometry.Point lastPoint;

    private MapVisiter mapVisiter;

    public Collector(int startX, int startY, int endX, int endY) throws IOException {

        start = new com.zaki.darkorbit.geometry.Point(startX, startY);
        end = new com.zaki.darkorbit.geometry.Point(endX, endY);
        lastPoint = com.zaki.darkorbit.geometry.Point.DEFAULT_POINT_0_0;

        mapVisiter = new MapVisiter();

        Analyzer analyzer = new Analyzer("resources/data");
        nearYellowLow = new RGB(analyzer.getMinRed(), analyzer.getMinGreen(), analyzer.getMinBlue());
        nearYellowHigh = new RGB(analyzer.getMaxRed(), analyzer.getMaxGreen(), analyzer.getMaxBlue());

        System.out.println("Min " + nearYellowLow);
        System.out.println("Max " + nearYellowHigh);
    }

    public void collect() throws AWTException {

        Robot r = new Robot();

        boolean wasEnemyOnMap = false;
        while (true) {
            //testEnemy(r);
            //test(r);
            try {
                // TODO disable pet and enable pet
                boolean isEnemyOnMap = collectInternal(r);
                if (isEnemyOnMap) {
                    wasEnemyOnMap = true;
                    mapVisiter.reset();
                    Thread.sleep(20_000);
                } else {
                    if (wasEnemyOnMap) {
                        wasEnemyOnMap = false;
                    }
                }
            } catch (Exception e) {
                // TODO
            }
        }
    }

    private void testEnemy(Robot r) {
        while (true) {
            BufferedImage img = r.createScreenCapture(new Rectangle(1900, 1180));
            /*for (int y = 630; y < end.getY() - 5; y++) {
                for (int x = 1290; x < end.getX() - 5; x++) {
                    Color c = new Color(img.getRGB(x, y), true);

                    RGB rgb = new RGB(c.getRed(), c.getGreen(), c.getBlue());
                    System.out.println(rgb);
                    //r.mouseMove(x, y);
                }
            }*/
            /*try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            boolean result = isEnemyOnMap(img);
            System.out.println(result);
        }
    }

    private void test(Robot r) {

        int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
        int y = (int) MouseInfo.getPointerInfo().getLocation().getY();

        Color c = new Color(r.createScreenCapture(new Rectangle(1900, 1080)).getRGB(x, y), true);
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();
        RGB rgb = new RGB(red, green, blue);

        System.out.println(red + " " + green + " " + blue + ": is enemy : " + rgb.equals(enemyColor));
    }

    private boolean collectInternal(Robot r) throws AWTException {

        com.zaki.darkorbit.geometry.Point point;
        boolean userHasToClick = false;

        int xPos = 0, yPos = 0;

        boolean isEnemyOnMap = false;
        try {
            point = doubleCheckScreen(r.createScreenCapture(new Rectangle(end.getX(), end.getY())));
            if (point != null) {
                userHasToClick = !point.isDefault();
                xPos = point.getX();
                yPos = point.getY();
            }
        } catch (EnemyOnMapException eome) {
            isEnemyOnMap = true;
        } catch (PortalOnMapException pome) {
            userHasToClick = false;
        }

        if (!userHasToClick) {
            if (!isEnemyOnMap) {
                // If there is no enemy, then we are good and we can continue collecting.
                com.zaki.darkorbit.geometry.Point nextCoordinate = mapVisiter.getNextPoint();
                xPos = nextCoordinate.getX();
                yPos = nextCoordinate.getY();
            } else {
                // If there is an enemy on the map, it is better to go on a safe place such as a portal.
                com.zaki.darkorbit.geometry.Point safePortal = mapVisiter.getSafePortal();
                if (safePortal != null) {
                    xPos = safePortal.getX();
                    yPos = safePortal.getY();
                }
            }
        }

        int waitMs = userHasToClick ? 1700 : 3500;
        click(xPos, yPos, waitMs);

        return isEnemyOnMap;
    }

    private com.zaki.darkorbit.geometry.Point doubleCheckScreen(BufferedImage screenCapture) throws AWTException, EnemyOnMapException {

        com.zaki.darkorbit.geometry.Point point = checkScreen(screenCapture);
        if (point.getX() == 0 && point.getX() == 0) {
            point = checkScreen(screenCapture);
        }

        return point;
    }

    private void click(int x, int y, int timeToWait) throws AWTException {

        Robot r = new Robot();

        r.mouseMove(x, y);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        try {
            Thread.sleep(timeToWait);
        } catch (Exception e) {
            // TODO
        }
    }

    private com.zaki.darkorbit.geometry.Point checkScreen(BufferedImage screenImg) throws AWTException, EnemyOnMapException {

        if (isEnemyOnMap(screenImg)) {
            throw new EnemyOnMapException();
        }

        com.zaki.darkorbit.geometry.Point.Direction lastPointDirection = mapVisiter.getLastPointDirection();

        boolean userHasToClick = false;

        com.zaki.darkorbit.geometry.Point result = com.zaki.darkorbit.geometry.Point.DEFAULT_POINT_0_0;

        if (lastPointDirection == com.zaki.darkorbit.geometry.Point.Direction.RIGHT) {
            for (int y = start.getY() + 5; y < end.getY(); y += 4) {
                for (int x = start.getX() + 5; x < end.getX() - 5; x += 3) {
                    // remove map pixels
                    if (x >= 1290 && y >= 630) {
                        // these are mini map coordinates
                        continue;
                    }

                    RGB pixel = getClickablePixel(screenImg, x, y);
                    if (pixel != null) {

                        // check if we clicked near that point of 5px last time
                        int lastX = lastPoint.getX();
                        int lastY = lastPoint.getY();
                        boolean isNearLastPoint = x - NEAR_LAST_POINT_DEST <= lastX && lastX <= x + NEAR_LAST_POINT_DEST && y - NEAR_LAST_POINT_DEST <= lastY && lastY <= y + NEAR_LAST_POINT_DEST;
                        if (!isNearLastPoint) {
                            userHasToClick = true;
                            lastPoint = new com.zaki.darkorbit.geometry.Point(x, y);
                            result = lastPoint;
                            logger.onCollect(pixel);

                            break;
                        }
                    }
                }
                if (userHasToClick) {
                    break;
                }
            }
        } else {
            for (int y = end.getY() - 5; y > start.getY(); y -= 4) {
                for (int x = end.getX() - 5; x > start.getX(); x -= 3) {
                    // remove map pixels
                    if (x >= 1290 && y >= 630) {
                        // these are mini map coordinates
                        continue;
                    }

                    RGB pixel = getClickablePixel(screenImg, x, y);
                    if (pixel != null) {
                        // check if we clicked near that point of 5px last time
                        int lastX = lastPoint.getX();
                        int lastY = lastPoint.getY();
                        boolean isNearLastPoint = x - NEAR_LAST_POINT_DEST <= lastX && lastX <= x + NEAR_LAST_POINT_DEST && y - NEAR_LAST_POINT_DEST <= lastY && lastY <= y + NEAR_LAST_POINT_DEST;
                        if (!isNearLastPoint) {
                            userHasToClick = true;
                            lastPoint = new Point(x, y);
                            result = lastPoint;
                            logger.onCollect(pixel);

                            break;
                        }
                    }
                }
                if (userHasToClick) {
                    break;
                }
            }
        }

        return result;
    }

    private Pair<Boolean, RGB> isPixelClickable(BufferedImage screenImg, int x, int y) {

        Color c = new Color(screenImg.getRGB(x, y), true);
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();

        RGB pixel = new RGB(red, green, blue);

        if (pixel.equals(portalJumpIconColor)) {
            throw new PortalOnMapException();
        }

        boolean result = pixel.isPixelBetweenPixels(nearYellowLow, nearYellowHigh);

        return new Pair<>(result, pixel);
    }

    private RGB getClickablePixel(BufferedImage screenImg, int x, int y) {

        Pair<Boolean, RGB> shouldPixelBeCollected = isPixelClickable(screenImg, x, y);
        Pair<Boolean, RGB> shouldLeftPixelBeCollected = isPixelClickable(screenImg, x - 1, y);
        Pair<Boolean, RGB> shouldRightPixelBeCollected = isPixelClickable(screenImg, x + 1, y);
       // Pair<Boolean, RGB> shouldTopPixelBeCollected = isPixelClickable(screenImg, x, y - 1);
       // Pair<Boolean, RGB> shouldBottomPixelBeCollected = isPixelClickable(screenImg, x, y + 1);

        return shouldPixelBeCollected.getKey() &&
               shouldLeftPixelBeCollected.getKey() &&
               shouldRightPixelBeCollected.getKey()
                //&&
              // shouldTopPixelBeCollected.getKey() &&
              // shouldBottomPixelBeCollected.getKey()
                ? shouldPixelBeCollected.getValue() : null;
    }

    private boolean isEnemyOnMap(BufferedImage screenImg) {

        // Traverse mini map for enemy. This is usually shown in low level maps as a yellow rectangle on the mini map.
        for (int y = 630; y < end.getY() - 5; y++) {
            for (int x = 1290; x < end.getX() - 5; x++) {
                Color c = new Color(screenImg.getRGB(x, y), true);

                RGB rgb = new RGB(c.getRed(), c.getGreen(), c.getBlue());
                if(rgb.equals(enemyColor)) {
                   return true;
                }
            }
        }

        return false;
    }
}
