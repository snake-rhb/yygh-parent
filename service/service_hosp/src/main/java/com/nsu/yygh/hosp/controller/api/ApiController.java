package com.nsu.yygh.hosp.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.nsu.yygh.common.exception.MyException;
import com.nsu.yygh.common.helper.HttpRequestHelper;
import com.nsu.yygh.common.result.Result;
import com.nsu.yygh.common.result.ResultCodeEnum;
import com.nsu.yygh.common.utils.MD5;
import com.nsu.yygh.hosp.service.DepartmentService;
import com.nsu.yygh.hosp.service.HospitalService;
import com.nsu.yygh.hosp.service.HospitalSetService;
import com.nsu.yygh.hosp.service.ScheduleService;
import com.nsu.yygh.model.hosp.Department;
import com.nsu.yygh.model.hosp.Hospital;
import com.nsu.yygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Map;

/**
 * api接口调用mongodb数据库：对医院进行增删改查操作
 */

@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    // mongodb的医院信息service
    @Autowired
    private HospitalService hospitalService;

    // 引入操作mysql的HospitalSetService
    @Autowired
    private HospitalSetService hospitalSetService;

    // mongodb的科室信息service
    @Autowired
    private DepartmentService departmentService;

    // mongodb的排版信息service
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        // 检查医院密钥，获取请求的参数
        Map<String, Object> map = judgeHospitalSign(request);

        scheduleService.removeSchedule(map);

        return Result.ok();
    }

    /**
     * 查询排班
     * @return
     */
    @PostMapping("/schedule/list")
    public Result findSchedule(HttpServletRequest request) {
        // 检查医院密钥，获取请求的参数
        Map<String, Object> map = judgeHospitalSign(request);

        Page<Schedule> page = scheduleService.findSchedule(map);

        return Result.ok(page);
    }

    /**
     * 添加排班接口
     * @param request
     * @return
     */
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        // 验证医院密钥，并获取封装的参数
        Map<String, Object> map = judgeHospitalSign(request);

        scheduleService.saveSchedule(map);

        return Result.ok();
    }

    /**
     * 删除科室接口
     * @param request
     * @return
     */
    @PostMapping("/department/remove")
    public Result removeDept(HttpServletRequest request) {
        // 验证医院密钥，并获取请求map
        Map<String, Object> map = judgeHospitalSign(request);

        departmentService.removeDept(map);

        return Result.ok();
    }

    /**
     * 查找科室
     * @param request
     * @return
     */
    @PostMapping("/department/list")
    public Result findDepartment(HttpServletRequest request) {
        // 根据工具方法，判断医院密钥是否正确，获得请求的参数
        Map<String, Object> map = this.judgeHospitalSign(request);

        // 调用service
        Page<Department> departmentList = departmentService.findDepartment(map);

        return Result.ok(departmentList);
    }

    /**
     * 将接口信息保存到mongodb数据库中
     * @param request
     * @return
     */
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        // 调用方法，验证医院签名是否相等
        Map<String, Object> resultMap = this.judgeHospitalSign(request);

        // 调用service
        departmentService.saveDepartment(resultMap);

        return Result.ok();
    }

    /**
     * 查询所有医院，暂时只能显示一个医院
     */
    @PostMapping("/hospital/show")
    public Result getHospitalAll() {
        Hospital hospital = hospitalService.getHospitalAll();
        return Result.ok(hospital);
    }

    /**
     * 上传一个医院
     * @param request
     * @return
     */
    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        // 调用抽取方法进行比较
        Map<String, Object> resultMap = this.judgeHospitalSign(request);

        // 在参数中有一个logoData医院头像图片，转化成了base64编码，传输中会把“+”转化为空格
        // 导致图片不能显示，所有要转化回来
        String logoData = (String) resultMap.get("logoData");
        if(!StringUtils.isEmpty(logoData)) {
            logoData = logoData.replaceAll(" ", "+");
            resultMap.put("logoData", logoData);
        }

        // 将Map传给service层
        hospitalService.save(resultMap);

        return Result.ok();
    }

    /**
     * 查询输入的医院密钥是否正确
     * @return
     */
    public Map<String, Object> judgeHospitalSign(HttpServletRequest request) {
        // 取出数据
        Map<String, String[]> requestMap = request.getParameterMap();
        // 使用工具类,将值为String[]转化为Object
        Map<String, Object> resultMap = HttpRequestHelper.switchMap(requestMap);

        // 获取传入的密钥
        String sign = (String) resultMap.get("sign");
        // 获得医院编号
        String hoscode = (String) resultMap.get("hoscode");

        // 根据医院的code查询mysql数据库中的密钥
        String signKey = hospitalSetService.getSignKeyByHoscode(hoscode);

        // 将医院的密钥也进行MD5加密，然后进行比对
        String encryptSignKey = MD5.encrypt(signKey);
        if(!sign.equals(encryptSignKey)) {
            // 抛出一个自定义异常：签名错误
            throw new MyException(ResultCodeEnum.SIGN_ERROR);
        }

        return resultMap;
    }
}
