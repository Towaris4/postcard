package org.example.postcard.model;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Хранит нормализованные сэмплы сердцебиения и интерполирует значение по фазе цикла.
 */
public class HeartbeatCurve {
    private final List<Point2D> points;

    /**
     * Создает кривую сердцебиения из заранее загруженных точек.
     *
     * @param points отсортированные точки-сэмплы
     */
    public HeartbeatCurve(List<Point2D> points) {
        this.points = points;
    }

    /**
     * Загружает точки сердцебиения из файла и нормализует амплитуду до диапазона [-1, 1].
     *
     * @param filePath путь к файлу данных с координатами x,y
     * @return готовая к использованию кривая сердцебиения
     */
    public static HeartbeatCurve loadFromFile(String filePath) {
        List<Point2D> loaded = new ArrayList<>();
        Pattern pattern = Pattern.compile("([-+]?\\d+\\.\\d+)\\s*,\\s*([-+]?\\d+\\.\\d+)");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    double x = Double.parseDouble(matcher.group(1));
                    double y = Double.parseDouble(matcher.group(2));
                    loaded.add(new Point2D.Double(x, y));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load heartbeat curve from " + filePath, e);
        }

        loaded.sort(Comparator.comparingDouble(Point2D::getX));
        normalizeY(loaded);
        return new HeartbeatCurve(loaded);
    }

    /**
     * Возвращает интерполированное значение Y по нормализованному прогрессу цикла.
     *
     * @param progress фаза анимации в диапазоне [0..1]
     * @return интерполированная амплитуда сердцебиения
     */
    public double interpolateYByProgress(double progress) {
        if (points.isEmpty()) {
            return 0;
        }
        if (points.size() == 1) {
            return points.get(0).getY();
        }

        double minX = points.get(0).getX();
        double maxX = points.get(points.size() - 1).getX();
        double xRange = Math.max(1e-6, maxX - minX);
        double targetX = minX + progress * xRange;

        int low = 0;
        int high = points.size() - 1;
        while (low + 1 < high) {
            int mid = (low + high) >>> 1;
            if (points.get(mid).getX() <= targetX) {
                low = mid;
            } else {
                high = mid;
            }
        }

        Point2D left = points.get(low);
        Point2D right = points.get(high);
        double dx = right.getX() - left.getX();
        double t = dx == 0 ? 0 : (targetX - left.getX()) / dx;
        return left.getY() + t * (right.getY() - left.getY());
    }

    /**
     * Нормализует координаты Y по максимальной абсолютной амплитуде.
     *
     * @param points точки для нормализации на месте
     */
    private static void normalizeY(List<Point2D> points) {
        double maxAbs = 0;
        for (Point2D point : points) {
            maxAbs = Math.max(maxAbs, Math.abs(point.getY()));
        }
        if (maxAbs < 1e-9) {
            maxAbs = 1.0;
        }
        for (Point2D point : points) {
            point.setLocation(point.getX(), point.getY() / maxAbs);
        }
    }
}
