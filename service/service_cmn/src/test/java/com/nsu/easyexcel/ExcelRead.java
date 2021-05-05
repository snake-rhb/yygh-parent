package com.nsu.easyexcel;

import com.alibaba.excel.EasyExcel;

/**
 * 读取excel测试
 */
public class ExcelRead {
    public static void main(String[] args) {
        // 读取文件的路径
        String filename = "F:\\学习资料\\尚医通项目\\excelTest.xlsx";

        // 文件名，数据实体类，读取监听器
        EasyExcel.read(filename, UserData.class, new ExcelListener())
                .sheet("sheet1")
                .doRead();
    }
}
