package com.nsu.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nsu.yygh.common.exception.MyException;
import com.nsu.yygh.common.result.Result;
import com.nsu.yygh.common.utils.MD5;
import com.nsu.yygh.hosp.service.HospitalSetService;
import com.nsu.yygh.model.hosp.HospitalSet;
import com.nsu.yygh.vo.hosp.HospitalQueryVo;
import com.nsu.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院管理控制")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
// 允许跨域
@CrossOrigin
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

    /**
     * 查询条件的分页
     * @param currentPage 当前页
     * @param size 每页的数量
     * @param hospitalSetQueryVo 条件查询的类：医院的名称和id
     * @return
     */
    @ApiOperation("分页条件查询")
    @PostMapping("/findConditionPage/{currentPage}/{size}")
    public Result findConditionPage(@PathVariable("currentPage") Long currentPage,
                                    @PathVariable("size") Integer size,
                                    @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        // 构建一个条件查询对象
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        // 使用Spring中的StringUtils工具类判断是否为空
        if(!StringUtils.isEmpty(hosname)) {
            // 加入查询条件
            wrapper.like("hosname", hosname);
        }

        if(!StringUtils.isEmpty(hoscode)) {
            wrapper.like("hoscode", hoscode);
        }
        // 构建一个分页查询对象
        Page<HospitalSet> page = new Page<>(currentPage, size);

        // 进行分页条件查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);
        return Result.ok(pageHospitalSet);
    }

    /**
     * 根据关键字进行分页查询
     * @param currentPage
     * @param size
     * @param keyword
     * @return
     */
    @ApiOperation("根据关键字进行分页查询")
    @GetMapping("/findConditionPage/{currentPage}/{size}/{keyword}")
    public Result findConditionPageByKeyword(@PathVariable("currentPage") Integer currentPage,
                                             @PathVariable("size") Integer size,
                                             @PathVariable(value = "keyword", required = false) String keyword) {
        // 构建一个分页对象
        Page<HospitalSet> page = new Page<>(currentPage, size);
        // 构建一个查询对象
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.like("hosname", keyword);
        wrapper.like("hoscode", keyword);

        // 进行查询分页
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);

        return Result.ok(hospitalSetPage);
    }

    /**
     * 添加一个医院
     * @param hospitalSet
     * @return
     */
    @ApiOperation("添加医院")
    @PostMapping("/addHospital")
    public Result addHospital(@RequestBody HospitalSet hospitalSet) {
        // 设置医院的状态: 1使用，0不能使用
        hospitalSet.setStatus(1);
        // 设置医院的密钥
        Random random = new Random();
        // 使用自定义md5生成一个密钥
        String encrypt = MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000));
        hospitalSet.setSignKey(encrypt);

        // 添加一个医院
        boolean save = hospitalSetService.save(hospitalSet);
        return Result.flag(save);
    }

    /**
     * 根据id查询一个医院
     * @param id
     * @return
     */
    @ApiOperation("根据id查询医院")
    @GetMapping("/findHospitalById/{id}")
    public Result<HospitalSet> getHospitalSetById(@PathVariable("id") Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    /**
     * 修改一个医院
     * @param hospitalSet
     * @return
     */
    @ApiOperation("修改一个医院")
    @PutMapping("/updateHospital")
    public Result updateHospital(@RequestBody HospitalSet hospitalSet) {
        // 修改，会先根据id进行查询
        boolean b = hospitalSetService.updateById(hospitalSet);
        return Result.flag(b);
    }

    /**
     * 批量删除医院
     * @param idList
     * @return
     */
    @ApiOperation("批量删除医院")
    @DeleteMapping("/batchDelete")
    public Result batchDelete(@RequestBody List<Long> idList) {
        boolean b = hospitalSetService.removeByIds(idList);
        return Result.flag(b);
    }

    /**
     * 修改医院的状态信息：解锁和锁定状态
     * @param id
     * @param status
     * @return
     */
    @ApiOperation("设置医院的状态：1是解锁，0是锁定")
    @PutMapping("/lockHospital/{id}/{status}")
    public Result lockHospital(@PathVariable("id") Long id,
                               @PathVariable("status") Integer status) {
        // 先根据id查出医院信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        // 设置状态信息：1是解锁，0是锁定
        hospitalSet.setStatus(status);
        // 对状态进行修改
        boolean b = hospitalSetService.updateById(hospitalSet);
        return Result.flag(b);
    }

    /**
     * 给医院接口发送短信签名密钥
     * @param id
     * @return
     */
    @ApiOperation("给医院接口发送短信签名密钥")
    @PutMapping("/sendKey/{id}")
    public Result sendKey(@PathVariable("id") Long id) {
        // 查询到医院
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        // 取出密钥和编号名称
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        String hosname = hospitalSet.getHosname();

        // 发送短信

        return Result.ok();
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
