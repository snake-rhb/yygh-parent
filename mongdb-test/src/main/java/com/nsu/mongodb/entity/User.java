package com.nsu.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
// 操作mongodb中的那个集合，相当于表
@Document("User")
public class User {
    // 添加一条数据时，mongodb为我们生成的_id规则
    @Id
    private String id;
    private String name;
    private Integer age;
    private String email;
    private String createData;
}
