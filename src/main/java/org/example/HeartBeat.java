package org.example;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeartBeat {
    public static List<Point2D> heartBeats = new ArrayList<>();

    public static void getPoints() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/heartBeats.txt"))) {
            String point;
            while ((point = br.readLine()) != null) {
                Pattern pattern1 = Pattern.compile("([-+]?\\d+\\.\\d+)\\s*,\\s*([-+]?\\d+\\.\\d+)");
                Matcher matcher1 = pattern1.matcher(point);
                if (matcher1.find()) {
                    float x1 = Float.parseFloat(matcher1.group(1));
                    float y1 = Float.parseFloat(matcher1.group(2));
                    heartBeats.add(new Point2D.Float(x1, y1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        heartBeats.sort(Comparator.comparingDouble(Point2D::getX));
        // Нормализуем амплитуду Y в диапазон [-1, 1]
        double maxAbs = 0.0;
        for (Point2D p : heartBeats) {
            maxAbs = Math.max(maxAbs, Math.abs(p.getY()));
        }
        if (maxAbs < 1e-9) maxAbs = 1.0;
        for (Point2D point : heartBeats) {
            double newY = point.getY() / maxAbs;
            point.setLocation(point.getX(), newY);
        }
    }
    // Тестовый main удалён как неиспользуемый
}
