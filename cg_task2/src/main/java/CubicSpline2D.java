package main.java;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

public class CubicSpline2D {
    private final CubicSpline splineX;
    private final CubicSpline splineY;

    public CubicSpline2D(List<Double> x, List<Double> y) {
        int n = x.size();

        double[] params = new double[n];
        for (int i = 0; i < n; i++) {
            params[i] = i;
        }

        double[] xArr = listToArray(x);
        double[] yArr = listToArray(y);

        this.splineX = new CubicSpline(params, xArr);
        this.splineY = new CubicSpline(params, yArr);
    }

    public Point2D point(double t) {
        Double x = splineX.point(t);
        Double y = splineY.point(t);
        return (x == null || y == null) ? null : new Point2D(x, y);
    }

    public List<Point2D> interpolate(int numPoints) {
        List<Point2D> result = new ArrayList<>();

        double start = 0;
        double end = splineX.point(start) == null ? 0 :
                getParamRange();

        for (int i = 0; i < numPoints; i++) {
            double t = start + (end - start) * i / (numPoints - 1);
            Point2D p = point(t);
            if (p != null) {
                result.add(p);
            }
        }
        return result;
    }

    private double getParamRange() {
        return splineX.point(0) == null ? 0 :
                findMaxParam();
    }

    private double findMaxParam() {

        double max = 0;
        while (splineX.point(max + 0.1) != null) {
            max += 0.1;
        }
        return max;
    }

    private double[] listToArray(List<Double> list) {
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}