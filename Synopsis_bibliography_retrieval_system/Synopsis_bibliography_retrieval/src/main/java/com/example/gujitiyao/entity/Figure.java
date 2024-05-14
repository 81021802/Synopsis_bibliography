package com.example.gujitiyao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Data
@Document(indexName = "figure")
@AllArgsConstructor
@NoArgsConstructor
public class Figure {
    @Id
    private Long id; // id
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String name;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String dynasty;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String styleName;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String literaryName;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String nativePlace;
    @Field(type = FieldType.Long)
    private Long count;
}
