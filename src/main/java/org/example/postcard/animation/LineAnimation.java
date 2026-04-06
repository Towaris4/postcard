package org.example.postcard.animation;

import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.List;

/**
 * Управляет поэтапным появлением заранее загруженных линейных сегментов.
 */
public class LineAnimation {
    private final List<Line2D> allLines;
    private int visibleCount;

    /**
     * Создает состояние пошаговой анимации линий.
     *
     * @param allLines полный список отрисовываемых сегментов
     */
    public LineAnimation(List<Line2D> allLines) {
        this.allLines = allLines;
        this.visibleCount = 0;
    }

    /**
     * Продвигает анимацию на одну линию.
     *
     * @return {@code true}, если новая линия стала видимой; иначе {@code false}
     */
    public boolean step() {
        if (visibleCount < allLines.size()) {
            visibleCount++;
            return true;
        }
        return false;
    }

    /**
     * Возвращает текущую видимую часть всех линейных сегментов.
     *
     * @return подсписок линий от начала до текущего видимого индекса
     */
    public List<Line2D> getVisibleLines() {
        if (visibleCount <= 0) {
            return Collections.emptyList();
        }
        return allLines.subList(0, visibleCount);
    }
}
