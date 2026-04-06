package org.example.postcard.config;

/**
 * Центральные настройки окна, источников данных и таймингов анимации.
 */
public final class AnimationConfig {
    public static final String WINDOW_TITLE = "Happy birthday!";
    public static final int WINDOW_X = 100;
    public static final int WINDOW_Y = 200;
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 540;

    public static final String CAT_DATA_PATH = "data/cat.txt";
    public static final String HEARTBEAT_DATA_PATH = "data/heartBeats.txt";

    public static final int LINE_TIMER_DELAY_MS = 5;
    public static final int PULSE_TIMER_DELAY_MS = 10;
    public static final long BEAT_DURATION_MS = 750L;
    public static final double HEART_BASE_SCALE = 1.0;
    public static final double HEART_PULSE_AMPLITUDE = 0.05;

    public static final double CAT_SCALE_DIVISOR = 60.0;
    public static final double CAT_OFFSET_X = 550.0;
    public static final double CAT_OFFSET_Y = 500.0;

    public static final double HEART_OFFSET_X = 200.0;
    public static final double HEART_OFFSET_Y = 200.0;
    public static final double HEART_BASE_SIZE = 80.0;
    public static final double HEART_X_STEP = 0.05;

    private AnimationConfig() {
    }
}
