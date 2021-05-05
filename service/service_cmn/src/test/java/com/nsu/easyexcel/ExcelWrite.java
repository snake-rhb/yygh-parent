package com.nsu.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 演示excel写操作
 */
public class ExcelWrite {
    public static void main(String[] args) {
        // 保存excel文件的路径
        String filename = "F:\\学习资料\\尚医通项目\\excelTest.xlsx";

        // 写入文件
        ExcelWriterBuilder write = EasyExcel.write(filename, UserData.class);

        // 写入的数据
        List<UserData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserData userData = new UserData();
            userData.setUid(i);
            userData.setUname("test" + i);
            userData.setBirthday(new Date());

            list.add(userData);
        }

        // 创建sheet工作表,写入数据
        write.sheet("sheet1").doWrite(list);
    }
}
