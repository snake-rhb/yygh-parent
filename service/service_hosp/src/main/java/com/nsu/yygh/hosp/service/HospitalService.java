package com.nsu.yygh.hosp.service;

import com.nsu.yygh.model.hosp.Hospital;
import com.nsu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    // 将医院信息保存到mongodb中
    void save(Map<String, Object> resultMap);

    // 查询所有医院
    Hospital getHospital(Map<String, Object> map);

    // 查找分页数据，带查询条件
    Page<Hospital> findHospitalPageAndCondition(int curPage, int size, HospitalQueryVo hospitalQueryVo);

    // 更新医院的状态值
    void updateHospStatus(String id, Integer status);
}
