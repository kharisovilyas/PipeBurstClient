package com.example.pipeburstclient.engine.model;
public class Point {
    public double velocity;
    public double flow;
    public double pressure;
    public double temperature;
    public double density;
    boolean isBorderPoint;

    public Point(double velocity, double flow, double pressure, double temperature, double density, boolean isBorderPoint) {
        this.velocity = velocity;
        this.flow = flow;
        this.pressure = pressure;
        this.temperature = temperature;
        this.density = density;
        this.isBorderPoint = isBorderPoint;
    }

}