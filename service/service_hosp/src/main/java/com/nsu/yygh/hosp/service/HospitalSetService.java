package com.nsu.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nsu.yygh.model.hosp.HospitalSet;

public interface HospitalSetService extends IService<HospitalSet> {
    // 根据医院code获得医院的密钥
    String getSignKeyByHoscode(String hoscode);
}
