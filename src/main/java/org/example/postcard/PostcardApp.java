package org.example.postcard;

import org.example.postcard.animation.AnimationController;
import org.example.postcard.animation.LineAnimation;
import org.example.postcard.animation.PulseEffect;
import org.example.postcard.config.AnimationConfig;
import org.example.postcard.model.CatShape;
import org.example.postcard.model.HeartShape;
import org.example.postcard.model.HeartbeatCurve;
import org.example.postcard.view.PostcardPanel;
import org.example.postcard.view.Renderer;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Точка входа в приложение. Связывает слои модели, анимации и представления.
 */
public class PostcardApp {
    /**
     * Создает и запускает окно открытки.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        CatShape catShape = CatShape.loadCatPath(
                AnimationConfig.CAT_DATA_PATH,
                AnimationConfig.CAT_SCALE_DIVISOR,
                AnimationConfig.CAT_OFFSET_X,
                AnimationConfig.CAT_OFFSET_Y
        );
        HeartbeatCurve heartbeatCurve = HeartbeatCurve.loadFromFile(AnimationConfig.HEARTBEAT_DATA_PATH);
        HeartShape heartShape = new HeartShape();

        LineAnimation lineAnimation = new LineAnimation(catShape.getLines());
        PulseEffect pulseEffect = new PulseEffect(heartShape, heartbeatCurve, AnimationConfig.BEAT_DURATION_MS);
        AnimationController controller = new AnimationController();
        Renderer renderer = new Renderer();

        JFrame frame = new JFrame(AnimationConfig.WINDOW_TITLE);
        frame.setLocation(AnimationConfig.WINDOW_X, AnimationConfig.WINDOW_Y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(AnimationConfig.WINDOW_WIDTH, AnimationConfig.WINDOW_HEIGHT);

        PostcardPanel panel = new PostcardPanel(lineAnimation, pulseEffect, controller, renderer);
        frame.add(panel);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                panel.stop();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                panel.stop();
            }
        });
        frame.setVisible(true);
        panel.start();
    }
}
