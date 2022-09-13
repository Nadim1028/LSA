package SVD;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class SvdCalculation {

    /*public double[][] getSVDMatrix(double[][] M){
        Matrix A = new Matrix(M);
        SingularValueDecomposition s = A.svd();
    }*/

    public static void main(String[] args) {

        // create M-by-N matrix that doesn't have full rank
        int  numberOfDigitAfterDecimalPoint = 2,col = 8;//col is used for padding between two matrix cell
        /*Matrix A  = new Matrix(new double[][]
                {{2,2,0,0},
                {2,2,0,0},
                {3,3,0,0},
                {0,0,2,2},
                {0,0,1,1},
                {0,0,2,2}}
        );*/

       /* Matrix A  = new Matrix(new double[][]
                {{2,3}, {4,10}}
        );*/

       /* Matrix A  = new Matrix(new double[][]
                {{1,1,1}, {-1,-3,-3},{2,4,4}}
        );*/
       /* Matrix A = new Matrix(new double[][]{
                {1, 1, 1, 0, 0},
                {2, 2, 2, 0, 0},
                {1, 1, 1, 0, 0},
                {5, 5, 5, 0, 0},
                {0, 0, 0, 2, 2},
                {0, 0, 0, 3, 3},
                {0, 0, 0, 1, 1}
        });
        */

              Matrix A = new Matrix(new double[][] {
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
        });



       /* Matrix B = Matrix.random(5, 3);
        Matrix A = Matrix.random(col, row).times(B).times(B.transpose());*/

        System.out.println("Row = "+A.getRowDimension());
        System.out.println("Col = "+A.getColumnDimension());

        System.out.print("A = "  );
        A.print(col,numberOfDigitAfterDecimalPoint);

        // compute the singular value decomposition
        System.out.println("A = U S V^T");
        System.out.println();
        SingularValueDecomposition s = A.svd();

        System.out.print("U = ");//Term Feature Matrix
        Matrix U = s.getU();
        U.print(col, numberOfDigitAfterDecimalPoint);

        System.out.print("Sigma = ");
        Matrix S = s.getS();// relative strengths of the features
        S.print(col, numberOfDigitAfterDecimalPoint);

        System.out.print("V = ");
        Matrix V = s.getV();// documents feature matrix
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
/*
Singular values:
   5.8310   4.2426   0.0000   0.0000

Matrix U:
  -0.4851   0.0000  -0.4851   0.4851   0.2425   0.4851
  -0.4851   0.0000   0.8416   0.1584   0.0792   0.1584
  -0.7276   0.0000  -0.2377  -0.4290  -0.2145  -0.4290
   0.0000  -0.6667   0.0000   0.5556  -0.2222  -0.4444
   0.0000  -0.3333   0.0000  -0.2222   0.8889  -0.2222
   0.0000  -0.6667   0.0000  -0.4444  -0.2222   0.5556

Matrix V:
  -0.7071   0.0000  -0.7071   0.0000
  -0.7071   0.0000   0.7071   0.0000
   0.0000  -0.7071   0.0000  -0.7071
   0.0000  -0.7071   0.0000   0.7071

* */