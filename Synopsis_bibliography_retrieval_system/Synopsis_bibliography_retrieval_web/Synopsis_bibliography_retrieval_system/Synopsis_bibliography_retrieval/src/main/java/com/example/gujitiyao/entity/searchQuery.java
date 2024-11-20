package com.example.gujitiyao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class searchQuery {
    private String bookName;
    private String bookAuthor;
    private String title;
    private String part;
    private String category;
    private String edition;
    private String author;
    private String summary;
    private Integer pageNum;
    private Integer pageSize;
    private String preTags;
    private String postTags;
    // 将SearchForm对象转换为Map<String, String[]>的方法
    public Map<String, String> toMap() {
        Map<String, String> paramMap = new HashMap<>();
        if (bookName != "") {
            paramMap.put("bookName", bookName);
        }
        if (bookAuthor != "") {
            paramMap.put("bookAuthor", bookAuthor);
        }
        if (title != "") {
            paramMap.put("title", title);
        }
        if (part != "") {
            paramMap.put("part", part);
        }
        if (category != "") {
            paramMap.put("category", category);
        }
        if (edition != "") {
            paramMap.put("edition", edition);
        }
        if (author != "") {
            paramMap.put("author", author);
        }
        if (summary != "") {
            paramMap.put("summary", summary);
        }

        // ...处理其他字段
        return paramMap;
    }
}
