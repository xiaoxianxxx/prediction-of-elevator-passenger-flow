package edu.zsc.utils;

public class DoubleUtils {
    public static boolean isEqual(double a1, double a2) {
        return Math.abs(a1 - a2) < 1e-6;
    }
}
