package main.java;

import java.util.Arrays;

public class CubicSpline {
    private final double[] x, y, a, b, c, d;
    private final int n;

    public CubicSpline(double[] x, double[] y) {
        this.n = x.length;
        this.x = Arrays.copyOf(x, n);
        this.y = Arrays.copyOf(y, n);
        this.a = new double[n];
        this.b = new double[n];
        this.c = new double[n];
        this.d = new double[n];

        calculateSpline();
    }

    private void calculateSpline() {
        if (n == 2) {
            a[0] = y[0];
            b[0] = (y[1] - y[0]) / (x[1] - x[0]);
            c[0] = 0;
            d[0] = 0;
            a[1] = y[1];
            return;
        }

        double[] h = new double[n - 1];
        for (int i = 0; i < n - 1; i++) {
            h[i] = x[i + 1] - x[i];
        }

        double[] alpha = new double[n];
        double[] beta = new double[n];
        double[] gamma = new double[n];
        double[] r = new double[n];

        for (int i = 1; i < n - 1; i++) {
            alpha[i] = h[i - 1];
            beta[i] = 2.0 * (h[i - 1] + h[i]);
            gamma[i] = h[i];
            r[i] = 3.0 * ((y[i + 1] - y[i]) / h[i] - (y[i] - y[i - 1]) / h[i - 1]);
        }

        beta[0] = 1.0;
        gamma[0] = 0.0;
        r[0] = 0.0;

        beta[n - 1] = 1.0;
        alpha[n - 1] = 0.0;
        r[n - 1] = 0.0;

        double[] w = new double[n];
        w[0] = gamma[0] / beta[0];

        double[] g = new double[n];
        g[0] = r[0] / beta[0];

        for (int i = 1; i < n; i++) {
            double m = beta[i] - alpha[i] * w[i - 1];
            w[i] = gamma[i] / m;
            g[i] = (r[i] - alpha[i] * g[i - 1]) / m;
        }

        c[n - 1] = g[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            c[i] = g[i] - w[i] * c[i + 1];
        }

        for (int i = 0; i < n - 1; i++) {
            a[i] = y[i];
            b[i] = (y[i + 1] - y[i]) / h[i] - h[i] * (c[i + 1] + 2.0 * c[i]) / 3.0;
            d[i] = (c[i + 1] - c[i]) / (3.0 * h[i]);
        }
        a[n - 1] = y[n - 1];
    }

    public Double point(double t) {
        int i = 0;
        while (i < n - 1 && t > x[i + 1]) {
            i++;
        }

        if (i >= n - 1) return null;

        double dx = t - x[i];
        return a[i] + b[i] * dx + c[i] * dx * dx + d[i] * dx * dx * dx;
    }
}