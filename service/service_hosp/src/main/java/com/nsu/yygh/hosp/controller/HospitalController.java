package com.nsu.yygh.hosp.controller;

import com.nsu.yygh.common.result.Result;
import com.nsu.yygh.hosp.service.HospitalService;
import com.nsu.yygh.model.hosp.Hospital;
import com.nsu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医院的接口
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    /**
     * 分页条件查询医院数据
     * @param curPage 当前页
     * @param size 每页数据大小
     * @param hospitalQueryVo 条件封装
     * @return
     */
    @PostMapping("/findHospitalPage/{curPage}/{size}")
    public Result findHospitalPageAndCondition(@PathVariable int curPage,
                                               @PathVariable int size,
                                               @RequestBody(required = false) HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> page = hospitalService.findHospitalPageAndCondition(curPage, size, hospitalQueryVo);

        return Result.ok(page);
    }

    /**
     * 更新医院的状态值
     * @param id 医院id
     * @param status 状态信息：0，未上线 1，已上线
     * @return
     */
    @GetMapping("/updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable("id") String id,
                                   @PathVariable("status") Integer status) {
        hospitalService.updateHospStatus(id, status);
        return Result.ok();
    }
}
