package labThree.lab;

import labThree.lab.io.FileWrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Math.abs;

public class Solution {
    private static class Functions {
        static double f1(double X, double Y) {
            return 28 * X * X * X; // X
        }

        static double f2(double X, double Y) {
            return 2 * X; // X^2
        }

        static double f3(double X, double Y) {
            return 4 * 3 * X * X; // 4*X^3
        }

        static double f4(double X, double Y) {
            return 9 * 4 * X * X * X; // + Y - X*X*X*X
        }
    }

    private FileWrite fileOut;  // вывод данных в файл
//    private static final double mconst = 0.875; // 1/2^3 - 1
    private static final double mconst = 0.9375; // 1/2^3 - 1
    private int error;
    private boolean direction;  // false - left-to-right, true - right-to-left
    private double h;
    private double
            A, B,
            x, y,   // c ~ x, yc ~ y
            hmin, hmax,   // минимальный шаг
            eps,    // оценка погрешности на шаге
            epsmax; // наибольшая допустимая погрешность
    private int pointsCount, wAccuracy; // кол-во точек, кол-во точек в которых не достигли точности

    public Solution() {
        this("/home/alexander/Documents/java projects/VM_lab_new_sem/data.txt",
                "/home/alexander/Documents/java projects/VM_lab_new_sem/output.txt");
    }

    public Solution(String inputfile, String outfile) {
        wAccuracy = error = 0;
        pointsCount = 1;
        dataFromFile(inputfile, outfile);
        hmax = h = abs(B - A) / 10.0;
        direction = (x == B);

        if ((direction ? B : A) != x || A >= B || epsmax < 0 || hmin <= 0) {
            error = 1;
        }
    }

