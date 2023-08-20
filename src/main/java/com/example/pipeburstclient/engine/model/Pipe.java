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

    public List<List<Point>> calculatePoints(List<Point> startVerticalPoints) {
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
        Point verticalMainPoint = new Point(1.5,50,500000,90,10000,false);
        double density0 = points.get(0).get(0).density;
        double density1 = density0 * 0.5;
        double density_step = 0;
        double velocity0 = points.get(verticalQuantity / 2).get(0).velocity;
        double velocity1 = velocity0 * 0.65;
        double velocity_step = 0;
        double temp0 = 90;
        double temp_step = 0;
        double pressure_step = 0;
        double pressure0 = points.get(0).get(0).pressure;


        for (int i = 0; i < verticalQuantity; i++) {
            for (int j = 1; j < horizontalQuantity; j++) {
                double x = j * deltaLength;
                double y = i * deltaDiameter;
                double temp1 = calc.calcTemp(20,length_b,diameter,points.get((int) ((int) length_b*1.1/deltaLength)).get(j).velocity);
                double pressure1 = calc.calcPressure(500000,1.5,diameter,length_b*1.1);

                Point prevPoint = points.get(i).get(j - 1);
                //double density0 = 10;

                //установить поинту начальную плотность
                if (x < -1 * Math.sqrt(0.1/ diameter) * Math.pow((y - diameter*1.3), 2) + length_b) {
                    points.get(i).get(j).density = density0;
                    points.get(i).get(j).velocity = calc.calcVelocity(1.5,diameter,y,deltaDiameter);
                    points.get(i).get(j).pressure = calc.calcPressure(prevPoint.pressure, points.get(i).get(j).velocity, diameter, deltaLength);
                    points.get(i).get(j).temperature = calc.calcTemp(prevPoint.temperature, deltaLength, diameter, points.get(i).get(j).velocity);

                }
                else if (x > length_b*1.1) {
                    /*if (x < length_b*1.1){
                        if ((j-1)*deltaLength<length_b) {
                            double point_c = 0.1*length_b/deltaLength;
                            density_step = (density0 - density1) / point_c;
                            velocity_step = (velocity0 - velocity1) / point_c;
                        }
                        points.get(i).get(j).density = points.get(i).get(j - 1).density - density_step+calc.generateRandomComponent(points.get(i).get(j - 1).density);
                        points.get(i).get(j).velocity = points.get(i).get(j-1).velocity - velocity_step+calc.generateRandomComponent(points.get(i).get(j - 1).velocity);
                    }
                    else {*/
                        points.get(i).get(j).density = density1;
                        points.get(i).get(j).velocity = calc.calcVelocity(velocity1, diameter, y, deltaDiameter);
                        points.get(i).get(j).pressure = calc.calcPressure(prevPoint.density*0.6, points.get(i).get(j).velocity, diameter, deltaLength);
                        points.get(i).get(j).temperature = calc.calcTemp(prevPoint.temperature*0.95, deltaLength, diameter, points.get(i).get(j).velocity);

                } else {
                    if (points.get(i).get(j - 1).density == density0) {
                        double point_c = /*(int)*/ (length_b*1.1 / deltaLength - j);
                        density_step = (density0 - density1) / point_c;
                        velocity_step = (velocity0 - velocity1) / point_c;
                        temp_step = (temp0-temp1)/point_c;
                        pressure_step = (pressure0-pressure1)/point_c;
                    }
                    if (points.get(i).get(j).density > density1) {
                    }
                    else {
                        points.get(i).get(j).density = density1;
                    }
                    points.get(i).get(j).density = points.get(i).get(j - 1).density - density_step + calc.generateRandomComponent(points.get(i).get(j).density);
                    points.get(i).get(j).velocity = points.get(i).get(j - 1).velocity - velocity_step + 0.5 * calc.generateRandomComponent(points.get(i).get(j - 1).velocity);
                    points.get(i).get(j).pressure = calc.calcPressure(prevPoint.pressure * 0.96, points.get(i).get(j).velocity, diameter, deltaLength) + calc.generateRandomComponent(prevPoint.pressure);
                    points.get(i).get(j).temperature = calc.calcTemp(prevPoint.temperature, deltaLength, diameter, points.get(i).get(j).velocity) - temp_step + calc.generateRandomComponent(temp1);
                    points.get(i).get(j).flow = calc.calcFlow(points.get(i).get(j).velocity, points.get(i).get(j).density);
                    if (points.get(i).get(j).density < density1) {
                        points.get(i).get(j).density = density1;
                    }

                }

            }

        }
    }

}

