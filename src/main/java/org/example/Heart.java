package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Heart {

    public static ArrayList<Point2D> heart = new ArrayList<>();

    public static void getHeart(double scaleFactor) {
        heart.clear();
        double deltaX = 200;
        double deltaY = 200;
        double baseSize = 80.0;
        double size = baseSize * scaleFactor; // масштабирование относительно центра
        for (double x = -2 * size; x < 2.01 * size; x += 0.05) {
            double nx = x / size;  // нормализованный x

            // Верхняя часть сердца
            double term1 = 1 - Math.pow(Math.abs(nx) - 1, 2);
            double y1 = (term1 >= 0) ? Math.sqrt(term1) : 0;

            // Нижняя часть сердца
            double sqrtTerm = Math.sqrt(Math.abs(nx * 2));
            double term2 = 1 - sqrtTerm / 2;
            double y2 = (term2 >= 0) ? -2.5 * Math.sqrt(term2) : 0;

            if (!Double.isNaN(y1) && !Double.isNaN(y2)) {
                heart.add(new Point2D.Double(x + deltaX, -y1 * size + deltaY));
                heart.add(new Point2D.Double(x + deltaX, -y2 * size + deltaY));
            }
        }
    }



    static class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);
            for (Point2D point : heart) {
                int pointSize = 3;
                g2d.fillRect(
                        (int) point.getX() - pointSize / 3,
                        (int) point.getY() - pointSize / 3,
                        pointSize,
                        pointSize
                );
            }
        }
    }
}
