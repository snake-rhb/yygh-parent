package com.nsu.yygh.hosp.controller;

import com.nsu.yygh.common.result.Result;
import com.nsu.yygh.hosp.service.HospitalSetService;
import com.nsu.yygh.model.hosp.HospitalSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "医院管理控制")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 查询所有的医院信息
     * @return
     */
    @ApiOperation("查询所有的医院信息")
    @GetMapping("/findAll")
    public Result<List<HospitalSet>> findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }


    /**
     * 对医院实现逻辑删除
     * @param id
     * @return
     */
    @ApiOperation("逻辑删除医院")
    @DeleteMapping("/delete/{id}")
    public Result deleteHospitalSet(@PathVariable("id") Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return Result.flag(flag);
    }
}
