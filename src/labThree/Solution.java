package labThree;

public class Solution {

    private static double A = 1;
    private static double B = 1;
    private static double C = 1;
    private static double Y_C = 1;
    private static double h = 0.5;
    private static double EPS = 0.5;
    private static double hMin = 0.001;
    private static double dir = 0;
    private static double x0;
    private static double y0;
    private static double y1;


    public static void main(String[] args) {
        chooseDir();

        y1 = y0 + ((double) 1 / 6) * (k1(x0, y0) + 4 * k3(x0, y0) + k4(x0, y0));
        double yEps = y0 + ((double) 1/ 336) * (14 * k1(x0, y0) + 35 * k4(x0, y0) + 162 * k5(x0, y0) + 125 * k6(x0, y0));
        double newEps = Math.abs(y1 - yEps);

        if (newEps > EPS) {
            double he = h * Math.pow(EPS / newEps, (double) 1 / 4);
        }


        /////////////////////////////////////

        double x1 = x0 + h;
        y0 = y1;
        x0 = x1;

        if
    }

    private static double f(double x, double y) {
        return x * x;
    }

    private static double k1(double x, double y) {
        return h * f(x, y);
    }

    private static double k2(double x, double y) {
        return h * f(x + h / 2, y + k1(x, y) / 2);
    }

    private static double k3(double x, double y) {
        return h * f(x + h / 2, y + k1(x, y) / 4 + k2(x, y) / 4);
    }

    private static double k4(double x, double y) {
        return h * f(x + h, y - k2(x, y) + 2 * k3(x, y));
    }

    private static double k5(double x, double y) {
        return h * f(x + ((double) 2 / 3) * h, y + ((double) 7 / 27) * k1(x, y) + ((double) 10 / 27) * k2(x, y) + ((double) 1 / 27) * k4(x, y));
    }

    private static double k6(double x, double y) {
        return h * f(x + (h * h) / 5, y - ((double) 1 / 625) * (28 * k1(x, y) - 125 * k2(x, y) + 546 * k3(x, y) + 54 * k4(x, y) + 378 * k5(x, y)));
    }

    private static void chooseDir() {
        if (C == A) {
            dir = 1;
        } else if (C == B) {
            dir = -1;
        }

        if (dir < 0) {
            double temp = A;
            A = B;
            B = temp;
        }

        x0 = A;
        y0 = Y_C;
        h = (B - A) / 10;
    }
}
