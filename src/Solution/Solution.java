package Solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Solution {
    private static double xPrev;
    private static double xCurr;
    private static double xNext;
    private static double eps;
    private static double iterationsNumber;


    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("input"));

        xPrev = Double.parseDouble(sc.nextLine());
        xCurr = Double.parseDouble(sc.nextLine());
        eps = Double.parseDouble(sc.nextLine());
        iterationsNumber = Integer.parseInt(sc.nextLine());

        System.out.println(calculate());
    }

    private static double calculate() {
        int counter = -1;
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

        System.out.println(counter);
        System.out.println(xCurr);
        return function(xCurr);
    }

    private static double function(double x) {
        return x * x - 4;
    }

    private static double nextX() {
        if (function(xCurr) - function(xPrev) == 0) {
            System.out.println("sssssssss");
        }
        return xCurr - ((function(xCurr) * (xCurr - xPrev)) / (function(xCurr) - function(xPrev)));
    }
}
