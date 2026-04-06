package org.example.postcard.animation;

import org.example.postcard.config.AnimationConfig;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.function.Consumer;

/**
 * Координирует Swing-таймеры для анимации линий и пульсации сердца.
 */
public class AnimationController {
    private Timer lineTimer;
    private Timer pulseTimer;

    /**
     * Запускает оба таймера анимации и привязывает колбэки обновления состояния представления.
     *
     * @param lineAnimation модель состояния видимости линий
     * @param pulseEffect преобразователь сердечного сигнала в точки сердца
     * @param heartConsumer колбэк для публикации новых точек сердца
     * @param onLineStep колбэк на каждом успешном шаге прорисовки линии
     * @param onLineFinished колбэк при завершении анимации линий
     * @param repaintAction колбэк запроса перерисовки панели
     */
    public void start(
            LineAnimation lineAnimation,
            PulseEffect pulseEffect,
            Consumer<List<Point2D>> heartConsumer,
            Runnable onLineStep,
            Runnable onLineFinished,
            Runnable repaintAction
    ) {
        stop();

        lineTimer = new Timer(AnimationConfig.LINE_TIMER_DELAY_MS, e -> {
            if (lineAnimation.step()) {
                onLineStep.run();
            } else {
                onLineFinished.run();
                ((Timer) e.getSource()).stop();
            }
            repaintAction.run();
        });
        lineTimer.start();

        pulseTimer = new Timer(AnimationConfig.PULSE_TIMER_DELAY_MS, e -> {
            heartConsumer.accept(pulseEffect.nextHeartPoints());
            repaintAction.run();
        });
        pulseTimer.start();
    }

    /**
     * Останавливает таймеры и очищает ссылки на них.
     */
    public void stop() {
        if (lineTimer != null) {
            lineTimer.stop();
            lineTimer = null;
        }
        if (pulseTimer != null) {
            pulseTimer.stop();
            pulseTimer = null;
        }
    }
}
