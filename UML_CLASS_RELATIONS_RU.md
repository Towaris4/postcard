# Словесное описание отношений классов (для UML)

Этот документ описывает связи между классами проекта `Postcard`, чтобы по нему можно было собрать UML-диаграмму классов в PlantUML.

## 1) Состав классов по пакетам

- `org.example.postcard`
  - `PostcardApp` (точка входа, сборка зависимостей)

- `org.example.postcard.config`
  - `AnimationConfig` (статические настройки приложения)

- `org.example.postcard.model`
  - `CatShape` (модель контура кота, загрузка линий)
  - `HeartShape` (генератор геометрии сердца)
  - `HeartbeatCurve` (кривая сердцебиения, интерполяция значения)

- `org.example.postcard.animation`
  - `LineAnimation` (пошаговая видимость линий)
  - `PulseEffect` (расчет текущей формы сердца по фазе пульса)
  - `AnimationController` (координация Swing-таймеров)

- `org.example.postcard.view`
  - `PostcardPanel` (панель UI, хранит состояние кадра)
  - `Renderer` (отрисовка линий, сердца и текста)

## 2) Основные отношения между классами

### 2.1 Инициализация (кто кого создает)

- `PostcardApp` **создает**:
  - `CatShape` через `CatShape.loadCatPath(...)`
  - `HeartbeatCurve` через `HeartbeatCurve.loadFromFile(...)`
  - `HeartShape`
  - `LineAnimation` (получает `List<Line2D>` из `CatShape`)
  - `PulseEffect` (получает `HeartShape`, `HeartbeatCurve`, `beatDuration`)
  - `AnimationController`
  - `Renderer`
  - `PostcardPanel` (получает `LineAnimation`, `PulseEffect`, `AnimationController`, `Renderer`)
  - `JFrame` (добавляет в него `PostcardPanel`)

Для UML это удобно показать как зависимости `PostcardApp --> <класс>` с пометкой `<<create>>`.

### 2.2 Отношения хранения (has-a / агрегация)

- `PostcardPanel` **хранит ссылки** на:
  - `LineAnimation`
  - `PulseEffect`
  - `AnimationController`
  - `Renderer`
  - `List<Point2D> heartPoints` (текущее состояние сердца)

Это можно показать как ассоциации/агрегации от `PostcardPanel` к указанным классам.

### 2.3 Композиция расчетов анимации

- `PulseEffect` **зависит от**:
  - `HeartShape` (генерация точек сердца)
  - `HeartbeatCurve` (получение амплитуды по фазе)
  - `AnimationConfig` (константы масштаба)

- `LineAnimation` **зависит от**:
  - `List<Line2D>` (полный набор линий, полученный из `CatShape`)

- `AnimationController` **управляет**:
  - двумя `javax.swing.Timer` (`lineTimer` и `pulseTimer`)
  - и координирует вызовы к `LineAnimation` и `PulseEffect`

### 2.4 Отрисовка

- `PostcardPanel.paintComponent(...)` делегирует рендер в `Renderer.render(...)`.
- `Renderer` не хранит бизнес-состояние, а только использует входные данные:
  - `List<Line2D> visibleLines`
  - `List<Point2D> heartPoints`
  - `boolean animationFinished`

## 3) Отношения использования конфигурации

- `AnimationConfig` используется как источник констант в:
  - `PostcardApp` (пути к файлам, параметры окна, длительность пульса)
  - `AnimationController` (интервалы таймеров)
  - `PulseEffect` (параметры пульсации сердца)
  - `HeartShape` (смещения, размер, шаг генерации)

На UML обычно показывается как зависимость (пунктирная стрелка) к `AnimationConfig`.

## 4) Поток взаимодействия во время работы

1. `PostcardApp` создает объектный граф и запускает `PostcardPanel.start()`.
2. `PostcardPanel` вызывает `AnimationController.start(...)`.
3. `AnimationController`:
   - по таймеру линий вызывает `LineAnimation.step()`,
   - по таймеру пульса получает точки через `PulseEffect.nextHeartPoints()`,
   - передает новые точки обратно в `PostcardPanel`.
4. `PostcardPanel` вызывает `repaint()`.
5. В `paintComponent(...)` панель запрашивает:
   - `lineAnimation.getVisibleLines()`,
   - текущие `heartPoints`,
   - и передает это в `Renderer.render(...)`.

## 5) Подсказки для UML-обозначений

- Для полей в классе (например, зависимости `PostcardPanel`) используйте **ассоциацию**.
- Для вызовов статических фабрик в `PostcardApp` используйте **зависимость `<<create>>`**.
- Для `AnimationController` и `Timer` можно показать отношение как обычную ассоциацию `1..2` (или два отдельных поля).
- Связи коллекций можно подписывать кратностью:
  - `CatShape "1" o-- "*" Line2D` (логическая связь через `List<Line2D>`)
  - `HeartbeatCurve "1" o-- "*" Point2D`

## 6) Минимальный список связей, которые стоит обязательно отразить

- `PostcardApp --> CatShape : <<create/load>>`
- `PostcardApp --> HeartbeatCurve : <<create/load>>`
- `PostcardApp --> HeartShape : <<create>>`
- `PostcardApp --> LineAnimation : <<create>>`
- `PostcardApp --> PulseEffect : <<create>>`
- `PostcardApp --> AnimationController : <<create>>`
- `PostcardApp --> Renderer : <<create>>`
- `PostcardApp --> PostcardPanel : <<create>>`
- `PostcardPanel --> LineAnimation`
- `PostcardPanel --> PulseEffect`
- `PostcardPanel --> AnimationController`
- `PostcardPanel --> Renderer`
- `PulseEffect --> HeartShape`
- `PulseEffect --> HeartbeatCurve`
- `AnimationController --> LineAnimation`
- `AnimationController --> PulseEffect`
- `PostcardPanel --> Renderer : render(...)`
- `PostcardApp ..> AnimationConfig`
- `AnimationController ..> AnimationConfig`
- `PulseEffect ..> AnimationConfig`
- `HeartShape ..> AnimationConfig`

