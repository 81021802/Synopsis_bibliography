package com.example.gujitiyao.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int code; //状态码
    private String msg; // 消息
    private long total; //记录条数
    private Object data; //数据对象

}
