package org.example.postcard.view;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Рендерер без внутреннего состояния для элементов сцены открытки.
 */
public class Renderer {
    /**
     * Рисует видимые линии кота, текущие точки сердца и финальный текстовый эффект.
     *
     * @param g2d целевой графический контекст
     * @param visibleLines текущие видимые линии кота
     * @param heartPoints текущий набор точек сердца
     * @param animationFinished признак завершения отрисовки линий
     */
    public void render(Graphics2D g2d, List<Line2D> visibleLines, List<Point2D> heartPoints, boolean animationFinished) {
        g2d.setColor(Color.ORANGE);
        for (Line2D line : visibleLines) {
            g2d.draw(line);
        }

        g2d.setColor(Color.RED);
        for (Point2D point : heartPoints) {
            int pointSize = 3;
            g2d.fillRect(
                    (int) point.getX() - pointSize / 3,
                    (int) point.getY() - pointSize / 3,
                    pointSize,
                    pointSize
            );
        }

        if (animationFinished) {
            long now = System.currentTimeMillis();
            long mod = now % 10_000L;
            double progress = mod / 10_000.0;
            float hue = (float) progress;
            float saturation = (float) (0.75 + 0.20 * Math.sin(2 * Math.PI * 5 * progress));
            float brightness = (float) (0.85 + 0.10 * Math.cos(2 * Math.PI * 8 * progress));
            saturation = Math.max(0f, Math.min(1f, saturation));
            brightness = Math.max(0f, Math.min(1f, brightness));
            g2d.setColor(Color.getHSBColor(hue, saturation, brightness));
            g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
            g2d.drawString("Happy birthday!", 50, 50);
        }
    }
}
