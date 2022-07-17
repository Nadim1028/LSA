package SVD;


import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class SvdCalculation {
    public static void main(String[] args) {

//        1, 0, 0, 1, 0, 0, 0, 0, 0
//         101000000
//         110000000
//         0 1 1 0 1 0 0 0 0
//         011200000
//         010010000
//         010010000
//         001100000
//         01000000 1
//         00000111 0
//         000000111
//         000000011

        // create M-by-N matrix that doesn't have full rank
        int  numberOfDigitAfterDecimalPoint = 2,col = 3;//row is the number of digit after decimal point
      /*  Matrix A  = new Matrix(new double[][]
                {{3,3,2}, {2,3,-2},{0,0,0}}
                );*/

       /* Matrix A  = new Matrix(new double[][]
                {{2,3}, {4,10}}
        );*/

        Matrix A  = new Matrix(new double[][]
                {{1,1,1}, {-1,-3,-3},{2,4,4}}
        );

       /* Matrix A = new Matrix(new double[][] {
                {1, 0, 0, 1, 0, 0, 0, 0, 0 },
                {1, 0, 1, 0, 0, 0, 0, 0, 0 },
                {1, 1, 0, 0, 0, 0, 0, 0, 0 },
                {0, 1, 1, 0, 1, 0, 0, 0, 0 },
                {0, 1, 1, 2, 0, 0, 0, 0, 0 },
                {0, 1, 0, 0, 1, 0, 0, 0, 0 },
                {0, 1, 0, 0, 1, 0, 0, 0, 0 },
                {0, 0, 1, 1, 0, 0, 0, 0, 0 },
                {0, 1, 0, 0, 0, 0, 0, 0, 1 },
                {0, 0, 0, 0, 0, 1, 1, 1, 0 },
                {0, 0, 0, 0, 0, 0, 1, 1, 1 },
                {0, 0, 0, 0, 0, 0, 0, 1, 1 }
        });*/

       /* Matrix B = Matrix.random(5, 3);
        Matrix A = Matrix.random(col, row).times(B).times(B.transpose());*/

        System.out.println("Row = "+A.getRowDimension());
        System.out.println("Col = "+A.getColumnDimension());

        System.out.print("A = "  );
        A.print(col,numberOfDigitAfterDecimalPoint);

        // compute the singular vallue decomposition
        System.out.println("A = U S V^T");
        System.out.println();
        SingularValueDecomposition s = A.svd();

        System.out.print("U = ");
        Matrix U = s.getU();
        U.print(col, numberOfDigitAfterDecimalPoint);

        System.out.print("Sigma = ");
        Matrix S = s.getS();
        S.print(col, numberOfDigitAfterDecimalPoint);

        System.out.print("V = ");
        Matrix V = s.getV();
        V.print(col, numberOfDigitAfterDecimalPoint);

        System.out.println("rank = " + s.rank());
        System.out.println("condition number = " + s.cond());
        System.out.println("2-norm = " + s.norm2());

        // print out singular values
        System.out.print("singular values = ");
        Matrix svalues = new Matrix(s.getSingularValues(), 1);
        svalues.print(col, numberOfDigitAfterDecimalPoint);
    }

}