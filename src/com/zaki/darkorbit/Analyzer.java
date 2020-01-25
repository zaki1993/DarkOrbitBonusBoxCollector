package com.zaki.darkorbit;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Analyzer {

    private static final int MIN_RGB = 0;
    private static final int MAX_RGB = 255;

    private final int minRed;
    private final int maxRed;

    private final int minGreen;
    private final int maxGreen;

    private final int minBlue;
    private final int maxBlue;

    public Analyzer(String fileName) throws IOException {

        int minRed = MAX_RGB;
        int maxRed = MIN_RGB;

        int minGreen = MAX_RGB;
        int maxGreen = MIN_RGB;

        int minBlue = MAX_RGB;
        int maxBlue = MIN_RGB;

        try(Scanner sc = new Scanner(new File(fileName))) {
            String line = null;
            while (sc.hasNext() && (line = sc.nextLine()) != null) {
                String[] parts = line.split(" ");
                int red = Integer.parseInt(parts[0]);
                int green = Integer.parseInt(parts[1]);
                int blue = Integer.parseInt(parts[2]);

                // process red color
                if (red < minRed) {
                    minRed = red;
                }
                if (red > maxRed) {
                    maxRed = red;
                }

                // process green color
                if (green < minGreen) {
                    minGreen = green;
                }
                if (green > maxGreen) {
                    maxGreen = green;
                }

                // process blue color
                if (blue < minBlue) {
                    minBlue = blue;
                }
                if (blue > maxBlue) {
                    maxBlue = blue;
                }
            }
        }

        this.minRed = minRed;
        this.minGreen = minGreen;
        this.minBlue = minBlue;
        this.maxRed = maxRed;
        this.maxGreen = maxGreen;
        this.maxBlue = maxBlue;
    }

    public int getMinRed() {
        return minRed;
    }

    public int getMaxRed() {
        return maxRed;
    }

    public int getMinGreen() {
        return minGreen;
    }

    public int getMaxGreen() {
        return maxGreen;
    }

    public int getMinBlue() {
        return minBlue;
    }

    public int getMaxBlue() {
        return maxBlue;
    }
}
