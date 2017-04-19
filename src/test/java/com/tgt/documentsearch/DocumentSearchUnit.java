package com.tgt.documentsearch;

import com.tgt.documentsearch.Impl.BasicDocumentSearch;
import com.tgt.documentsearch.Impl.IndexDocumentSearch;
import com.tgt.documentsearch.Impl.PatternDocumentSearch;
import com.tgt.documentsearch.model.PatternCountModel;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.ParseException;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.*;


@RunWith(Parameterized.class)
@SpringBootTest
public class DocumentSearchUnit {

	public DocumentSearch documentSearch;

    enum Type {BASIC,PATTERN, INDEX};

    private Type searchType;

	public static String pathForSampleFiles = "src/main/resources/sample_text";


	public DocumentSearchUnit(Type type, DocumentSearch documentSearch) {
		this.documentSearch = documentSearch;
        this.searchType = type;
	}

    @Parameterized.Parameters (name = "{index}: Document Search Type = {0}" )
    public static Collection<Object[]> testObject() {
        return Arrays.asList(new Object[][] {
                {Type.BASIC, new BasicDocumentSearch(pathForSampleFiles)},
                {Type.PATTERN, new PatternDocumentSearch(pathForSampleFiles)},
                {Type.INDEX, new IndexDocumentSearch(pathForSampleFiles)}
        });
    }

	@Test
	public void testTermPartOfStringRegex() throws IOException {
		List<PatternCountModel> expected = Arrays.asList(
				new PatternCountModel( "french_armed_forces.txt", 59),
				new PatternCountModel( "hitchhikers.txt", 24),
				new PatternCountModel( "warp_drive.txt", 9));
        Assume.assumeTrue(searchType == Type.BASIC || searchType == Type.PATTERN);
        List<PatternCountModel> result = documentSearch.getPatternMatchCount("the");
		assertEquals(expected,result);
	}

    @Test
    public void testTermPartOfStringWord() throws IOException {
        List<PatternCountModel> expected = Arrays.asList(
                new PatternCountModel( "french_armed_forces.txt", 57),
                new PatternCountModel( "hitchhikers.txt", 21),
                new PatternCountModel( "warp_drive.txt", 6));
        Assume.assumeTrue(searchType == Type.INDEX);
        List<PatternCountModel> result = documentSearch.getPatternMatchCount("the");
        assertEquals(expected,result);
    }

    @Test
    public void testSingleToken() throws IOException {
        List<PatternCountModel> expected = Arrays.asList(
                new PatternCountModel( "french_armed_forces.txt", 1),
                new PatternCountModel( "hitchhikers.txt", 0),
                new PatternCountModel( "warp_drive.txt", 0));
        List<PatternCountModel> result = documentSearch.getPatternMatchCount("across");
        assertEquals(expected,result);
    }

    @Test
    public void testTokenNotThere() throws IOException {
        List<PatternCountModel> expected = Arrays.asList(
                new PatternCountModel( "french_armed_forces.txt", 0),
                new PatternCountModel( "hitchhikers.txt", 0),
                new PatternCountModel( "warp_drive.txt", 0));
        List<PatternCountModel> result = documentSearch.getPatternMatchCount("9999999");
        assertEquals(expected,result);
    }

    @Test
    public void testTokenwithFullStop() throws IOException {
        List<PatternCountModel> expected = Arrays.asList(
                new PatternCountModel( "french_armed_forces.txt", 2),
                new PatternCountModel( "hitchhikers.txt", 0),
                new PatternCountModel( "warp_drive.txt", 0));
        List<PatternCountModel> result = documentSearch.getPatternMatchCount("won");
        assertEquals(expected,result);
    }
    @Test
    public void testTokenwithFullComma() throws IOException {
        List<PatternCountModel> expected = Arrays.asList(
                new PatternCountModel( "french_armed_forces.txt", 0),
                new PatternCountModel( "hitchhikers.txt", 2),
                new PatternCountModel( "warp_drive.txt", 0));
        List<PatternCountModel> result = documentSearch.getPatternMatchCount("guide");
        assertEquals(expected,result);
    }

    @Test
    public void testTokenForCaseSensitive() throws IOException {
        List<PatternCountModel> expected = Arrays.asList(
                new PatternCountModel( "french_armed_forces.txt", 0),
                new PatternCountModel( "hitchhikers.txt", 0),
                new PatternCountModel( "warp_drive.txt", 0));
        List<PatternCountModel> result = documentSearch.getPatternMatchCount("france");
        assertEquals(expected,result);
    }

    @Test
    public void testTokenWithOnlyNumbers() throws IOException {
        List<PatternCountModel> expected = Arrays.asList(
                new PatternCountModel( "french_armed_forces.txt", 1),
                new PatternCountModel( "hitchhikers.txt", 0),
                new PatternCountModel( "warp_drive.txt", 0));
        List<PatternCountModel> result = documentSearch.getPatternMatchCount("1942");
        assertEquals(expected,result);
    }

    @Test (expected = PatternSyntaxException.class)
    public void testInvalidTokenForRegex() throws PatternSyntaxException {
        Assume.assumeTrue(searchType == Type.PATTERN);
        documentSearch.getPatternMatchCount("[");
    }



}
