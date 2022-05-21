package CosineSimilarity;

public class TestMain {
    public static void main(String[] args) {
        int[] A ={1,2,2,3,40}, B={1,2,2,3,40}, C={9,9,9,1};
        CosineAngleCalculator c = new CosineAngleCalculator();
        System.out.println("Value = "+c.getCosineSimilarity(A,B));
    }
}
