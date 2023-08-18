package com.example.pipeburstclient.engine.model;

import com.example.pipeburstclient.engine.math.Calculation;

import java.util.ArrayList;
import java.util.List;

public class Pipe {
    private final double length;
    private final double diameter;
    private final int horizontalQuantity;
    private final int verticalQuantity;
    private final double deltaDiameter;
    private final double deltaLength;

    private List<List<Point>> points;

    public Pipe(double length, double diameter, int horizontalQuantity, int verticalQuantity, List<Point> startVerticalPoints) {
        this.length = length;
        this.diameter = diameter;
        this.horizontalQuantity = horizontalQuantity;
        this.verticalQuantity = verticalQuantity;
        this.deltaDiameter = diameter / (verticalQuantity - 1);
        this.deltaLength = length / (horizontalQuantity - 1);
        this.points = calculatePoints(startVerticalPoints);
    }

    public List<List<Point>> getPoints() {
        return points;
    }

    private void initializePoints() {
        points = new ArrayList<>();
        Point startPoint = new Point(0, 0, 0, 0, 0, false);
        for (int i = 0; i < verticalQuantity; i++) {
            List<Point> row = new ArrayList<>();
            for (int j = 0; j < horizontalQuantity; j++) {
                if (i > 0 && j > 0) {
                    row.add(startPoint);
                } else {
                    row.add(new Point(0, 0, 0, 0, 0, false));
                }
            }
            points.add(row);
        }
    }

    public double getLength() {
        return length;
    }

    public double getDiameter() {
        return diameter;
    }

    public int getHorizontalQuantity() {
        return horizontalQuantity;
    }

    public int getVerticalQuantity() {
        return verticalQuantity;
    }

    public double getDeltaDiameter() {
        return deltaDiameter;
    }

    public double getDeltaLength() {
        return deltaLength;
    }

    public List<List<Point>> calculatePoints(List<Point> startVerticalPoints){
        initializePoints();
        Point verticalMainPoint = startVerticalPoints.get(verticalQuantity / 2);
        Calculation calc = new Calculation();
        for (int i = 0; i < verticalQuantity; i++) {
            //блок для расчета для первого столбца
            points.get(i).set(0, startVerticalPoints.get(i));
            points.get(i).get(0).flow = calc.calcFlow(startVerticalPoints.get(i).velocity, startVerticalPoints.get(i).density);
            //для последующих столбцов
            for (int j = 1; j < horizontalQuantity; j++) {
                double x = j * deltaLength;
                double y = i * deltaDiameter;
                Point prevPoint = points.get(i).get(j - 1);
                double velocity = calc.calcVelocity(verticalMainPoint.velocity, diameter, y, deltaDiameter);
                double pressure = calc.calcPressure(prevPoint.pressure, prevPoint.velocity, diameter, deltaLength);
                double temperature = calc.calcTemp(prevPoint.temperature, deltaLength, diameter, velocity);
                double density = startVerticalPoints.get(0).density;
                boolean isBorderPoint = calc.isBorderPoint(i, startVerticalPoints.size());
                //для потока во втором столбце считается верно, выводит ХУЙНЮ
                double flow = calc.calcFlow(velocity, density);
                Point updatedPoint = new Point(
                        velocity,
                        flow,
                        pressure,
                        temperature,
                        density,
                        isBorderPoint
                );
                points.get(i).set(j, updatedPoint);
            }
        }
        return points;
    }

    public void Properties_with_Burst(double length_b) {
        Calculation calc = new Calculation();
        double density0 = points.get(0).get(0).density;
        double density1 = density0 * 0.6;
        double density_step = 0;
        double velocity0 = points.get(verticalQuantity/2).get(0).velocity;
        double velocity1 = velocity0 * 0.6;
        double velocity_step = 0;
        for (int i = 0; i < verticalQuantity; i++) {
            for (int j = 1; j < horizontalQuantity; j++) {
                double x = j * deltaLength;
                double y = i * deltaDiameter;
                Point prevPoint = points.get(i).get(j - 1);
                //double density0 = 10;

                //установить поинту начальную плотность
                if ((x < -1 * Math.sqrt(1/diameter) *Math.pow(y-diameter, 2) + length_b))
                    points.get(i).get(j).density = density0;
                else {
                    if (points.get(i).get(j - 1).density == density0) {
                        double point_c = /*(int)*/ (length_b / deltaLength - j*0.9);
                        density_step = (density0 - density1) / point_c;
                    }
                    points.get(i).get(j).density = points.get(i).get(j - 1).density - density_step;
                    points.get(i).get(j).velocity *= 0.6;
                    points.get(i).get(j).pressure = calc.calcPressure(prevPoint.pressure, points.get(i).get(j).velocity, diameter, deltaLength);
                    points.get(i).get(j).temperature = calc.calcTemp(prevPoint.temperature, deltaLength, diameter, points.get(i).get(j).velocity);
                    points.get(i).get(j).flow = calc.calcFlow(points.get(i).get(j).velocity, points.get(i).get(j).density);

                }
                if (x > length_b) {
                    points.get(i).get(j).density = density1;
                }


            }

        }
    }

}

