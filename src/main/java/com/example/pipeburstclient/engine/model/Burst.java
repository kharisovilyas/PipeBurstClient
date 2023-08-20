package com.example.pipeburstclient.engine.model;

import com.example.pipeburstclient.engine.math.Calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Burst {
    private double deltaVelocity;
    private double deltaFlow;
    private double deltaPressure;
    private double deltaTemperature;
    private double deltaDensity;
    private final Pipe startPipe;

    public List<List<Map<String, Double>>> getDeltas() {
        return deltas;
    }

    private List<List<Map<String, Double>>> deltas = new ArrayList<>();

    public Burst(Pipe startPipe) {
        this.startPipe = startPipe;
    }

    public double getDeltaVelocity() {
        return deltaVelocity;
    }

    public void setDeltaVelocity(double deltaVelocity) {
        this.deltaVelocity = deltaVelocity;
    }

    public double getDeltaFlow() {
        return deltaFlow;
    }

    public void setDeltaFlow(double deltaFlow) {
        this.deltaFlow = deltaFlow;
    }

    public double getDeltaPressure() {
        return deltaPressure;
    }

    public void setDeltaPressure(double deltaPressure) {
        this.deltaPressure = deltaPressure;
    }

    public double getDeltaTemperature() {
        return deltaTemperature;
    }

    public void setDeltaTemperature(double deltaTemperature) {
        this.deltaTemperature = deltaTemperature;
    }

    public double getDeltaDensity() {
        return deltaDensity;
    }

    public void setDeltaDensity(double deltaDensity) {
        this.deltaDensity = deltaDensity;
    }

    public void optimumSensorPlacement(
            List<Double> pipeDiameters,  // параметры от 3мм до 53мм
            List<Double> sensorsSteps   //  параметры от 1м до len Pipe
    ) {
        double diameter = startPipe.getDiameter();
        int verticalQuantity = startPipe.getVerticalQuantity();
        List<Point> startVerticalPoints = startPipe.getPoints().get(0);

        for (Double pipeDiameter : pipeDiameters) {
            // Создайте новый список для каждой итерации
            List<Map<String, Double>> deltaList = new ArrayList<>();

            for (double length : sensorsSteps) {
                Map<String, Double> deltaMap = new HashMap<>();

                Calculation calculation = new Calculation();
                Pipe testingPipe = new Pipe(
                        length, diameter, startPipe.getHorizontalQuantity(), verticalQuantity, startVerticalPoints
                );
                testingPipe.calculationBurstPipePoints(length / 2, 0.1 - pipeDiameter);

                deltaMap.put("Velocity", calculation.calcAnyDelta(
                        testingPipe.getPoints().get(0).get(0).velocity,
                        testingPipe.getPoints().get(0).get(startPipe.getHorizontalQuantity() - 1).velocity
                ));
                deltaMap.put("Flow", calculation.calcAnyDelta(
                        testingPipe.getPoints().get(0).get(0).flow,
                        testingPipe.getPoints().get(0).get(startPipe.getHorizontalQuantity() - 1).flow
                ));
                deltaMap.put("Pressure", calculation.calcAnyDelta(
                        testingPipe.getPoints().get(0).get(0).pressure,
                        testingPipe.getPoints().get(0).get(startPipe.getHorizontalQuantity() - 1).pressure
                ));
                deltaMap.put("Temperature", calculation.calcAnyDelta(
                        testingPipe.getPoints().get(0).get(0).temperature,
                        testingPipe.getPoints().get(0).get(startPipe.getHorizontalQuantity() - 1).temperature
                ));
                deltaMap.put("Density", calculation.calcAnyDelta(
                        testingPipe.getPoints().get(0).get(0).density,
                        testingPipe.getPoints().get(0).get(startPipe.getHorizontalQuantity() - 1).density
                ));

                // Добавьте deltaMap во внутренний список deltaList
                deltaList.add(deltaMap);
            }

            // Добавьте deltaList во внешний список deltas
            deltas.add(deltaList);
        }
    }
}

