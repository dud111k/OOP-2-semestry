package main.java;

import java.util.Arrays;

public class CubicSpline {
    private double[] a, b, c, d;
    private double[] x, y;
    private int n;

    public CubicSpline(double[] x, double[] y) {

        this.x = x.clone();
        this.y = y.clone();
        this.n = x.length;

        this.a = new double[n];
        this.b = new double[n];
        this.c = new double[n];
        this.d = new double[n];

        for (int i = 0; i < n - 1; i++) {
            if (x[i] >= x[i + 1]) {
                throw new IllegalArgumentException("x must be strictly increasing"); // Х ДОЛЖНЫ ВОЗРАСТАТЬ
            }
        }


        calculateSpline();
    }

    private void calculateSpline() {
        double[] h = new double[n - 1]; // h_i = x_(i + 1) - x_i интервалов на 1 меньше чем точек всегда !!!
        for (int i = 0; i < n - 1; i++) {
            h[i] = x[i + 1] - x[i];
        }
        double[] alpha = new double[n - 1]; // a_i = 3/h_i * (y_{i+1} - y_i) - 3/h_{i-1} * (y_i - y_{i-1})
        for (int i = 1; i < n - 1; i++) {
            alpha[i] = 3.0 / h[i] * (y[i + 1] - y[i]) - 3.0 / h[i - 1] * (y[i] - y[i - 1]);
        } // начало прогонки
        double[] l = new double[n];
        double[] mu = new double[n];
        double[] z = new double[n];

        l[0] = 1.0;
        mu[0] = 0.0;
        z[0] = 0.0;

        for (int i = 1; i < n - 1; i++) {
            l[i] = 2.0 * (x[i + 1] - x[i - 1]) - h[i - 1] * mu[i - 1];
            mu[i] = h[i] / l[i];
            z[i] = (alpha[i] - h[i - 1] * z[i - 1]) / l[i];
        }

        l[n - 1] = 1.0;
        z[n - 1] = 0.0;
        c[n - 1] = 0.0;

        // он идет обратно
        for (int j = n - 2; j >= 0; j--) {
            c[j] = z[j] - mu[j] * c[j + 1];
            b[j] = (y[j + 1] - y[j]) / h[j] - h[j] * (c[j + 1] + 2.0 * c[j]) / 3.0;
            d[j] = (c[j + 1] - c[j]) / (3.0 * h[j]);
        }

        // коэф. а = знач У в узлах
        for (int i = 0; i < n; i++) {
            a[i] = y[i];
        }
    }

    public Double point(double param) {
        if (param < x[0] || param > x[n - 1]) { //чек пределы сплайна
            return null;
        }

        int i = findInterval(param);
        if (i < 0 || i >= n - 1) {
            return null;
        }

        double dx = param - x[i]; //смещение от начала
        return a[i] + b[i] * dx + c[i] * dx * dx + d[i] * dx * dx * dx; // S_i(x) = a_i + b_i*dx + c_i*dx² + d_i*dx³
    }

    private int findInterval(double x) { //бин поиск
        int low = 0;
        int high = n - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (x < this.x[mid]) {
                high = mid - 1;
            } else if (mid < n - 1 && x >= this.x[mid + 1]) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}