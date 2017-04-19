package com.tgt.documentsearch.Impl;


import com.tgt.documentsearch.DocumentSearch;
import com.tgt.documentsearch.model.PatternCountModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import org.apache.log4j.Logger;

/**
 * Basic Document Search is implemented using a simple count match provided by StringUtils
 */

public class BasicDocumentSearch implements DocumentSearch {
    private static final Logger logger = Logger.getLogger(BasicDocumentSearch.class);

    private Map<String, String> parsedFileMap;

    /**
     * Set the directory path and parse all the files to a string
     */
    public BasicDocumentSearch(String fileDirectoryPath) {
        parsedFileMap = fileParsedToString(fileDirectoryPath);
    }

    /**
     * match the term in each of the file content
     */
    @Override
    public List<PatternCountModel> getPatternMatchCount(String searchAttribute) {
        List<PatternCountModel> returnModel = new ArrayList<>();
        parsedFileMap.forEach((x, y) -> returnModel
                .add(new PatternCountModel(x, StringUtils.countMatches(y, searchAttribute))));
        return returnModel;
    }


    private Map<String, String> fileParsedToString(String fileDirectoryPath) {

        Map<String, String> parserResult = new TreeMap<>();

        for (File f : FileUtils.getFile(fileDirectoryPath).listFiles()) {
            try {
                parserResult.put(f.getName(), FileUtils.readFileToString(f, Charset.forName("utf-8")));

            } catch (IOException ex) {
                logger.error("Failed to read and parse the file", ex);
            }
        }
        return parserResult;
    }
}
