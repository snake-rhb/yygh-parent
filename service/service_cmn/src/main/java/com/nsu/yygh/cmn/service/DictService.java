package com.nsu.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nsu.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    // 查询数据字典中对应id的子节点
    List<Dict> findChildrenData(Long id);

    // 将数据字典写入到excel中
    void exportDictToExcel(HttpServletResponse response);

    // 上传一个excel文件，将这个文件中的数据插入到数据字典中
    void addDictData(MultipartFile file);
}
