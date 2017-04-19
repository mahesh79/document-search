package com.tgt.documentsearch.Impl;


import ch.qos.logback.classic.turbo.MatchingFilter;
import com.tgt.documentsearch.DocumentSearch;
import com.tgt.documentsearch.model.PatternCountModel;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.expression.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * Pattern Document Search is implemented using a REGEX API like Matcher and Pattern
 */

public class PatternDocumentSearch implements DocumentSearch {

    private static final Logger logger = Logger.getLogger(PatternDocumentSearch.class);

    private Map<String, String> parsedFileMap;

    public PatternDocumentSearch(String fileDirectoryPath) {
        parsedFileMap = fileParsedToString(fileDirectoryPath);
    }

    @Override
    public List<PatternCountModel> getPatternMatchCount(String searchAttribute) throws PatternSyntaxException {
        List<PatternCountModel> returnModel = new ArrayList<>();
        Pattern pattern = null;
        pattern = Pattern.compile(searchAttribute);
        for (Map.Entry<String, String> entry : parsedFileMap.entrySet()) {
            Matcher matcher = pattern.matcher(entry.getValue());
            int matchCount = 0;
            while (matcher.find()) {
                matchCount++;
            }
            returnModel.add(new PatternCountModel(entry.getKey(), matchCount));
        }

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
