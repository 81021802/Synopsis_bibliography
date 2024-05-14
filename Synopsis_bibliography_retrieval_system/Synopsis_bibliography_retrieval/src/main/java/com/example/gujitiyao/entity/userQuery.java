package com.example.gujitiyao.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userQuery {
    private List<Fields> fields;
    private String queryString;
    private String analyzer;
    private String operator;
}
