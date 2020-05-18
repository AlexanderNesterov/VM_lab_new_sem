package labThree.lab;

import labThree.lab.io.FileWrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Math.abs;

public class Solution {
    private static class Function {
        static double f1(double X, double Y) {  // 2 * X
            return 3 * X * X * X;
        }

        static double f2(double X, double Y) { // X * X
            return 2 * X;
        }

        static double f3(double X, double Y) {  // 4 * X * X * X
            return 4 * 3 * X * X;
        }

        static double f4(double X, double Y) { // 7 * X * X * X * X
            return 7 * 4 * X * X * X;
        }
    }

    private FileWrite fileOut;
    private static final double mconst = 0.9375;
    private int error;
    private boolean direction;
    private double h;
    private double
            A, B,
            x, y,
            hmin,
            eps,
            epsmax;
    private int pointsCount, wAccuracy;

    public Solution() {
        this("/home/alexander/Documents/java projects/VM_lab_new_sem/data.txt",
                "/home/alexander/Documents/java projects/VM_lab_new_sem/output.txt");
    }

    public Solution(String inputfile, String outfile) {
        wAccuracy = error = 0;
        pointsCount = 1;
        readValuesFromFile(inputfile, outfile);
        h = abs(B - A) / 10.0;
        direction = (x == B);

        if ((direction ? B : A) != x || A >= B || epsmax < 0 || hmin <= 0) {
            error = 1;
        }
    }

    public void doCalculation() {
        if (error == 1) {
            System.out.println("Ошибка ввода!");
            return;
        }

        writeToFile();

        boolean lastdiv = false;
        while (true) {
            int multiplyLimit = 0;
            getNextY(h * getDirection());
            if (eps <= epsmax) {
                while (eps <= epsmax) {
                    if (lastdiv) {
                        lastdiv = false;
                        break;
                    }
                    if (multiplyLimit <= 2) {
                        multiplyLimit++;
                    } else {
                        multiplyLimit = 0;
                        break;
                    }
                    h *= 2;
                    getNextY(h * getDirection());
                    if (eps > epsmax) {
                        h /= 2;
                        break;
                    }
                }
            } else {
                if (eps > epsmax) {
                    while (eps > epsmax) {
                        if (h / 2 >= hmin) {
                            lastdiv = true;
                            h /= 2;
                            getNextY(h * getDirection());
                        } else {
                            h = hmin;
                            break;
                        }
                    }
                }
            }

            if ((direction ? x - h - A : B - x - h) < hmin || x + h == x) {
                break;
            }

            step();
            pointsCount++;
        }

        if ((direction ? x - A : B - x) >= 2 * hmin) {
            h = direction ? x - hmin - A : B - hmin - x;
            step();
            lastStep();
            pointsCount++;
        } else if ((direction ? x - A : B - x) <= 1.5 * hmin) {
            lastStep();
        } else {
            h = direction ? (x - A) / 2.0 : (B - x) / 2.0;
            step();
            lastStep();
            pointsCount++;
        }
        pointsCount++;
        writeResultsToFile();
    }

    private double f(double X, double Y) {
        return Function.f1(X, Y);
    }

    private double getDirection() {
        return (direction) ? -1.0 : 1.0;
    }

    private double checkRule(double yh1, double yh2) {
        return abs((yh1 - yh2) / mconst);
    }

    private double getNextY(double h) {
        double nextY = sasha129(x, y, h);

//        double nextY2 = sasha129(x + h / 2.0, sasha127(x, y, h / 2.0), h / 2.0);
        double nextY2 = sasha129(x, y, h);
        eps = checkRule(nextY, nextY2);
//        setZeroEps();

        return nextY;
    }

    private void step() {
        y = getNextY(h * getDirection());
        if (eps > epsmax) {
            wAccuracy++;
        }
        x += h * getDirection();
        fileOut.write(15, x, y, eps, h * getDirection());
    }

    private void lastStep() {
        h = direction ? x - A : B - x;
        step();
    }

    private void setZeroEps() {
        if (eps < 1e-15) {
            eps = 0;
        }
    }

    private double alik115(double X, double Y, double H) {
        double K1 = H * f(X, Y);
        double K2 = H * f(X + (1.0 / 2.0) * H, Y + (1.0 / 2.0) * K1);
        double K3 = H * f(X + (1.0 / 2.0) * H, Y + (1.0 / 2.0) * K2);
        double K4 = H * f(X + H, Y + K3);
        return Y + (1.0 / 6.0) * (K1 + 2.0 * K2 + 2.0 * K3 + K4);
    }

    private double sasha129(double X, double Y, double H) {
        double K1 = H * f(X, Y);
        double K2 = H * f(X + (1.0 / 2.0) * H, Y + (1.0 / 2.0) * K1);
        double K3 = H * f(X + (1.0 / 2.0) * H, Y + (1.0 / 4.0) * K1 + (1.0 / 4.0) * K2);
        double K4 = H * f(X + H, Y - K2 + 2 * K3);
        return Y + (1.0 / 6.0) * (K1 + 4.0 * K3 + K4);
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

    private double intMethod115(double X, double Y, double H) {
        double K1 = H * f(X, Y);
        double K2 = H * f(X + (1.0 / 3.0) * H, Y + (1.0 / 3.0) * K1);
        double K3 = H * f(X + (2.0 / 3.0) * H, Y + (2.0 / 3.0) * K2);
        return Y + 0.25 * (K1 + 3.0 * K3);
    }

    private void readValuesFromFile(String inputfile, String outfile) {
        fileOut = new FileWrite(outfile);
        try (BufferedReader fin = new BufferedReader(new FileReader(new File(inputfile)))) {
            A = Double.parseDouble(fin.readLine());
            B = Double.parseDouble(fin.readLine());
            x = Double.parseDouble(fin.readLine());
            y = Double.parseDouble(fin.readLine());
            hmin = Double.parseDouble(fin.readLine());
            epsmax = Double.parseDouble(fin.readLine());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

    }

    private void writeResultsToFile() {
        fileOut.write("\n");
        fileOut.write("Общее количество точек:   \t" + pointsCount);
        fileOut.write("Точность не достигнута в : " + wAccuracy);
    }

    private void writeToFile() {
        fileOut.cleanFile();
        fileOut.write("X\t\t\t\t\t\tY\t\t\t\t\t\tEPS\t\t\t\t\t\th");
        fileOut.write(15, x, y, eps);
    }
}
