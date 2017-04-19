package com.tgt.documentsearch;

import com.tgt.documentsearch.Impl.IndexDocumentSearch;
import com.tgt.documentsearch.model.PatternCountModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;

public class DocumentSearchConsole {

    private static final Logger logger = Logger.getLogger(DocumentSearchConsole.class);
    @Autowired
    @Qualifier("basicDocumentSearch")
    private DocumentSearch basicDocumentSearch;

    @Autowired
    @Qualifier("patternDocumentSearch")
    private DocumentSearch patternDocumentSearch;

    @Autowired
    @Qualifier("indexDocumentSearch")
    private DocumentSearch IndexDocumentSearch;

    public void run() {
        logger.info("Document Search Console App starting");
        String searchAttribute;
        int option;
        DocumentSearch selectedDocumentSearch;
        List<PatternCountModel> searchResult;
        // create a scanner object
        try (Scanner scanner = new Scanner(System.in)) {

            // Request for the string to be searched
            System.out.print("Enter the search term: \t");
            // Reading the string till enter for next line
            searchAttribute = scanner.nextLine().trim();
            //Request for search type
            System.out.print("Select your search type:\n" +
                    "\t\t1. Basic Search\n" +
                    "\t\t2. Pattern Search\n" +
                    "\t\t3. Index Search\n");
            // Capture the selected option
            option = scanner.nextInt();
        }


        // Set search object based on selected option
        try {
            if (option == 1) {
                selectedDocumentSearch = basicDocumentSearch;
            } else if (option == 2) {
                selectedDocumentSearch = patternDocumentSearch;
            } else if (option == 3) {
                selectedDocumentSearch = IndexDocumentSearch;
            } else {
                selectedDocumentSearch = null;
                System.out.println("Selection option not valid: " + option);
            }
            searchResult = selectedDocumentSearch.getPatternMatchCount(searchAttribute);

            System.out.println("Search results:");
            searchResult.forEach(x -> System.out.println(MessageFormat.format(
                    "\t{0} - {1} matches", x.getFileName(), x.getMatchNumber())));
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }
}
