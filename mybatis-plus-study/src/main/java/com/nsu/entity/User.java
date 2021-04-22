package com.nsu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;

    // 使用mybatis-plus的注解：@TableField(fill = FieldFill.INSERT)
    // 在插入数据或更新数据时，自动修改创建或更新时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    // 加入一个乐观锁的version版本号，每次更新修改版本号
    // @TableField(fill = FieldFill.INSERT)默认填充一个版本号
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
