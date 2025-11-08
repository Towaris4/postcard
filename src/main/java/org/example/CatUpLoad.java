package org.example;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;

public class CatUpLoad {

    public static List<Line2D> line2DList = new ArrayList<>();
    public static ListIterator<Line2D> iterator;
    public static Timer timer;
    public static Timer heartbeatTimer;

    public void getLines() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/cat.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Pattern pattern1 = Pattern.compile("(-?\\d+\\.\\d+)\\s*,\\s*(-?\\d+\\.\\d+)\\s+(-?\\d+\\.\\d+)\\s*,\\s*(-?\\d+\\.\\d+)");
                Matcher matcher1 = pattern1.matcher(line);
                if (matcher1.find()) {
                    float x1 = Float.parseFloat(matcher1.group(1)) / 60;
                    float y1 = Float.parseFloat(matcher1.group(2)) / 60;
                    float x2 = Float.parseFloat(matcher1.group(3)) / 60;
                    float y2 = Float.parseFloat(matcher1.group(4)) / 60;
                    line2DList.add(new Line2D.Float(x1 + 550, -y1 + 500, x2 + 550, -y2 + 500));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.shuffle(line2DList);
    }

    public static void main(String[] args) {
        CatUpLoad catLoad = new CatUpLoad();
        catLoad.getLines();
        HeartBeat.getPoints();
        Heart.getHeart(1.0);


        // Отрисовка
        JFrame frame = new JFrame("Happy birthday!");
        frame.setLocation(100, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 540);
        frame.add(new DrawingPanel(line2DList));
        frame.setVisible(true);
    }

    static class DrawingPanel extends JPanel {
        boolean animationFinished = false;

        public DrawingPanel(List<Line2D> lines) {

            CatUpLoad.iterator = lines.listIterator();

            // Таймер с задержкой 200 мс (для плавной анимации)
            timer = new Timer(5, e -> {
                if (iterator.hasNext()) {
                    iterator.next(); // Переходим к следующей линии
                    repaint();      // Перерисовываем панель
                } else {
                    animationFinished = true;
                    ((Timer) e.getSource()).stop(); // Останавливаем таймер
                    repaint();
                }
            });
            timer.start(); // Запускаем анимацию


            // Настраиваем пульсацию сердца на 80 ударов в минуту (один удар = 750 мс)
            final long beatDurationMs = 750L;
            final java.util.List<Point2D> hb = HeartBeat.heartBeats;
            final double minX = hb.isEmpty() ? 0 : hb.get(0).getX();
            final double maxX = hb.isEmpty() ? 1 : hb.get(hb.size() - 1).getX();
            final double rangeX = Math.max(1e-6, maxX - minX);
            final long startTime = System.currentTimeMillis();

            heartbeatTimer = new Timer(10, e -> {
                long elapsed = System.currentTimeMillis() - startTime;
                long phase = elapsed % beatDurationMs;
                double p = (double) phase / (double) beatDurationMs; // 0..1
                double targetX = minX + p * rangeX;

                // Линейная интерполяция Y по targetX
                double y;
                if (hb.isEmpty()) {
                    y = 0;
                } else if (hb.size() == 1) {
                    y = hb.get(0).getY();
                } else {
                    int lo = 0, hi = hb.size() - 1;
                    while (lo + 1 < hi) {
                        int mid = (lo + hi) >>> 1;
                        double xm = hb.get(mid).getX();
                        if (xm <= targetX) {
                            lo = mid;
                        } else {
                            hi = mid;
                        }
                    }
                    double x0 = hb.get(lo).getX();
                    double y0 = hb.get(lo).getY();
                    double x1 = hb.get(hi).getX();
                    double y1 = hb.get(hi).getY();
                    double t = (x1 - x0) == 0 ? 0 : (targetX - x0) / (x1 - x0);
                    y = y0 + t * (y1 - y0); // y уже нормирован в [-1,1]
                }

                // Масштабируем на ±5% от базового размера относительно центра
                double scaleFactor = 1.0 + 0.05 * y; // 0.95..1.05
                Heart.getHeart(scaleFactor);
                repaint();
            });
            heartbeatTimer.start();
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            ListIterator<Line2D> drawIterator = line2DList.listIterator();
            while (drawIterator.nextIndex() < iterator.nextIndex()) {
                g2d.draw(drawIterator.next());
            }

            /*g2d.setColor(Color.BLACK);
            for (Line2D line : line2DList) {
                g2d.draw(line);
            }*/
            // Отрисовываем сердце параллельно с прорисовкой кота
            g2d.setColor(Color.RED);
            for (Point2D point : Heart.heart) {
                int pointSize = 3;
                g2d.fillRect(
                        (int) point.getX() - pointSize / 3,
                        (int) point.getY() - pointSize / 3,
                        pointSize,
                        pointSize
                );
            }

            // Сообщение выводим после завершения анимации кота
            if (animationFinished) {
                // Цвет по «спирали» (приближение золотого отношения), замкнутый цикл за 60 секунд
                long now = System.currentTimeMillis();
                long mod = now % 10000L; // 10s
                double p = mod / 10000.0; // 0..1
                // Оборот оттенка за минуту
                float hue = (float) p;
                // Приближение золотого отношения 1.6 -> частоты 5 и 8 (возврат в исходную точку за 60с)
                float sat = (float) (0.75 + 0.20 * Math.sin(2 * Math.PI * 5 * p));
                float bri = (float) (0.85 + 0.10 * Math.cos(2 * Math.PI * 8 * p));
                sat = Math.max(0f, Math.min(1f, sat));
                bri = Math.max(0f, Math.min(1f, bri));

                g2d.setColor(Color.getHSBColor(hue, sat, bri));
                g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 36));

                String message = "С Днем Рождения, Мама!";
                int x = 50;
                int y = 50;
                g2d.drawString(message, x, y);
            }
        }
    }
}
