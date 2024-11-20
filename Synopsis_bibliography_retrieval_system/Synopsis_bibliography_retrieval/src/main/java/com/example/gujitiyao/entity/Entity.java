package com.example.gujitiyao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Data
@Document(indexName = "entity")
@AllArgsConstructor
@NoArgsConstructor
public class Entity {
    @Id
    private Long id; // id
    @Field(type = FieldType.Long)
    private Long textID;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String bookName;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String title;
    @Field(type = FieldType.Keyword)
    private String label;
    @Field(type = FieldType.Keyword)
    private String entity;
    @Field(type = FieldType.Long)
    private Long startIdx;
    @Field(type = FieldType.Long)
    private Long endIdx;
}
