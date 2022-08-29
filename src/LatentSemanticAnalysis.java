//package org.dkpro.similarity.algorithms.sspace.util;

//import javax.xml.crypto.dsig.Transform;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import Tokenization.Tokenization;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.matrix.LogEntropyTransform;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixBuilder;
import edu.ucla.sspace.matrix.SVD;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.SparseArray;
import edu.ucla.sspace.util.SparseIntHashArray;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vector;

public class LatentSemanticAnalysis implements SemanticSpace, Serializable {

    private static final long serialVersionUID = 220l;
    private static final String PROPERTY_PREFIX =
            "edu.ucla.sspace.lsa.LatentSemanticAnalysis";
    public static final String MATRIX_TRANSFORM_PROPERTY =
            PROPERTY_PREFIX + ".transform";
    public static final String LSA_DIMENSIONS_PROPERTY =
            PROPERTY_PREFIX + ".dimensions";
    public static final String LSA_SVD_ALGORITHM_PROPERTY =
            PROPERTY_PREFIX + ".svd.algorithm";
    private static final String LSA_SSPACE_NAME =
            "lsa-semantic-space";
    private static final Logger LSA_LOGGER =
            Logger.getLogger(LatentSemanticAnalysis.class.getName());
    private final ConcurrentMap<String,Integer> termToIndex;

    private final AtomicInteger termIndexCounter;
    private final MatrixBuilder termDocumentMatrixBuilder;
    private Matrix wordSpace;
    public Matrix documentSpace;
    private Properties properties;


    public LatentSemanticAnalysis() throws IOException {
        this(System.getProperties());
        properties = System.getProperties();
        //System.out.println("Properties ==================================== "+System.getProperties());
    }


    public LatentSemanticAnalysis(Properties properties) throws IOException {
        termToIndex = new ConcurrentHashMap<String,Integer>();
        termIndexCounter = new AtomicInteger(0);

        termDocumentMatrixBuilder = Matrices.getMatrixBuilderForSVD();

        wordSpace = null;
        documentSpace = null;
    }

    public Map<String, Integer> getTermsCounts(String filePath) throws IOException {
        Tokenization tokenization = new Tokenization();
        Collection<String> tokens = tokenization.tokenGenerator(filePath);
        System.out.println("Tokens of file = "+filePath+"->"+tokens);
        Map<String,Integer> termCounts = processDocument(tokens);
        return termCounts;

        /*try {

            BufferedReader br = null;
            br = new BufferedReader(new FileReader("src/data/uncommentedCode.txt"));

            processDocument(br);
            br.close();
        }


        catch (IOException e) {
            e.printStackTrace();
        }
        */
    }


    public Map<String, Integer> processDocument(Collection<String> documentTokens) {

        Map<String,Integer> termCounts = new HashMap<String,Integer>(1000);

        for (String word : documentTokens) {
            if (word.equals(IteratorFactory.EMPTY_TOKEN)) {
                continue;
            }

            addTerm(word);
            Integer termCount = termCounts.get(word);

            // update the term count
            termCounts.put(word, (termCount == null)
                    ? 1
                    : 1 + termCount.intValue());
        }


        if (termCounts.isEmpty()) {
            return termCounts;
        }

        int totalNumberOfUniqueWords = termIndexCounter.get();

        SparseArray<Integer> documentColumn =
                new SparseIntHashArray(totalNumberOfUniqueWords);
        for (Map.Entry<String,Integer> e : termCounts.entrySet()) {
            documentColumn.set(termToIndex.get(e.getKey()), e.getValue());
        }

        termDocumentMatrixBuilder.addColumn(documentColumn);

        return termCounts;
    }


    /**
     This method is not used currently
     */
    @Override
    public void processDocument(BufferedReader document)
            throws IOException
    {
        Iterator<String> documentTokens = IteratorFactory.tokenize(document);
        Collection<String> tokens = new ArrayList<String>();
        while (documentTokens.hasNext()) {
            String str = documentTokens.next();
            System.out.println(str);
            tokens.add(str);
        }
        System.out.println("======================================================================");
        document.close();

        processDocument(tokens);
    }



