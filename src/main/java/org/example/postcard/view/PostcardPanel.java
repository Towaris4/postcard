package org.example.postcard.view;

import org.example.postcard.animation.AnimationController;
import org.example.postcard.animation.LineAnimation;
import org.example.postcard.animation.PulseEffect;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing-панель, которая хранит состояние анимации и делегирует отрисовку в {@link Renderer}.
 */
public class PostcardPanel extends JPanel {
    private final LineAnimation lineAnimation;
    private final PulseEffect pulseEffect;
    private final AnimationController controller;
    private final Renderer renderer;

    private List<Point2D> heartPoints = new ArrayList<>();
    private boolean animationFinished;
    private boolean started;

    /**
     * Создает панель с внедренными зависимостями анимации.
     *
     * @param lineAnimation модель поэтапной анимации линий
     * @param pulseEffect модель пульсации сердца
     * @param controller контроллер жизненного цикла таймеров
     * @param renderer стратегия отрисовки
     */
    public PostcardPanel(
            LineAnimation lineAnimation,
            PulseEffect pulseEffect,
            AnimationController controller,
            Renderer renderer
    ) {
        this.lineAnimation = lineAnimation;
        this.pulseEffect = pulseEffect;
        this.controller = controller;
        this.renderer = renderer;
    }

    /**
     * Запускает анимацию один раз для текущего экземпляра панели.
     */
    public void start() {
        if (started) {
            return;
        }
        started = true;
        controller.start(
                lineAnimation,
                pulseEffect,
                points -> heartPoints = points,
                () -> {
                },
                () -> animationFinished = true,
                this::repaint
        );
    }

    /**
     * Останавливает все запущенные таймеры, связанные с этой панелью.
     */
    public void stop() {
        controller.stop();
        started = false;
    }

    /**
     * Колбэк отрисовки, вызываемый в потоке событий Swing.
     *
     * @param g графический контекст
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.render((Graphics2D) g, lineAnimation.getVisibleLines(), heartPoints, animationFinished);
    }
}
