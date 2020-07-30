package ru.andtalk;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import static java.lang.Math.*;

/**
 * Класс Controller отвечает за функционал стартового окна приложения(окно выбора метода).
 * В окне есть две кнопки, при нажатии на которые срабатывают соответствующие методы.
 *     1. При выборе метода Эйлера срабатывает метод eulerStart, который выводит соответсвующий график.
 *     2. При выборе метода Рунге-Кутты срабатывает метод rkStart, который создает 2 окна. Первый - график x(t), второй - график глобальной погрешности по методу Рунге.
 * Приложение заканчивает работу тогда, когда все окна закрыты.
 */
public class Controller {

    /** Начало исследования функции */
    private final double T0 = 0;
    /** Конец исследования функции */
    private final double T = 3;
    /** Количество шагов исследования функции */
    private final int N = 600;
    /** Длина шага */
    private final double H = (T - T0) / N;

    /** Массив точек для графика функции, полученного методом Эйлера */
    private double[] eulerArr = new double[N + 1];
    /** Массив точек для графика функции, полученного классическим методом Рунге-Кутты */
    private double[] rkArr = new double[N + 1];
    /** Массив точек для графика функции, полученной путем подставления в нее значений */
    private double[] simpleArr = new double[N + 1];
    /** Массив точек для графика функции погрешности метода Рунге-Кутты */
    private double[] epsArr = new double[N + 1];

    /**
     * Исходная функция - (e^sin(x)) * atan(2*x^2)
     * @param x Значение аргумента
     * @return Значение функции
     */
    private double f(double x) {
        return pow(E, sin(x)) * atan(2 * x * x);
    }

    /**
     * Дифференцированная исходная функция
     * @see Controller#f(double)
     * @param x Значение аргумента
     * @return Значение функции
     */
    private double difF(double x) {
        return (f(x + H) - f(x)) / H;
    }

    /**
     * Заполняет массив {@link Controller#simpleArr} числами, которые получаются путем подставления значений в исходную функцию - {@link Controller#f(double)}
     */
    private void simple() {
        for (int i = 0; i <= N; i++) {
            simpleArr[i] = f(i * H);
        }
    }

    /**
     * Метод Эйлера
     * Заполняет массив {@link Controller#eulerArr} в соответствии с указанными в полях класса отрезком ({@link Controller#T0}, {@link Controller#T}) и колличеством шагов {@link Controller#N}.
     * Используется методом {@link Controller#eulerStart()}
     */
    private void euler() {
        double xm = 0;
        double xm1;
        eulerArr[0] = 0;
        for (int i = 1; i <= N; i++) {
            xm1 = xm + H * difF((i - 1) * H);
            eulerArr[i] = xm1;
            xm = xm1;
        }
    }

    /**
     * Первая из четырех вспомогательных функций дифференцирования для высчитывания нового шага методом Рунге-Кутты
     * Используется методом {@link Controller#rk4()}
     * @see Controller#k2(double)
     * @see Controller#k3(double)
     * @see Controller#k4(double)
     * @see Controller#rk4
     * @see Controller#rkStart()
     * @param x Значение аргумента
     * @return Значение функции
     */
    private double k1(double x) {
        return difF(x);
    }

    /**
     * Вторая из четырех вспомогательных функций дифференцирования для высчитывания нового шага методом Рунге-Кутты
     * Используется методом {@link Controller#rk4()}
     * @see Controller#k1(double)
     * @see Controller#k3(double)
     * @see Controller#k4(double)
     * @see Controller#rk4()
     * @see Controller#rkStart()
     * @param x Значение аргумента
     * @return Значение функции
     */
    private double k2(double x) {
        return difF(x + H / 2);
    }

    /**
     * Третья из четырех вспомогательных функций дифференцирования для высчитывания нового шага методом Рунге-Кутты
     * Используется методом {@link Controller#rk4()}
     * @see Controller#k1(double)
     * @see Controller#k2(double)
     * @see Controller#k4(double)
     * @see Controller#rk4()
     * @see Controller#rkStart()
     * @param x Значение аргумента
     * @return Значение функции
     */
    private double k3(double x) {
        return difF(x + H / 2);
    }

    /**
     * Последняя из четырех вспомогательных функций дифференцирования для высчитывания нового шага методом Рунге-Кутты
     * Используется методом {@link Controller#rk4()}
     * @see Controller#k2(double)
     * @see Controller#k3(double)
     * @see Controller#k4(double)
     * @see Controller#rk4()
     * @see Controller#rkStart()
     * @param x Значение аргумента
     * @return Значение функции
     */
    private double k4(double x) {
        return difF(x + H);
    }

    /**
     * 4-х этапный классический метод Рунге-Кутты с вычислением погрешности на каждом шаге.
     * Заполняет массив {@link Controller#rkArr} в соответствии с указанными в полях класса отрезком ({@link Controller#T0}, {@link Controller#T}) и колличеством шагов {@link Controller#N}.
     * Также заполняет массив точек для графика погрешности {@link Controller#epsArr}.
     * Используется методом {@link Controller#rkStart()}.
     */
    private void rk4() {
        double xm = 0;
        double xm1;
        rkArr[0] = 0;
        epsArr[0] = 0;
        for (int i = 1; i <= N; i++) {
            xm1 = xm + H / 6 * (k1(i * H) + 2 * k2(i * H) + 2 * k3(i * H) + k4(i * H));
            rkArr[i] = xm1;
            epsArr[i] = abs(4 * ((xm + H / 12 * (k1(i * H / 2) + 2 * k2(i * H / 2) + 2 * k3(i * H / 2) + k4(i * H / 2))) - xm1) / 3);
            xm = xm1;
        }
    }

