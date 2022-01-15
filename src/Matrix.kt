import java.util.Arrays;

public class Matrix {

    public double[][] matrix;

    public Matrix(int i, int j) {
        matrix = new double[i][j];
    }

    public Matrix(double[][] matrix) {
        int length = matrix[0].length;
        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i].length != length) throw new IllegalArgumentException("Matrix has missing elements");
        }
        this.matrix = matrix;
    }

    public Matrix minor(int i, int j) {
        if (i > matrix.length) throw new IllegalArgumentException("Value i is outside indexed bounds");
        else if (j > matrix[0].length) throw new IllegalArgumentException("Value j is outside indexed bounds");
        double[][] minorMatrix = new double[matrix.length - 1][matrix[0].length - 1];
        for (int k = 0, l = 0; k < matrix.length; k++) {
            if (k != i - 1) {
                for (int m = 0, n = 0; m < matrix[0].length; m++) {
                    if (m != j - 1) {
                        minorMatrix[l++][n++] = matrix[k][m];
                    }
                }
            }
        }
        return new Matrix(minorMatrix);
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            if (i == 0) temp.append("｢");
            else if (i == matrix.length - 1) temp.append(" ");
            else temp.append("|");
            for (int j = 0; j < matrix[i].length; j++) {
                temp.append("  ").append(matrix[i][j]).append("  ");
            }
            if (i == 0) temp.append("\n");
            else if (i == matrix.length - 1) temp.append("｣");
            else temp.append("|\n");
        }
        return temp.toString();
    }
}
