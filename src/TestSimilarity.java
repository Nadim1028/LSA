import CosineSimilarity.CosineAngleCalculator;
import edu.ucla.sspace.vector.DoubleVector;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class TestSimilarity
{
    public static void main(String[] args) throws IOException
    {
        String filePath1="src/Output/clean_data1.txt",filePath2="src/Output/clean_data2.txt";
        int[]  valueOfTerms1,valueOfTerms2;
        LatentSemanticAnalysis latentSemanticAnalysis = new LatentSemanticAnalysis();
        Map<String,Integer> termCounts1=latentSemanticAnalysis.getTermsCounts(filePath1);
        Map<String,Integer> termCounts2=latentSemanticAnalysis.getTermsCounts(filePath2);
        Map<String,Integer> termCounts3=latentSemanticAnalysis.getTermsCounts(filePath2);
        Map<String,Integer> termCounts4=latentSemanticAnalysis.getTermsCounts(filePath1);

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


        Set<String> str = latentSemanticAnalysis.getWords();
        System.out.println("*******************Tokens****************************");
        System.out.println(str);
        System.out.println("*****************************************************");

        latentSemanticAnalysis.processSpace(System.getProperties());
        System.out.println("Document Space ======= "+latentSemanticAnalysis.documentSpace.rows());
        System.out.println("Vector  ============== "+latentSemanticAnalysis.getDocumentVector(3));


    }

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
