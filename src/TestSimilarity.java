import CosineSimilarity.CosineAngleCalculator;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import Matrix.TfidfMatrixBuilder;
import Matrix.TokenFinderInDocs;
import java.io.IOException;
import java.util.*;

public class TestSimilarity
{
    public static void main(String[] args) throws IOException
    {
        String filePath1="src/Output/clean_data1.txt",filePath2="src/Output/clean_data2.txt",filePath3="src/Output/clean_data3.txt";
        int[]  valueOfTerms1,valueOfTerms2;
        int numberOfDigitAfterDecimalPoint = 2,spaceRange=10;
        LatentSemanticAnalysis latentSemanticAnalysis = new LatentSemanticAnalysis();
        Map<String,Integer> termCounts1=latentSemanticAnalysis.getTermsCounts(filePath1);
        Map<String,Integer> termCounts2=latentSemanticAnalysis.getTermsCounts(filePath2);
        Map<String,Integer> termCounts3=latentSemanticAnalysis.getTermsCounts(filePath3);

        ArrayList<Integer> totalWordsInEachDoc = new ArrayList<Integer>(), numOfDocsWithThisWord = new ArrayList<Integer>();
        totalWordsInEachDoc.add(termCounts1.size());
        totalWordsInEachDoc.add(termCounts2.size());
        totalWordsInEachDoc.add(termCounts3.size());


        System.out.println("doc1 unique keyword size : "+termCounts1.size());
        System.out.println("doc2 unique keyword size : "+termCounts2.size());
        System.out.println("doc3 unique keyword size : "+termCounts3.size());

        System.out.println("*****Words frequency in each docs.*****");

        System.out.println("Doc1 : "+termCounts1);
        System.out.println("Doc2 : "+termCounts2);
        System.out.println("Doc3 : "+termCounts3);


        Set<String> str = latentSemanticAnalysis.getWords();
        System.out.println("*******************TokensInAllDocuments****************************");
        System.out.println(str);
        System.out.println("total tokens size : " + str.size());

        System.out.println("*****CastInsensitiveUniqueTokens*****");
        Set<String> caseInsensitiveTokenSet= new HashSet<>();
        for(String token : str){
            String lowerCaseString = token.toLowerCase();
            caseInsensitiveTokenSet.add(lowerCaseString);
        }


        List<String> sortedList = new ArrayList<>(caseInsensitiveTokenSet);
        Collections.sort(sortedList);
        System.out.println("Sorted :"+sortedList);
        System.out.println("CastInsensitiveUniqueTokensSize : "+caseInsensitiveTokenSet.size());

        double[][] matrix = new double[sortedList.size()][3],tfidfMatrix = new double[sortedList.size()][3];

        for(int i=0;i<sortedList.size();i++){
            int wordFrequency1 = new TokenFinderInDocs(termCounts1,sortedList.get(i)).getTermFrequency();
            int wordFrequency2 = new TokenFinderInDocs(termCounts2,sortedList.get(i)).getTermFrequency();
            int wordFrequency3 = new TokenFinderInDocs(termCounts3,sortedList.get(i)).getTermFrequency();
            //System.out.println(sortedList.get(i) + ":" + "d1="+wordFrequency1+",d2="+wordFrequency2 + ",d3="+wordFrequency3);
            matrix[i][0] = wordFrequency1;
            matrix[i][1] = wordFrequency2;
            matrix[i][2] = wordFrequency3;
            int count = 0;
            if(wordFrequency1 > 0)
                count++;
            if(wordFrequency2 > 0)
                count++;
            if(wordFrequency3 > 0)
                count++;
            numOfDocsWithThisWord.add(count);

        }
       System.out.println("**********Term Frequency Matrix***********");
        for (int i = 0; i < matrix.length; i++) {
            System.out.print((i+1)+". "+sortedList.get(i)+" => ");
            for (int j = 0; j < matrix[i].length; j++)
                System.out.print(matrix[i][j] + "   ");
            System.out.println();
            //System.out.println("   "+numOfDocsWithThisWord.get(i));
        }
        TfidfMatrixBuilder tfidfMatrixBuilder = new TfidfMatrixBuilder(3,matrix,totalWordsInEachDoc,numOfDocsWithThisWord);
        tfidfMatrix = tfidfMatrixBuilder.getTfidfMatrix();

        System.out.println("\n\n********TFIDF Matrix*********");
        for (int i = 0; i < tfidfMatrix.length; i++) {
            System.out.print((i+1)+". "+sortedList.get(i)+" => ");
            for (int j = 0; j < tfidfMatrix[i].length; j++)
                System.out.print(tfidfMatrix[i][j] + "   ");
            System.out.println();
        }

        System.out.println("\n\n************SVD MATRIX***********");
        Matrix A = new Matrix(tfidfMatrix);
        SingularValueDecomposition s = A.svd();

        System.out.print("U = ");
        Matrix U = s.getU();
        U.print(spaceRange, numberOfDigitAfterDecimalPoint);

        System.out.print("Sigma = ");
        Matrix S = s.getS();
        S.print(spaceRange, numberOfDigitAfterDecimalPoint);

        System.out.print("V = ");
        Matrix V = s.getV();
        V.print(spaceRange, numberOfDigitAfterDecimalPoint);

        /*Matrix X = new Matrix(U.getColumnDimension(),U.getColumnDimension());
        X = U.copy();*/
        double[][] C =  U.copy().getArray();
        ArrayList<Integer> col1Terms = new ArrayList<>(),col2Terms = new ArrayList<>(), col3Terms = new ArrayList<>();
        System.out.println("U with positive value : ");

        for(int i=0;i<U.getRowDimension();i++){
            for (int j =0;j<U.getColumnDimension();j++){
                if(C[i][j]<=0)
                    C[i][j] = -1* C[i][j];
                C[i][j] = Double.parseDouble(String.format("%.2f",C[i][j]));
                if(C[i][j]< 0.001)
                    C[i][j] = 0;

                System.out.print(C[i][j]+"         ");
            }

            if( C[i][0] != 0)
                col1Terms.add(i);
            if(C[i][1] !=0)
                col2Terms.add(i);
            if(C[i][2] !=0)
                col3Terms.add(i);
            System.out.println();
        }

      /*  System.out.println("Col1 array number : = ");
        for (int i=0;i<col2Terms.size();i++){
            System.out.println("value = " + col2Terms.get(i) );
        }*/

        ArrayList<Integer> mergedTerms = getMergedTokensOfDocumentsPair(col2Terms,col3Terms);
        System.out.println("Merged Terms = "+mergedTerms);
        int[] vectorA = new int[mergedTerms.size()], vectorB =  new int[mergedTerms.size()];

        for (int i=0;i<mergedTerms.size();i++){
            vectorA[i] = (int) matrix[i][1];
            vectorB[i] = (int) matrix[i][2];
            System.out.println(sortedList.get(mergedTerms.get(i)) + "; A = "+ vectorA[i] + "; B = " +  vectorB[i]);
        }

        CosineAngleCalculator cosineAngleCalculator = new CosineAngleCalculator();

        System.out.println("Cosine Similarity = "+(cosineAngleCalculator.getCosineSimilarity(vectorA,vectorB)*100)+"%");

    }

