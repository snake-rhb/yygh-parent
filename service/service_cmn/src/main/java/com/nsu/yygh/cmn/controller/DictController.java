package com.nsu.yygh.cmn.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.nsu.yygh.cmn.service.DictService;
import com.nsu.yygh.common.result.Result;
import com.nsu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api("数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * 根据id查找子节点
     * @param id
     * @return
     */
    @ApiOperation("根据id查找子节点")
    @GetMapping("/findChildrenData/{id}")
    public Result<List<Dict>> findChildrenData(@PathVariable("id") Long id) {
        List<Dict> childrenList = dictService.findChildrenData(id);
        return Result.ok(childrenList);
    }

    /**
     * 将数据字典写入到excel中
     * @param response 会导出一个excel文件，需要用到response对象
     * @return
     */
    @GetMapping("/exportDictToExcel")
    public void exportDictToExcel(HttpServletResponse response) {
        dictService.exportDictToExcel(response);
    }

    /**
     * 上传一个excel文件，将这个文件中的数据插入到数据字典中
     * @param file 上传的excel文件
     * @return
     */
    @PostMapping("/addDictData")
    public Result addDictData(MultipartFile file) {
        dictService.addDictData(file);
        return Result.ok();
    }

    /**
     * 根据医院的类型hospCode和值value查询医院等级接口
     * @param hospCode 医院类型，Hospital
     * @param value 医院的值
     * @return
     */
    @GetMapping("/getHospitalGrade/{hospCode}/{value}")
    public String getHospitalGrade(@PathVariable("hospCode") String hospCode,
                                   @PathVariable("value") String value) {
        String hospitalGrade = dictService.getDictName(hospCode, value);
        return hospitalGrade;
    }

    /**
     * 根据value值查询所在地区
     * @param value
     * @return
     */
    @GetMapping("/getLocation/{value}")
    public String getLocation(@PathVariable("value") String value) {
        return dictService.getDictName(null, value);
    }

    /**
     * 比如根据dict_id=Province，查询所有的省份
     * @param dictCode
     * @return
     */
    @ApiOperation("根据dict_id查询数据字典中的子节点")
    @GetMapping("/getListByDictCode/{dictCode}")
    public Result getListByDictCode(@PathVariable("dictCode") String dictCode) {
        List<Dict> list = dictService.getListByDictId(dictCode);
        return Result.ok(list);
    }
}
