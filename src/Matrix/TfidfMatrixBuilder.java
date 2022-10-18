package Matrix;

import java.util.ArrayList;

public class TfidfMatrixBuilder {
        int numOfDoc;//,wordFrequencyInSelfDoc;
        double[][] termFrequencyMatrix,tfidfMatrix;
        ArrayList<Integer> totalWordsInEachDoc,numOfDocsWithThisWord;

        public TfidfMatrixBuilder(int numOfDoc, double[][] termFrequencyMatrix,
                                  ArrayList<Integer> totalWordsInEachDoc, ArrayList<Integer> numOfDocsWithThisWord) {
                this.numOfDoc = numOfDoc;
                this.termFrequencyMatrix = termFrequencyMatrix;
                this.totalWordsInEachDoc = totalWordsInEachDoc;
                this.numOfDocsWithThisWord = numOfDocsWithThisWord;
        }

        public   double[][] getTfidfMatrix(){
                tfidfMatrix = new double[termFrequencyMatrix.length][numOfDoc];
                for(int i=0;i<termFrequencyMatrix.length;i++){
                        for (int j = 0; j < termFrequencyMatrix[i].length; j++){
                                double  tf = getTFValue(termFrequencyMatrix[i][j],totalWordsInEachDoc.get(j));
                                double  idf  = getIDFValue(numOfDocsWithThisWord.get(i));
                                tfidfMatrix[i][j] = Double.parseDouble(String.format("%.3f", tf*idf));
                                //System.out.print( tfidfMatrix[i][j]+"   ");
                               // System.out.println("Multiplication = "+ tf*idf);
                        }
                }


                return tfidfMatrix;
        }

        public double getTFValue(double wordFrequency, double totalWordsInThisDoc) {
                return wordFrequency/totalWordsInThisDoc;
        }


        public double getIDFValue(double numOfDocumentWithThisWord){

                return Math.log10(numOfDoc/numOfDocumentWithThisWord);
        }
}
