package com.example.gujitiyao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;

@Data
@Document(indexName = "book")
@AllArgsConstructor
@NoArgsConstructor

/*注解：@Document用来声明Java对象与ElasticSearch索引的关系
     indexName 索引名称(是字母的话必须是小写字母)
     type 索引类型
     shards 主分区数量，默认5
     replicas 副本分区数量，默认1
     createIndex 索引不存在时，是否自动创建索引，默认true
     不建议自动创建索引(自动创建的索引 是按着默认类型和默认分词器)
     注解：@Id 表示索引的主键
    注解：@Field 用来描述字段的ES数据类型，是否分词等配置，等于Mapping描述
    index 设置字段是否索引，默认是true，如果是false则该字段不能被查询
    store 默认为no,被store标记的fields被存储在和index不同的fragment中，以便于快速检索。虽然store占用磁盘空间，但是减少了计算。
    type 数据类型(text、keyword、date、object、geo等)
    analyzer 对字段使用分词器，注意一般如果要使用分词器，字段的type一般是text。
    format 定义日期时间格式，详细见 官方文档: https://www.elastic.co/guide/reference/mapping/date-format/.
    注解：@CompletionField 定义关键词索引 要完成补全搜索
    analyzer 对字段使用分词器，注意一般如果要使用分词器，字段的type一般是text。
    searchAnalyzer 显示指定搜索时分词器，默认是和索引是同一个，保证分词的一致性。
    maxInputLength:设置单个输入的长度，默认为50 UTF-16 代码点*/

/*常用数据类型：
        简单类型：

        字符串类型: - string(不再支持)可知string类型的field已经被移除了, 我们需要用text或keyword类型来代替string.
        text：会分词，不支持聚合
        keyword：不会分词，将全部内容作为一个词条，支持聚合

        数字类型: 尽可能选择范围小的数据类型, 字段的长度越短, 索引和搜索的效率越高;(优先考虑使用带缩放因子的浮点类型)
        byte :            有符号的8位整数, 范围: [-128 ~ 127]
        short :           有符号的16位整数, 范围: [-32768 ~ 32767]
        integer :        有符号的32位整数, 范围: [−231 ~ 231-1]
        long :            有符号的64位整数, 范围: [−263 ~ 263-1]
        float :             32位单精度浮点数
        double :         64位双精度浮点数
        half_float :     16位半精度IEEE 754浮点类型
        scaled_float : 缩放类型的的浮点数, 比如price字段只需精确到分, 57.34缩放因子为100, 存储结果为5734

        日期类型:date JSON没有日期数据类型, 所以在ES中, 日期可以是:
        —   包含格式化日期的字符串 “2018-10-01”, 或"2018/10/01 12:10:30"    (可以通过 format属性 定义日期时间格式）DateFormat 官方文法.
        —   代表时间毫秒数的长整型数字.
        —   代表时间秒数的整数.

        布尔类型： boolean 可以接受表示真、假的字符串或数字:
        —  真值: true, “true”, “on”, “yes”, “1”…
        —  假值: false, “false”, “off”, “no”, “0”, “”(空字符串), 0.0, 0

        复杂类型:

        数组[]： 由数组中第一个非空值决定数组类型(type = FieldType.Keyword)
        List集合： 由数组中第一个非空值决定数组类型(type = FieldType.Keyword)
        嵌套类型: list里泛型是object形式的或自定义对象(type = FieldType.Nested)
        对象：{ }object for single JSON objects 单个JSON对象(type = FieldType.Object)*/

public class Book implements Serializable{
    @Id
    private Long id; // id
    /*ik_smart 粗精度，ik_max_word 细精度*/
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String bookName;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_smart"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String bookAuthor;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_smart"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String title;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String location;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_smart"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String mainTitle;
    @Field(type = FieldType.Keyword)
    private String volumes;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_smart"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String parallelTitle;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_smart"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String part;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_smart"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String category;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String edition;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String editionDynasty;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String editionType;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String author;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String author1Dynasty;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String author1;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String responsibility1;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String author2Dynasty;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String author2;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String responsibility2;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String author3Dynasty;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String author3;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String responsibility3;
    @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String summary;

}
