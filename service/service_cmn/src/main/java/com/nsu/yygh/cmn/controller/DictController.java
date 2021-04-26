package com.nsu.yygh.cmn.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nsu.yygh.cmn.service.DictService;
import com.nsu.yygh.common.result.Result;
import com.nsu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("数据字典接口")
@RestController
@RequestMapping("/api/dict")
@CrossOrigin
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
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> childrenList = dictService.list(wrapper);
        return Result.ok(childrenList);
    }
}
