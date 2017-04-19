package com.tgt.documentsearch;


import com.tgt.documentsearch.model.PatternCountModel;

import java.util.List;


public  interface DocumentSearch {


     List<PatternCountModel> getPatternMatchCount(String searchTerm);
}