    /**
     * Метод для вывода функции погрешности
     * Используется методом {@link Controller#rkStart()}
     * Создает новое окно в котором выводит график глобальной погрешности метода Рунге-Кутты.
     * График строится по заранее заполненному в методе {@link Controller#rk4()} массиву.
     * То есть метод {@link Controller#rk4()} был заранее вызван в методе {@link Controller#rkStart()}
     */
    private void epsChart() {
        Stage epsStage = new Stage();
        epsStage.setTitle("Погрешность по методу Рунге");

        final NumberAxis xAxis = new NumberAxis(); // Ось x
        final NumberAxis yAxis = new NumberAxis(); // Ось y

        // Создаем поле графика
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setCreateSymbols(false); // Убираем изображение точек на поле

        lineChart.setTitle("Погрешность по методу Рунге");

        XYChart.Series epsSeries = new XYChart.Series(); // Создание функции погрешности
        epsSeries.setName("График погрешности"); // Имя функции

        // Записываем точки графика
        for (int i = 0; i <= 100; i++) {
            epsSeries.getData().add(new XYChart.Data(i * H, epsArr[i]));
        }

        // Загружаем данные в график
        lineChart.getData().add(epsSeries);

        // Создаем сцену и загружаем в него график
        Scene scene = new Scene(lineChart, 800, 600);

        // Загружаем сцену и отображаем окно
        epsStage.setScene(scene);
        epsStage.show();
    }

    /**
     * При выборе пользователем метода Эйлера срабатывает данный метод.
     * Он вызывает методы {@link Controller#euler()} и {@link Controller#simple()}, которые заполнят массивы {@link Controller#eulerArr} и {@link Controller#simpleArr}
     * Далее создается окно, в котором по данным массивам строятся графики функций.
     */
    @FXML
    private void eulerStart()  {
        // Вызов методов для заполнение массивов точками
        euler();
        simple();

        Stage eulerStage = new Stage();
        eulerStage.setTitle("Метод эйлера");

        final NumberAxis xAxis = new NumberAxis(); // Ось x
        final NumberAxis yAxis = new NumberAxis(); // Ось y

        // Создание поле графика
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setCreateSymbols(false); // Убирает изображение точек на поле

        lineChart.setTitle("Метод Эйлера");

        XYChart.Series eulerSeries = new XYChart.Series(); // Создание первой функции - функции Эйлера
        eulerSeries.setName("Метод Эйлера"); // Имя функции

        //Записываем точки графика
        for (int i = 0; i <= N; i++) {
            eulerSeries.getData().add(new XYChart.Data(i * H, eulerArr[i]));
        }

        XYChart.Series simpleSeries = new XYChart.Series(); // Создание второй функции - исходной
        simpleSeries.setName("Исходная"); // Имя функции

        // Запись точек графика
        for (int i = 0; i <= N; i++) {
            simpleSeries.getData().add(new XYChart.Data(i * H, simpleArr[i]));
        }

        // Загрузка данных в график
        lineChart.getData().add(simpleSeries);
        lineChart.getData().add(eulerSeries);

        // Создание сцены и загрузка в нее графика
        Scene scene = new Scene(lineChart, 800, 600);

        // Загружаем сцену и отображаем окно
        eulerStage.setScene(scene);
        eulerStage.show();
    }

    /**
     * При выборе пользователем метода Рунге-Кутты срабатывает данный метод.
     * Он вызывает методы {@link Controller#rk4()} и {@link Controller#simple()}, которые заполнят массивы {@link Controller#rkArr}, {@link Controller#epsArr} и {@link Controller#simpleArr}.
     * Далее создается окно, в котором по данным массивам строятся графики функций.
     * В конце данного метода вызывается метод {@link Controller#epsChart()}, который выведет в новом окне график глобальной погрешности.
     */
    /* Вывод графиков методом Рунге-Кутты, а также графика погрешности в отдельном окне. Запускается при выборе пользователем данного метода. */
    @FXML
    private void rkStart() {
        // Вызов методов для заполнения массивов точками
        rk4();
        simple();

        Stage rkStage = new Stage();
        rkStage.setTitle("Метод Рунге-Кутты");

        final NumberAxis xAxis = new NumberAxis(); // Ось x
        final NumberAxis yAxis = new NumberAxis(); // Ось y

        //Создание поля для графиков
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setCreateSymbols(false); // Убираем изображение точек на поле

        lineChart.setTitle("Метод Рунге-Кутты");

        XYChart.Series rkSeries = new XYChart.Series(); // Создаем первую функцию - функцию Рунге-Кутты
        rkSeries.setName("Метод Рунге-Кутты"); // Имя функции

        //Записываем точки графика
        for (int i = 0; i <= N; i++) {
            rkSeries.getData().add(new XYChart.Data(i * H, rkArr[i]));
        }

        XYChart.Series simpleSeries = new XYChart.Series();  // Создаем вторую функцию - исходную
        simpleSeries.setName("Исходная"); // Имя функции

        // Записываем точки графика
        for (int i = 0; i <= N; i++) {
            simpleSeries.getData().add(new XYChart.Data(i * H, simpleArr[i]));
        }

        // Загружаем данные в график
        lineChart.getData().add(simpleSeries);
        lineChart.getData().add(rkSeries);

        Scene scene = new Scene(lineChart, 800, 600);  // Создаем сцену и загружаем в нее график

        //Загружаем сцену и отображаем окно
        rkStage.setScene(scene);
        rkStage.show();

        epsChart(); //Запускаем метод с выводом окна для погрешности
    }
}