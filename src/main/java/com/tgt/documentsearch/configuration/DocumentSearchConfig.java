package com.tgt.documentsearch.configuration;


import com.tgt.documentsearch.DocumentSearch;
import com.tgt.documentsearch.DocumentSearchConsole;
import com.tgt.documentsearch.Impl.BasicDocumentSearch;
import com.tgt.documentsearch.Impl.IndexDocumentSearch;
import com.tgt.documentsearch.Impl.PatternDocumentSearch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentSearchConfig {

    @Bean(name = "basicDocumentSearch")
    public DocumentSearch basicDocumentSearch(@Value("${directory.path.for.search}") String fileDirectoryPath) {
        return new BasicDocumentSearch(fileDirectoryPath);
    }

    @Bean(name = "patternDocumentSearch")
    public DocumentSearch patternDocumentSearch(@Value("${directory.path.for.search}") String fileDirectoryPath) {
        return new PatternDocumentSearch(fileDirectoryPath);
    }

    @Bean(name = "indexDocumentSearch")
    public DocumentSearch indexDocumentSearch(@Value("${directory.path.for.search}") String fileDirectoryPath) {
        return new IndexDocumentSearch(fileDirectoryPath);
    }

    @Bean(name = "documentSearchConsole")
    public DocumentSearchConsole documentSearchConsole() {
        return new DocumentSearchConsole();
    }
}
