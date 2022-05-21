package CosineSimilarity;

public class CosineAngleCalculator {
    int[] vectorA, vectorB;
    int[] vectorX,vectorY;

    public  double getCosineSimilarity(int[] vectorA, int[] vectorB) {
        int lengthB=0,lengthA=0;

        if(vectorA.length>=vectorB.length){
            lengthB=vectorB.length;
            vectorX = new int[vectorA.length];
            for(int i=0;i<vectorA.length;i++){
                if(lengthB>=i+1){
                    vectorX[i]= vectorB[i];
                }
                else{
                    vectorX[i]= 0;
                }
            }

            double result = calculateValue(vectorA,vectorX);
            if(result>1)
                result=1;
            return result;
        }

        else{
            lengthA=vectorA.length;
            vectorY = new int[vectorB.length];
            for(int i=0;i<vectorB.length;i++){
                if(lengthA>=i+1){
                    vectorY[i]= vectorA[i];
                }
                else{
                    vectorY[i]= 0;
                }
            }
            double result = calculateValue(vectorB,vectorY);
            if(result>1)
                result=1;
            return result;
        }



    }

    public double calculateValue(int[] vectorA, int[] vectorB){
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