    public void start() {
        if (error == 1) {
            System.out.println("Ошибка ввода!");
            return;
        }

        fileOut.cleanFile();
        fileOut.write("X\t\t\t\t\t\tY\t\t\t\t\t\tEPS\t\t\t\t\t\th");
        fileOut.write(15, x, y, eps);

        boolean lastdiv = false;
        while (true) {
            int multiplyLimit = 0;
            runge(h * direct());
            if (eps <= epsmax / 8) {
                while (eps <= epsmax / 8) {
                    if (h * 2 <= hmax * 200) {
                        if (lastdiv) {
                            lastdiv = false;
                            break;
                        }
                        if (multiplyLimit <= 5) {
                            multiplyLimit++;
                        } else {  // если достигли предела для умножения
                            multiplyLimit = 0;
                            break;
                        }
                        h *= 2;
                        runge(h * direct());
                        // если при умножении вышли за макс погрешность, то возвращаемся назад
                        if (eps > epsmax) {
                            h /= 2;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            } else {
                if (eps > epsmax) { // если точность слишком низкая
                    while (eps > epsmax) {
                        if (h / 2 >= hmin) {
                            lastdiv = true;
                            h /= 2;
                            runge(h * direct());
                        } else { // если не удалось достичь точности
                            h = hmin; // устанавливаем мин шаг
                            break;
                        }
                    }
                }
            }

            // Определение конца отрезка.
            if ((direction ? x - h - A : B - x - h) < hmin || x + h == x) { // Если после текущего шага B-x будет < hmin
                break;
            }

            step();
            pointsCount++;
        }

        // обрабатываем состояние, когда находимся у конца отрезка
        // выбор направления - direction ? [справа налево] : [слева направо]
        if ((direction ? x - A : B - x) >= 2 * hmin) { // если больше чем за 2 мин шага от края
            h = direction ? x - hmin - A : B - hmin - x;
            step();
            lastStep();
            pointsCount++;
        } else {
            if ((direction ? x - A : B - x) <= 1.5 * hmin) { // если меньше чем за 1.5 мин шага
                lastStep();
            } else { // если 1.5*hmin < остаток(B-x) < 2*hmin
                h = direction ? (x - A) / 2.0 : (B - x) / 2.0;
                step();
                lastStep();
                pointsCount++;
            }
        }
        pointsCount++;

        fileOut.write("\n");
        fileOut.write("Общее количество точек:   \t" + pointsCount);
        fileOut.write("Точность не достигнута в : " + wAccuracy);
    }

    private double f(double X, double Y) {
        return Functions.f1(X, Y);    // ф-ия изменяется при необходимости
    }

    // в зависимости от направления выдает -1(если справа налево) или 1(слева направо)
    private double direct() {
        return (direction) ? -1.0 : 1.0;
    }

    // Оценка погрешности по правилу Рунге
    private double rungeRule(double yh1, double yh2) {
        return abs((yh1 - yh2) / mconst);
    }

    // Вычисление Yi+1 + оценка погрешности
    private double runge(double h) {
        // 1) вычисление Yi+1
        double nextY = sasha129(x, y, h); // вычисление yi+1

        // 2) Оценка погрешности по правилу Рунге
        // вычисление первого h/2 шага внутри второго шага
        double nextY2 = sasha129(x + h / 2.0, sasha127(x, y, h / 2.0), h / 2.0);
//        double nextY2 = sasha127(x, y, h);
        eps = rungeRule(nextY, nextY2);

        if (eps < 1e-15) {// ~ машинный эпсилон
            eps = 0;
        }

        return nextY;
    }

    private void step() {
        y = runge(h * direct());        // вычисление Yi+1, оценка погрешности по правилу Рунге
        if (eps > epsmax) {
            wAccuracy++;
        }
        x += h * direct();  // делаем шаг с учётом направления
        fileOut.write(15, x, y, eps, h * direct()); // выводим данные
    }

    private void lastStep() {
        h = direction ? x - A : B - x;
        step();
    }

    // Нахождение yi+1. Вычисление по формуле (110), метод 3-го порядка
    private double intMethod115(double X, double Y, double H) {
        double K1 = H * f(X, Y);
        double K2 = H * f(X + (1.0 / 3.0) * H, Y + (1.0 / 3.0) * K1);
        double K3 = H * f(X + (2.0 / 3.0) * H, Y + (2.0 / 3.0) * K2);
        return Y + 0.25 * (K1 + 3.0 * K3);
    }

    private double alik115(double X, double Y, double H) {
        double K1 = H * f(X, Y);
        double K2 = H * f(X + (1.0 / 2.0) * H, Y + (1.0 / 2.0) * K1);
        double K3 = H * f(X + (1.0 / 2.0) * H, Y + (1.0 / 2.0) * K2);
        double K4 = H * f(X + H, Y + K3);
        return Y + (1.0 / 6.0) * (K1 + 2 * K2 + 2.0 * K3 + K4);
    }

    private double alik22(double X, double Y, double H) {
        double K1 = H * f(X, Y);
        double K2 = H * f(X + (1.0 / 2.0) * H, Y + (1.0 / 2.0) * K1);
        return Y + K2;
    }

    private double sasha127(double X, double Y, double H) {
        double K1 = H * f(X, Y);
        double K2 = H * f(X + (1.0 / 2.0) * H, Y + (1.0 / 2.0) * K1);
        double K3 = H * f(X + (1.0 / 2.0) * H, Y + (1.0 / 4.0) * K1 + (1.0 / 4.0) * K2);
        double K4 = H * f(X + H, Y - K2 + 2 * K3);
        double K5 = H * f(X + (2.0 / 3.0) * H, Y + (7.0 / 27.0) * K1 + (10.0 / 27.0) * K2 + (1.0 / 27.0) * K4);
        double K6 = H * f(X + (1.0 / 5.0) * H, Y - (1.0 / 625.0) * (28 * K1 - 125 * K2 + 546 * K3 + 54 * K4 + 378 * K5));
        return Y + (1.0 / 336.0) * (14 * K1 + 35.0 * K4 + 162 * K5 + 125 * K6);
    }

    private double sasha129(double X, double Y, double H) {
        double K1 = H * f(X, Y);
        double K2 = H * f(X + (1.0 / 2.0) * H, Y + (1.0 / 2.0) * K1);
        double K3 = H * f(X + (1.0 / 2.0) * H, Y + (1.0 / 4.0) * K1 + (1.0 / 4.0) * K2);
        double K4 = H * f(X + H, Y - K2 + 2 * K3);
        return Y + (1.0 / 6.0) * (K1 + 4.0 * K3 + K4);
    }

    private void dataFromFile(String inputfile, String outfile) {
        fileOut = new FileWrite(outfile);
        // считывание из файла. try-with-resources конструкция, закрывает поток сам
        try (BufferedReader fin = new BufferedReader(new FileReader(new File(inputfile)))) {
            A = Double.parseDouble(fin.readLine());
            B = Double.parseDouble(fin.readLine());
            x = Double.parseDouble(fin.readLine());
            y = Double.parseDouble(fin.readLine());
            hmin = Double.parseDouble(fin.readLine());
            epsmax = Double.parseDouble(fin.readLine());
        } catch (IOException ex) {
            //System.out.printf("File error!");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

    }
}
