package labOne;

import java.io.*;
import java.util.Scanner;

public class Solution {
    private static double xPrev;
    private static double xCurr;
    private static double xNext;
    private static double eps;
    private static int counter;
    private static double iterationsNumber;

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new File("input"));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("output")));

        xPrev = Double.parseDouble(sc.nextLine());
        xCurr = Double.parseDouble(sc.nextLine());
        eps = Double.parseDouble(sc.nextLine());
        iterationsNumber = Integer.parseInt(sc.nextLine());

        double foundX;
        try {
            foundX = calculate();

            String x = "x: " + foundX + "\n";
            String func = "f(x): " + function(foundX) + "\n";
            String iterations = "Number of iterations: " + counter;

            bos.write(x.getBytes());
            bos.write(func.getBytes());
            bos.write(iterations.getBytes());
        } catch (Exception e) {
            bos.write(e.getMessage().getBytes());
        } finally {
            bos.close();
        }
    }

    private static double calculate() {
        counter = -1;
        xNext = nextX();

        while (++counter < iterationsNumber && Math.abs(xNext - xCurr) >= eps) {
            xPrev = xCurr;
            xCurr = xNext;
            xNext = nextX();
        }

        while (++counter < iterationsNumber && (xPrev - xNext) > 0) {
            xPrev = xCurr;
            xCurr = xNext;
            xNext = nextX();
        }

/*        System.out.println("counter: " + counter);
        System.out.println("x: " + xCurr);*/
        return xCurr;
    }

    private static double function(double x) {
        return x * x - 4;
    }

    private static double nextX() {
        if (function(xCurr) - function(xPrev) == 0) {
            throw new ArithmeticException("IER: Division by 0");
        }
        return xCurr - ((function(xCurr) * (xCurr - xPrev)) / (function(xCurr) - function(xPrev)));
    }
}
