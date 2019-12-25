package com.example.demo.entity;

import lombok.*;
import java.util.List;

@Data
@Getter
@Setter
@ToString
public class Response<T> {
    //文字描述的错误类型
    public String message;

    //返回查询结果/操作结果
    public List<T> result;
}