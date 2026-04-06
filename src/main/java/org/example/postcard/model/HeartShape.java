package org.example.postcard.model;

import org.example.postcard.config.AnimationConfig;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Генерирует точки сердца для заданного коэффициента пульсации.
 */
public class HeartShape {
    /**
     * Строит текущий контур сердца в виде набора точек.
     *
     * @param scaleFactor динамический коэффициент масштаба от эффекта пульсации
     * @return список точек, готовый к отрисовке
     */
    public List<Point2D> generate(double scaleFactor) {
        List<Point2D> heartPoints = new ArrayList<>();
        double size = AnimationConfig.HEART_BASE_SIZE * scaleFactor;

        for (double x = -2 * size; x < 2.01 * size; x += AnimationConfig.HEART_X_STEP) {
            double normalizedX = x / size;
            double term1 = 1 - Math.pow(Math.abs(normalizedX) - 1, 2);
            double y1 = term1 >= 0 ? Math.sqrt(term1) : 0;

            double sqrtTerm = Math.sqrt(Math.abs(normalizedX * 2));
            double term2 = 1 - sqrtTerm / 2;
            double y2 = term2 >= 0 ? -2.5 * Math.sqrt(term2) : 0;

            if (!Double.isNaN(y1) && !Double.isNaN(y2)) {
                heartPoints.add(new Point2D.Double(
                        x + AnimationConfig.HEART_OFFSET_X,
                        -y1 * size + AnimationConfig.HEART_OFFSET_Y
                ));
                heartPoints.add(new Point2D.Double(
                        x + AnimationConfig.HEART_OFFSET_X,
                        -y2 * size + AnimationConfig.HEART_OFFSET_Y
                ));
            }
        }
        return heartPoints;
    }
}