    public static ArrayList<Integer> getMergedTokensOfDocumentsPair(ArrayList<Integer> col1Terms,  ArrayList<Integer> col2Terms){
        col1Terms.addAll(col2Terms);
        Collections.sort(col1Terms);
        //System.out.println(col1Terms);

        Set<Integer> mergedTokens= new HashSet<>();
        for (Integer tokensIndex:col1Terms) {
            mergedTokens.add(tokensIndex);
        }

        //System.out.println("Set of Indexes = "+mergedTokens);

        return new ArrayList<>(mergedTokens);
    }

}






 /*Map<String,Integer> termCounts1=latentSemanticAnalysis.getTermsCounts(filePath1);
        Map<String,Integer> termCounts2=latentSemanticAnalysis.getTermsCounts(filePath2);

        valueOfTerms1 = new int[termCounts1.size()];
        valueOfTerms2 = new int[termCounts2.size()];
        int counter=0;
        for (Map.Entry<String, Integer> term :
                termCounts1.entrySet()) {
            valueOfTerms1[counter]=term.getValue();
        }
        counter=0;
        for (Map.Entry<String, Integer> term :
                termCounts2.entrySet()) {
            valueOfTerms2[counter]=term.getValue();
        }

        CosineAngleCalculator cosineAngleCalculator = new CosineAngleCalculator();
        double result = cosineAngleCalculator.getCosineSimilarity(valueOfTerms1,valueOfTerms2);
        printTermsValue(termCounts1,termCounts2);
        System.out.println("Cosine Similarity = "+(result*100)+"%");*/

/*public static void printTermsValue( Map<String,Integer> termCounts1, Map<String,Integer> termCounts2){
        System.out.println("*************FirstDocument**************");
        for (Map.Entry<String, Integer> term :
                termCounts1.entrySet()) {

            System.out.print(term.getKey() + ":");
            System.out.println(term.getValue());
        }

        System.out.println("*************SecondDocument**************");
        for (Map.Entry<String, Integer> term :
                termCounts2.entrySet()) {

            System.out.print(term.getKey() + ":");
            System.out.println(term.getValue());
        }

    }*/