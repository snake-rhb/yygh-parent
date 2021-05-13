package com.nsu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nsu.yygh.hosp.repository.HospitalRepository;
import com.nsu.yygh.hosp.service.HospitalService;
import com.nsu.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    // 注入一个Hospital实体类操作mongodb数据库的Repository
    @Autowired
    private HospitalRepository hospitalRepository;

    /**
     * 将医院信息保存到mongodb中
     * @param resultMap
     */
    @Override
    public void save(Map<String, Object> resultMap) {
        // 使用fastJson将map集合转化为Hospital对象
        // 先将map转化为字符串
        String mapString = JSONObject.toJSONString(resultMap);
        // 再将字符串转化为对象
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        // 判断数据是否存在
        String hoscode = hospital.getHoscode();
        Hospital resHospital = hospitalRepository.getHospitalByHoscode(hoscode);

        if(resHospital == null) {
            // 说明对象不存在就保存
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
            // 说明存在，就进行修改
            hospital.setId(resHospital.getId());
            hospital.setStatus(resHospital.getStatus());
            hospital.setCreateTime(resHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    /**
     * 查询所有医院
     * @return
     */
    @Override
    public Hospital getHospitalAll() {
        return hospitalRepository.findAll().get(0);
    }
}
