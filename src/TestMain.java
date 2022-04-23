import edu.ucla.sspace.vector.DoubleVector;

import java.io.IOException;
import java.util.Set;
import java.util.Vector;

public class TestMain {
    public static void main(String[] args) throws IOException {
        LatentSemanticAnalysis latentSemanticAnalysis = new LatentSemanticAnalysis();
        latentSemanticAnalysis.readFile();
        Set<String> str = latentSemanticAnalysis.getWords();
        System.out.println(str);
        System.out.println("============================================");
        //DoubleVector spaceName =  latentSemanticAnalysis.getDocumentVector(3);
       // System.out.println(spaceName);

    }
}
