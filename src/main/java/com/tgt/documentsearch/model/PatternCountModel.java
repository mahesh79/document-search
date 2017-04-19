package com.tgt.documentsearch.model;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PatternCountModel {

    private String fileName;
    private int matchNumber;
}
