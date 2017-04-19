package com.tgt.documentsearch;

import com.tgt.documentsearch.Impl.BasicDocumentSearch;
import com.tgt.documentsearch.Impl.IndexDocumentSearch;
import com.tgt.documentsearch.Impl.PatternDocumentSearch;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Random;

@RunWith(Parameterized.class)
public class DocumentSearchPerformance {

    private static final Logger logger = Logger.getLogger(DocumentSearchPerformance.class);

    public DocumentSearch documentSearch;

    enum Type {BASIC, PATTERN, INDEX}

    ;

    private Type searchType;
    private static Random random;
    private Long totalSearchTimePerType;

    final static int MAX_ITERATION = 10;
    final static int EACH_ITERATION_SIZE = 200000;

    static List<String> randomSearchTerm;

    public static String pathForSampleFiles = "src/main/resources/sample_text";

    @BeforeClass
    public static void setup() throws IOException {
        random = new Random();
        randomSearchTerm = prepareSearchTerms();
    }

    public DocumentSearchPerformance(Type searchType, DocumentSearch documentSearch) {
        this.documentSearch = documentSearch;
        this.searchType = searchType;
    }

    @Parameterized.Parameters (name = "{index}: Document Search Type = {0}" )
    public static Collection<Object[]> testObject() {
        return Arrays.asList(new Object[][]{
                {Type.BASIC, new BasicDocumentSearch(pathForSampleFiles)},
                {Type.PATTERN, new PatternDocumentSearch(pathForSampleFiles)},
                {Type.INDEX, new IndexDocumentSearch(pathForSampleFiles)}
        });
    }

    @Test
    public void testPerformanceRunForAllSearchTypes() {
        totalSearchTimePerType = totalTimeForSearchRequests(documentSearch);
        displayResults(totalSearchTimePerType);

    }

    private long totalTimeForSearchRequests(DocumentSearch documentSearch) {
        long totalIterativeTime = 0;
        for (int i = 0; i < MAX_ITERATION; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < EACH_ITERATION_SIZE; j++) {
                documentSearch
                        .getPatternMatchCount(randomSearchTerm.get(random.nextInt(randomSearchTerm.size())));
            }

            totalIterativeTime = totalIterativeTime + (System.currentTimeMillis() - start);
            logger.info("Processed  " + i + " out of " + MAX_ITERATION + " in " + totalIterativeTime + " ms");
        }
        return totalIterativeTime;
    }

    private static List<String> prepareSearchTerms() throws IOException {
        List<String> wordList = new ArrayList<>();
        for (File f : FileUtils.getFile(pathForSampleFiles).listFiles()) {
            wordList.addAll(Arrays.asList(FileUtils.readFileToString(f, Charset.forName("utf-8")).split("\\s"))
                    .stream()
                    .map(x -> x.replaceAll("\\W", ""))
                    .collect(Collectors.toList()));
        }

        return wordList;
    }

    private void displayResults(long totalSearchTimePerType) {
        System.out.println("\nLoad test for document search type: " + searchType);
        System.out.println("  Total Number of Iterations: " + MAX_ITERATION);
        System.out.println("  Total number of search term per Iteration: " + EACH_ITERATION_SIZE);
        System.out.println("  Elapsed search time - Total:" + totalSearchTimePerType + " ms");
        System.out.println("  Elapsed search time per Iteration - Average:" + totalSearchTimePerType / (MAX_ITERATION) + " ms\n");
    }
}
