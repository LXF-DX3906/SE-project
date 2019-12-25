package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @program: SE_imsge_share
 * @description: 对应前端type
 * @author: Xuefei Lv
 * @create: 2019/12/25
 **/
@Getter
@Setter
public class Type {
    private Map<Integer,String> type_dict;
    public Type(){
        type_dict = new HashMap<>();
        type_dict.put(1,"城市");
        type_dict.put(2,"星空");
        type_dict.put(3,"旅游");
        type_dict.put(4,"春天");
        type_dict.put(5,"教育");
        type_dict.put(6,"美食");
        type_dict.put(7,"健身");
        type_dict.put(8,"风景");
        type_dict.put(9,"金融");
        type_dict.put(10,"背景");
        type_dict.put(11,"医疗");
        type_dict.put(12,"植物");
        type_dict.put(13,"阅读");
        type_dict.put(14,"动物");
        type_dict.put(15,"科技");
    }
}
