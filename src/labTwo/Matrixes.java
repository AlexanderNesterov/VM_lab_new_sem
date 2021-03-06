package labTwo;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Matrixes {
    private static final int UPPER_RANGE = 20;
    private static final int LOWER_RANGE = -10;
    private static final String INPUT_FILE_NAME = "src/input.txt";
    private static final String OUTPUT_FILE_NAME = "src/output.txt";

    public static void writeToFile(double[][] matrix) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(OUTPUT_FILE_NAME)))) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    sb.append(matrix[i][j]).append(" ");
                }
                sb.append("\n");
            }

            writer.write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double[][] readFile() {
        double[][] matrix = null;

        try (Scanner sc = new Scanner(new File(INPUT_FILE_NAME))) {
            int size = sc.nextInt();
            matrix = new double[size][size];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    matrix[i][j] = sc.nextDouble();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return matrix;
    }

    public static double[][] generateIMatrix(int size) {
        double[][] matrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            matrix[i][i] = 1;
        }

        return matrix;
    }

    public static double findMaxAbsValue(double[][] matrix) {
        double maxValue = -1;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (Math.abs(matrix[i][j]) > maxValue) {
                    maxValue = Math.abs(matrix[i][j]);
                }
            }
        }

        return maxValue;
    }

    public static double[][] subtraction(double[][] matrixOne, double[][] matrixTwo) {
        double[][] result = new double[matrixOne.length][matrixOne.length];

        for (int i = 0; i < matrixOne.length; i++) {
            for (int j = 0; j < matrixOne.length; j++) {
                result[i][j] = matrixOne[i][j] - matrixTwo[i][j];
            }
        }

        return result;
    }

    public static double[][] copy(double[][] matrix) {
        double[][] copy = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[0].length; j++) {
                copy[i][j] = matrix[i][j];
            }
        }

        return copy;
    }

    public static void print(double[][] matrix) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                sb
                        .append(String.format("%7.5f", matrix[i][j]))
                        .append(" ");
            }

            sb.append("\n");
        }

        System.out.println(sb.toString());
    }

    public static double[][] generate(int size) {
        double[][] matrix = new double[size][size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextInt(UPPER_RANGE) + LOWER_RANGE;
            }
        }

        return matrix;
    }

    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
        if (firstMatrix.length != secondMatrix[0].length) {
            throw new ArithmeticException("Different sizes");
        }

        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];

        for (int i = 0; i < firstMatrix.length; i++) {
            for (int j = 0; j < secondMatrix[0].length; j++) {
                for (int k = 0; k < firstMatrix[0].length; k++) {
                    result[i][j] = result[i][j] + firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }

        return result;
    }

    public static double[][] transpose(double[][] originMatrix) {
        double[][] result = new double[originMatrix.length][originMatrix[0].length];

        for (int i = 0; i < originMatrix.length; i++) {
            for (int j = 0; j < originMatrix[0].length; j++) {
                result[i][j] = originMatrix[j][i];
            }
        }

        return result;
    }

    public static double[][] multiplyByNumber(double[][] matrix, double number) {
        double[][] result = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                result[i][j] = matrix[i][j] * number;
            }
        }

        return result;
    }

    public static double[][] reverseMatrix(double[][] matrix) {
        if (matrix.length != matrix[0].length) {
            throw new ArithmeticException("Wrong sizes");
        }

        double det = determinant(matrix);

        if (det == 0) {
            throw new ArithmeticException("Division by zero");
        }

        double reversedDeterminant = 1 / det;

        double[][] result = new double[matrix.length][matrix.length];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = findAlgExt(matrix, i, j);
            }
        }

        return Matrixes.transpose(multiplyByNumber(result, reversedDeterminant));
    }

    public static double determinant(double[][] matrix) {
        if (matrix.length != matrix[0].length) {
            throw new ArithmeticException("Different sizes");
        }

        double[][] copy = Matrixes.copy(matrix);
        double accum = 1;

        for (int i = 0; i < copy.length - 1; i++) {
            double[] result = findDivider(copy, i);

            if (result[0] == 0) {
                return 0;
            } else if (result[1] == 1) {
                accum *= -1;
            }

            double divider = result[0];
            accum *= divider;

            for (int k = i; k < copy[0].length; k++) {
                copy[i][k] = copy[i][k] / divider;
            }

            for (int j = i; j < copy[0].length - 1; j++) {
                double dev = copy[j + 1][i];

                for (int k = i; k < copy[0].length; k++) {
                    copy[j + 1][k] = copy[j + 1][k] - (dev * copy[i][k]);
                }
            }
        }

        double result = 1;

        for (int i = 0; i < copy.length; i++) {
            result *= copy[i][i];
        }

        return result * accum;
    }

    private static double[] findDivider(double[][] matrix, int position) {
        double[] result = new double[2];

        double maxNum = -20;
        int rowNum = position;
        for (int j = position; j < matrix.length; j++) {
            if (matrix[j][position] != 0 && matrix[j][position] > maxNum) {
                rowNum = j;
                maxNum = matrix[j][position];
            }
        }

        if (position != rowNum) {
            swapRows(matrix, position, rowNum);
            result[1] = 1;
        }
        result[0] = matrix[position][position];

        return result;
    }

    public static double findAlgExt(double[][] matrix, int rowNum, int colNum) {
        int algSize = matrix.length - 1;
        double[][] algExtMatrix = new double[algSize][algSize];

        for (int i = 0; i < matrix.length; i++) {
            if (i == rowNum) {
                continue;
            }

            for (int j = 0; j < matrix[0].length; j++) {
                if (j == colNum) {
                    continue;
                }

                int k = i;
                int l = j;
                if (i > rowNum) {
                    k--;
                }

                if (j > colNum) {
                    l--;
                }

                algExtMatrix[k][l] = matrix[i][j];
            }
        }

        return Math.pow(-1, (rowNum + colNum + 2)) * determinant(algExtMatrix);
    }

    private static void swapRows(double[][] matrix, int firstRow, int secondRow) {
        for (int k = firstRow; k < matrix[0].length; k++) {
            double temp = matrix[secondRow][k];
            matrix[secondRow][k] = matrix[firstRow][k];
            matrix[firstRow][k] = temp;
        }
    }
}
