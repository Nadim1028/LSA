package Matrix;


import java.io.*;

class MatrixMultiplication {

    static void printMatrix(double M[][],
                            int rowSize,
                            int colSize)
    {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++)
                System.out.print(M[i][j] + " ");

            System.out.println();
        }
    }


    static double[][] multiplyMatrix(
            int row1, int col1, double A[][],
            int row2, int col2, double B[][])
    {
        int i, j, k;

        System.out.println("\nMatrix A:");
        printMatrix(A, row1, col1);
        System.out.println("\nMatrix B:");
        printMatrix(B, row2, col2);

        if (row2 != col1) {

            System.out.println(
                    "\nMultiplication Not Possible");
            //return 0;
        }

        // Matrix to store the result
        // The product matrix will
        // be of size row1 x col2
        double  C[][] = new double [row1][col2];

        // Multiply the two matrices
        for (i = 0; i < row1; i++) {
            for (j = 0; j < col2; j++) {
                for (k = 0; k < row2; k++)
                    C[i][j] += A[i][k] * B[k][j];
            }
        }


        //printMatrix(C, row1, col2);
        return C;
    }

    public static void main(String[] args)
    {

        int row1 = 4, col1 = 3, row2 = 3, col2 = 4;

        double A[][] = { { 1, 1, 1 },
                { 2, 2, 2 },
                { 3, 3, 3 },
                { 4, 4, 4 } };

        double B[][] = { { 1, 1, 1, 1 },
                { 2, 2, 2, 2 },
                { 3, 3, 3, 3 } };

        double C[][] = multiplyMatrix(row1, col1, A,
                row2, col2, B);
        System.out.println("\nResultant Matrix:");
        printMatrix(C,row1,col2);
    }
}
