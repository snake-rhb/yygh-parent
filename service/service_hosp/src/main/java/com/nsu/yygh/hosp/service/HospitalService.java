package com.nsu.yygh.hosp.service;

import com.nsu.yygh.model.hosp.Hospital;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    // 将医院信息保存到mongodb中
    void save(Map<String, Object> resultMap);

    // 查询所有医院
    Hospital getHospitalAll();
}
