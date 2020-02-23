package com.zaki.darkorbit;

import java.util.Objects;

public class RGB {

    private int red;
    private int green;
    private int blue;

    public RGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    @Override
    public String toString() {
        return "rgb(" + red + ", " + green + ", " + blue + ")";
    }

    public boolean isPixelBetweenPixels(RGB nearYellowLow, RGB nearYellowHigh) {
        return red >= nearYellowLow.getRed() && green >= nearYellowLow.getGreen() && blue >= nearYellowLow.getBlue() &&
               red <= nearYellowHigh.getRed() && green <= nearYellowHigh.getGreen() && blue <= nearYellowHigh.getBlue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RGB rgb = (RGB) o;
        return red == rgb.red &&
                green == rgb.green &&
                blue == rgb.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue);
    }
}
