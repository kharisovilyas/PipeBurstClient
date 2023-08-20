package com.example.pipeburstclient.engine.math;
import java.util.Random;

public class Calculation {
    public final double CONST_1 = 1;
    public final double CONST_2 = 4200;
    public final double CONST_3 = 0.3;
    public final double CONST_4 = 1000;

    public final double CONST_5 = 1.9;
    public final double CONST_6 = 0.6;

    public double calcFlow(double velocity, double density) {
        return velocity * density * 0.001;
    }

    public double calcTemp(double startTemp, double deltaLength, double diameter, double velocity) {
        return 20 + (startTemp - 20) * Math.exp(-1 * (CONST_1 * deltaLength) / (CONST_2 * CONST_3 * Math.PI * (diameter / 4) * CONST_4) * velocity)+generateRandomComponent(startTemp);
    }

    public double generateRandomComponent(double realComponent) {
        double max = realComponent * 0.01;
        double min = realComponent * (-0.01);
        return new Random().nextDouble() * (max - min) + min;
    }

    public double calcVelocity(double velocityStartPoint, double diameter, double y, double deltaDiameter) {
        double semiDiam = diameter / 2;
        double centring = semiDiam >= y ? semiDiam - y : y - semiDiam;
        return velocityStartPoint * (1 - CONST_6 * Math.pow(Math.abs(centring), 2) / Math.pow(diameter, 2)) + generateRandomComponent(velocityStartPoint)*5;
    }

    public double calcPressure(double startPressure, double startVelocity, double diameter, double deltaLength) {
        return startPressure - (CONST_5 * CONST_4 * Math.pow(startVelocity, 2) * deltaLength) / (2 * diameter);
    }

    public boolean isBorderPoint(int row, int length) {
        return row == 0 || row == length;
    }
    public double calcAnyDelta(double startValue, double endValue){
        return Math.abs(startValue - endValue);
    }

}