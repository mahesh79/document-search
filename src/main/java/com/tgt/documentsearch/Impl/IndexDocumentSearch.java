package com.tgt.documentsearch.Impl;

import com.tgt.documentsearch.DocumentSearch;
import com.tgt.documentsearch.model.PatternCountModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Index Document Search is implemented by caching the word count across all files in a Map
 */
public class IndexDocumentSearch implements DocumentSearch {

    private static final Logger logger = Logger.getLogger(IndexDocumentSearch.class);


    private final Map<String, Map<String, Long>> indexedFile;

    public IndexDocumentSearch(String fileDirectoryPath) {
        indexedFile = prepareFileCacheIndex(fileDirectoryPath);
    }

    @Override
    public List<PatternCountModel> getPatternMatchCount(String searchAttribute) {
        List<PatternCountModel> returnModel = new ArrayList<>();
        indexedFile.forEach((x, y) -> returnModel.add(
                new PatternCountModel(x, y.getOrDefault(searchAttribute, Long.parseLong("0")).intValue())));
        return returnModel;
    }

    private Map<String, Map<String, Long>> prepareFileCacheIndex(String fileDirectoryPath) {
        Map<String, Map<String, Long>> responseMap = new TreeMap<>();
        try {
            for (File f : FileUtils.getFile(fileDirectoryPath).listFiles()) {
                Map<String, Long> wordCache = new HashMap<>();

                // file is split by the word boundry characters and collected as a string
                List<String> words = Arrays.asList(FileUtils.readFileToString(f, Charset.forName("utf-8")).split("\\b"))
                        .stream()
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.toList());

                // string collection is grouped and counted
                wordCache = words
                        .stream()
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())
                        );

                responseMap.put(f.getName(), wordCache);
            }
        } catch (IOException e) {
            logger.error("Failed to create word index cache", e);
        }
        return responseMap;
    }
}
