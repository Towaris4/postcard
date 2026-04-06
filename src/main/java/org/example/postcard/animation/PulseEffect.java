package org.example.postcard.animation;

import org.example.postcard.config.AnimationConfig;
import org.example.postcard.model.HeartShape;
import org.example.postcard.model.HeartbeatCurve;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Преобразует сигнал сердцебиения в динамическую геометрию сердца для каждого кадра.
 */
public class PulseEffect {
    private final HeartShape heartShape;
    private final HeartbeatCurve heartbeatCurve;
    private final long beatDurationMs;
    private final long startTimeMs;

    /**
     * Создает состояние эффекта пульсации.
     *
     * @param heartShape генератор точек сердца
     * @param heartbeatCurve нормализованная кривая сердцебиения
     * @param beatDurationMs длительность полного цикла сердцебиения
     */
    public PulseEffect(HeartShape heartShape, HeartbeatCurve heartbeatCurve, long beatDurationMs) {
        this.heartShape = heartShape;
        this.heartbeatCurve = heartbeatCurve;
        this.beatDurationMs = beatDurationMs;
        this.startTimeMs = System.currentTimeMillis();
    }

    /**
     * Формирует точки сердца для текущего времени анимации.
     *
     * @return сгенерированные точки сердца для отрисовки
     */
    public List<Point2D> nextHeartPoints() {
        long elapsed = System.currentTimeMillis() - startTimeMs;
        long phase = elapsed % beatDurationMs;
        double progress = (double) phase / (double) beatDurationMs;
        double heartbeatY = heartbeatCurve.interpolateYByProgress(progress);
        double scale = AnimationConfig.HEART_BASE_SCALE + AnimationConfig.HEART_PULSE_AMPLITUDE * heartbeatY;
        return heartShape.generate(scale);
    }
}
