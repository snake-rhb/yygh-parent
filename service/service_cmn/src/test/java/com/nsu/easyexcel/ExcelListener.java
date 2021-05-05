package com.nsu.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * 对excel进行读操作，需要使用监听器
 */
public class ExcelListener extends AnalysisEventListener<UserData> {

    /**
     * 读取excel中的方法，从第二行开始读取，不会读取表头
     * @param userData
     * @param analysisContext
     */
    @Override
    public void invoke(UserData userData, AnalysisContext analysisContext) {
        System.out.println(userData);
    }

    /**
     * 读取表头的内容
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息：" + headMap);
    }

    // 读取excel之后进行的操作
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
