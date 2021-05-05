package com.nsu.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 写入excel，需要有一个对应实体类
 */

@Data
public class UserData {
    // 写入excel时的头信息
    @ExcelProperty(value = "用户id", index = 0)
    private int uid;
    @ExcelProperty(value = "用户姓名", index = 1)
    private String uname;
    @ExcelProperty(value = "用户生日", index = 2)
    private Date birthday;
}
