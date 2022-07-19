package Matrix;

import java.util.Map;

public class TokenFinderInDocs {
    Map<String,Integer> termCounts;
    String searchKey;
    int termFrequency=0;

    public TokenFinderInDocs(Map<String, Integer> termCounts, String searchKey) {
        this.termCounts = termCounts;
        this.searchKey = searchKey;
    }

    public int getTermFrequency(){
        for (Map.Entry<String, Integer> term :
                termCounts.entrySet())
        {
            if (searchKey.equalsIgnoreCase(term.getKey())) {
                termFrequency = term.getValue();
            }

        }
        return termFrequency;
    }


}
