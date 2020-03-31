package labTwo;

public class Solution {
    public static void main(String[] args) {
        double[][] matrix = Matrixes.generate(3);

        System.out.println("Исходная: ");
        Matrixes.print(matrix);

        System.out.println("Определитель: " + Matrixes.determinant(matrix));

        System.out.println("Обратная: ");
        double[][] reverse = Matrixes.reverseMatrix(matrix);
        Matrixes.print(reverse);

        Matrixes.print(Matrixes.multiply(matrix, reverse));
    }
}
