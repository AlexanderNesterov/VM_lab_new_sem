package labTwo;

public class Solution {
    /**
     * Размерность матрицы
     */
    private static final int SIZE = 100;

    public static void main(String[] args) {
//        double[][] matrix = Matrixes.readFile();
        /**
         * Генерирует обратную матрицу
         */
        double[][] matrix = Matrixes.generate(SIZE);

//        System.out.println("Исходная: ");
//        Matrixes.print(matrix);

        System.out.println("Определитель: " + Matrixes.determinant(matrix));

//        System.out.println("Обратная: ");
        double[][] reverse = Matrixes.reverseMatrix(matrix);
//        Matrixes.print(reverse);

//        System.out.println("Проверка: ");
        double[][] multiplied = Matrixes.multiply(matrix, reverse);
//        Matrixes.print(multiplied);

        double[][] iMatrix = Matrixes.generateIMatrix(SIZE);
        double[][] subtracted = Matrixes.subtraction(multiplied, iMatrix);

        System.out.println("Наибольшее по модулю : " + Matrixes.findMaxAbsValue(subtracted));

//        Matrixes.writeToFile(reverse);
    }
}
