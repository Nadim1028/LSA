import CosineSimilarity.CosineAngleCalculator;
import Jama.Matrix;
import Matrix.TokenFinderInDocs;
import edu.ucla.sspace.vector.DoubleVector;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TestSimilarity
{
    public static void main(String[] args) throws IOException
    {
        String filePath1="src/Output/clean_data1.txt",filePath2="src/Output/clean_data2.txt",filePath3="src/Output/nadim.txt";
        int[]  valueOfTerms1,valueOfTerms2;
        LatentSemanticAnalysis latentSemanticAnalysis = new LatentSemanticAnalysis();
        Map<String,Integer> termCounts1=latentSemanticAnalysis.getTermsCounts(filePath1);
        Map<String,Integer> termCounts2=latentSemanticAnalysis.getTermsCounts(filePath2);
        Map<String,Integer> termCounts3=latentSemanticAnalysis.getTermsCounts(filePath3);


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
            //System.out.println(lowerCaseString);
            caseInsensitiveTokenSet.add(lowerCaseString);
        }


        List<String> sortedList = new ArrayList<>(caseInsensitiveTokenSet);
        Collections.sort(sortedList);
        System.out.println("Sorted :"+sortedList);
        System.out.println("CastInsensitiveUniqueTokensSize : "+caseInsensitiveTokenSet.size());

        double[][] matrix = new double[sortedList.size()][3];

        for(int i=0;i<sortedList.size();i++){
            int wordFrequency1 = new TokenFinderInDocs(termCounts1,sortedList.get(i)).getTermFrequency();
            int wordFrequency2 = new TokenFinderInDocs(termCounts2,sortedList.get(i)).getTermFrequency();
            int wordFrequency3 = new TokenFinderInDocs(termCounts3,sortedList.get(i)).getTermFrequency();
            //System.out.println(sortedList.get(i) + ":" + "d1="+wordFrequency1+",d2="+wordFrequency2 + ",d3="+wordFrequency3);
            matrix[i][0] = wordFrequency1;
            matrix[i][1] = wordFrequency2;
            matrix[i][2] = wordFrequency3;
        }
        System.out.println("d1   d2   d3");
        for (int i = 0; i < matrix.length; i++) {
            // Loop through all elements of current row

            for (int j = 0; j < matrix[i].length; j++)
                System.out.print(matrix[i][j] + "   ");
            System.out.println("<=== "+sortedList.get(i));
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


    public static void printTermsValue( Map<String,Integer> termCounts1, Map<String,Integer> termCounts2){
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

    }
}
