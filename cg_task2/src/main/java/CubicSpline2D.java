package main.java;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

public class CubicSpline2D {
    private CubicSpline sx;
    private CubicSpline sy;
    private double[] params;

    public CubicSpline2D(List<Double> x, List<Double> y) {
        this.params = calculateParams(x, y);

        double[] xArr = listToArray(x);
        double[] yArr = listToArray(y);
        double[] pArr = params;

        this.sx = new CubicSpline(pArr, xArr);
        this.sy = new CubicSpline(pArr, yArr);
    }

    public Point2D point(double param) {
        Double x = sx.point(param);
        Double y = sy.point(param);
        if (x == null || y == null) return null;
        return new Point2D(x, y);
    }

    private double[] calculateParams(List<Double> x, List<Double> y) {
        int n = x.size();
        double[] s = new double[n];
        s[0] = 0.0;

        for (int i = 1; i < n; i++) {
            double dx = x.get(i) - x.get(i - 1);
            double dy = y.get(i) - y.get(i - 1);
            s[i] = s[i - 1] + Math.sqrt(dx * dx + dy * dy);
        }

        return s;
    }

    public List<Point2D> interpolate(int numPoints) {
        List<Point2D> result = new ArrayList<>();
        if (params.length < 2) return result;

        double start = params[0];
        double end = params[params.length - 1];

        for (int i = 0; i < numPoints; i++) {
            double t = (double) i / (numPoints - 1);
            double param = start + t * (end - start);
            Point2D point = point(param);
            if (point != null) {
                result.add(point);
            }
        }
        return result;
    }

    private double[] listToArray(List<Double> list) {
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}