    public void addTerm(String term) {
        Integer index = termToIndex.get(term);

        if (index == null) {

            synchronized(this) {
                // recheck to see if the term was added while blocking
                index = termToIndex.get(term);
                // if some other thread has not already added this term while
                // the current thread was blocking waiting on the lock, then add
                // it.
                if (index == null) {
                    index = Integer.valueOf(termIndexCounter.getAndIncrement());
                    termToIndex.put(term, index);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getWords() {
        return Collections.unmodifiableSet(termToIndex.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getVector(String word) {
        // determine the index for the word
        Integer index = termToIndex.get(word);

        return (index == null)
                ? null
                : wordSpace.getRowVector(index.intValue());
    }

    /**
     * Returns the semantics of the document as represented by a numeric vector.
     * Note that document semantics are represented in an entirely different
     * space, so the corresponding semantic dimensions in the word space will be
     * completely unrelated.  However, document vectors may be compared to find
     * those document with similar content.<p>
     *
     * Similar to {@code getVector}, this method is only to be used after
     * {@code processSpace} has been called.<p>
     *
     * Implementation note: If a specific document ordering is needed, caution
     * should be used when using this class in a multi-threaded environment.
     * Beacuse the document number is based on what order it was
     * <i>processed</i>, no guarantee is made that this will correspond with the
     * original document ordering as it exists in the corpus files.  However, in
     * a single-threaded environment, the ordering will be preserved.
     *
     * @param documentNumber the number of the document according to when it was
     *        processed
     *
     * @return the semantics of the document in the document space
     */
    public DoubleVector getDocumentVector(int documentNumber) {
        if (documentNumber < 0 || documentNumber >= documentSpace.rows()) {
            throw new IllegalArgumentException(
                    "Document number is not within the bounds of the number of "
                            + "documents: " + documentNumber);
        }
        return documentSpace.getRowVector(documentNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSpaceName() {
        return LSA_SSPACE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVectorLength() {
        return wordSpace.getRowVector(0).length();
//        return wordSpace.columns();
    }

    /**
     * {@inheritDoc}
     *
     * @param properties {@inheritDoc} See this class's {@link
     *        LatentSemanticAnalysis javadoc} for the full list of supported
     *        properties.
     */
    @Override
    public void processSpace(Properties properties) {
        try {
            // first ensure that we are no longer writing to the matrix
            termDocumentMatrixBuilder.finish();

            Transform transform = (Transform) new LogEntropyTransform();

            String transformClass =
                    properties.getProperty(MATRIX_TRANSFORM_PROPERTY);
            if (transformClass != null) {
                try {
                    Class clazz = Class.forName(transformClass);
                    transform = (Transform)(clazz.newInstance());
                }
                // perform a general catch here due to the number of possible
                // things that could go wrong.  Rethrow all exceptions as an
                // error.
                catch (Exception e) {
                    throw new Error(e);
                }
            }

            LSA_LOGGER.info("performing " + transform + " transform");

            // Get the finished matrix file from the builder
            File termDocumentMatrix = termDocumentMatrixBuilder.getFile();
            if (LSA_LOGGER.isLoggable(Level.FINE)) {
                LSA_LOGGER.fine("stored term-document matrix in format " +
                        termDocumentMatrixBuilder.getMatrixFormat()
                        + " at " + termDocumentMatrix.getAbsolutePath());
            }

            // Convert the raw term counts using the specified transform
            File transformedMatrix = (File) transform.transform(termDocumentMatrix,
                    termDocumentMatrixBuilder.getMatrixFormat());

            if (LSA_LOGGER.isLoggable(Level.FINE)) {
                LSA_LOGGER.fine("transformed matrix to " +
                        transformedMatrix.getAbsolutePath());
            }

            int dimensions = 300; // default
            String userSpecfiedDims =
                    properties.getProperty(LSA_DIMENSIONS_PROPERTY);
            if (userSpecfiedDims != null) {
                try {
                    dimensions = Integer.parseInt(userSpecfiedDims);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException(
                            LSA_DIMENSIONS_PROPERTY + " is not an integer: " +
                                    userSpecfiedDims);
                }
            }

            LSA_LOGGER.info("reducing to " + dimensions + " dimensions");

            // Determine whether the user specified any specific SVD algorithm
            // or whether the fastest available should be used.
            String svdProp = properties.getProperty(LSA_SVD_ALGORITHM_PROPERTY);
            SVD.Algorithm alg = (svdProp == null)
                    ? SVD.Algorithm.ANY
                    : SVD.Algorithm.valueOf(svdProp);

            // Compute SVD on the pre-processed matrix.
            Matrix[] usv = SVD.svd(transformedMatrix, alg,
                    termDocumentMatrixBuilder.getMatrixFormat(),
                    dimensions);

            // Load the left factor matrix, which is the word semantic space
            wordSpace = usv[0];
            // We transpose the document space to provide easier access to the
            // document vectors, which in the un-transposed version are the
            // columns.  NOTE: if the Matrix interface ever adds a getColumn()
            // method, it might be better to use that instead.
            documentSpace = Matrices.transpose(usv[2]);
        } catch (IOException ioe) {
            //rethrow as Error
            throw new IOError(ioe);
        }
    }
}






