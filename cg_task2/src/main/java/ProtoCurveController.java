package main.java;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProtoCurveController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private ArrayList<Point2D> points = new ArrayList<>();
    private List<Point2D> splinePoints = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controller initialized!");

        // Связываем размеры canvas с anchorPane
        canvas.widthProperty().bind(anchorPane.widthProperty());
        canvas.heightProperty().bind(anchorPane.heightProperty());

        // Обработчик кликов
        canvas.setOnMouseClicked(this::handleMouseClick);

        // Первая отрисовка
        redraw();
    }

    private void handleMouseClick(MouseEvent event) {
        System.out.println("Click at: " + event.getX() + ", " + event.getY());

        Point2D clickPoint = new Point2D(event.getX(), event.getY());
        points.add(clickPoint);

        if (points.size() >= 2) {
            calculateSpline();
        }

        redraw();
    }

    private void calculateSpline() {
        if (points.size() < 2) return;

        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();

        for (Point2D point : points) {
            x.add(point.getX());
            y.add(point.getY());
        }

        CubicSpline2D spline2D = new CubicSpline2D(x, y);
        splinePoints = spline2D.interpolate(100);
    }

    private void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // ТОЛЬКО сетка, без белого фона
        drawGridOnly(gc);

        // Рисуем сплайн
        if (splinePoints.size() > 1) {
            gc.setStroke(Color.GREEN);
            gc.setLineWidth(2);

            Point2D prev = splinePoints.get(0);
            for (int i = 1; i < splinePoints.size(); i++) {
                Point2D current = splinePoints.get(i);
                gc.strokeLine(prev.getX(), prev.getY(), current.getX(), current.getY());
                prev = current;
            }
        }

        // Рисуем точки
        if (!points.isEmpty()) {
            // Линии между точками (синий пунктир)
            if (points.size() > 1) {
                gc.setStroke(Color.BLUE);
                gc.setLineWidth(1);
                gc.setLineDashes(5, 3);

                Point2D prev = points.get(0);
                for (int i = 1; i < points.size(); i++) {
                    Point2D current = points.get(i);
                    gc.strokeLine(prev.getX(), prev.getY(), current.getX(), current.getY());
                    prev = current;
                }
                gc.setLineDashes(null);
            }

            // Точки (красные крестики)
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);

            for (Point2D point : points) {
                drawCross(gc, point.getX(), point.getY(), 6);
            }
        }
    }

    private void drawGridOnly(GraphicsContext gc) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        // ТОЛЬКО сетка, фон остается прозрачным
        gc.setStroke(Color.rgb(240, 240, 240));
        gc.setLineWidth(0.5);

        // Мелкая сетка
        for (double x = 0; x < width; x += 20) {
            gc.strokeLine(x, 0, x, height);
        }
        for (double y = 0; y < height; y += 20) {
            gc.strokeLine(0, y, width, y);
        }

        // Основная сетка
        gc.setStroke(Color.rgb(200, 200, 200));
        gc.setLineWidth(1);

        for (double x = 0; x < width; x += 100) {
            gc.strokeLine(x, 0, x, height);
        }
        for (double y = 0; y < height; y += 100) {
            gc.strokeLine(0, y, width, y);
        }
    }

    private void drawCross(GraphicsContext gc, double x, double y, double size) {
        gc.strokeLine(x - size, y - size, x + size, y + size);
        gc.strokeLine(x - size, y + size, x + size, y - size);
    }
}