package org.example.postcard.model;

import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Неизменяемый контейнер для сегментов контура кота и загрузчик из текстового файла.
 */
public class CatShape {
    private final List<Line2D> lines;

    /**
     * Создает объект формы на основе подготовленных линейных сегментов.
     *
     * @param lines сегменты линий, образующие отрисовываемую форму
     */
    public CatShape(List<Line2D> lines) {
        this.lines = lines;
    }

    /**
     * Загружает линии кота из файла, применяет трансформацию и перемешивает порядок отрисовки.
     *
     * @param filePath путь к текстовому файлу с парами точек
     * @param scaleDivisor значение для масштабирования исходных координат
     * @param offsetX смещение по оси X после масштабирования
     * @param offsetY смещение по оси Y после масштабирования
     * @return разобранная и преобразованная форма кота
     */
    public static CatShape loadCatPath(String filePath, double scaleDivisor, double offsetX, double offsetY) {
        List<Line2D> loaded = new ArrayList<>();
        Pattern pattern = Pattern.compile("(-?\\d+\\.\\d+)\\s*,\\s*(-?\\d+\\.\\d+)\\s+(-?\\d+\\.\\d+)\\s*,\\s*(-?\\d+\\.\\d+)");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    double x1 = Double.parseDouble(matcher.group(1)) / scaleDivisor;
                    double y1 = Double.parseDouble(matcher.group(2)) / scaleDivisor;
                    double x2 = Double.parseDouble(matcher.group(3)) / scaleDivisor;
                    double y2 = Double.parseDouble(matcher.group(4)) / scaleDivisor;
                    loaded.add(new Line2D.Double(x1 + offsetX, -y1 + offsetY, x2 + offsetX, -y2 + offsetY));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load cat shape from " + filePath, e);
        }

        Collections.shuffle(loaded);
        return new CatShape(loaded);
    }

    /**
     * Возвращает все линейные сегменты, используемые анимацией линий.
     *
     * @return изменяемый список линий формы
     */
    public List<Line2D> getLines() {
        return lines;
    }
}
