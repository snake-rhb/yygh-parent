package com.nsu.hospital.service;

import com.alibaba.fastjson.JSONObject;
import com.nsu.hospital.model.HospitalSet;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public interface ApiService {

    String getHoscode();

    String getSignKey();

    JSONObject getHospital(HospitalSet hospitalSet);

    boolean saveHospital(String data, HttpSession session);

    Map<String, Object> findDepartment(int pageNum, int pageSize, HttpSession session);

    boolean saveDepartment(String data, HttpSession session);

    boolean removeDepartment(String depcode, HttpSession session);

    Map<String, Object> findSchedule(int pageNum, int pageSize, HttpSession session);

    boolean saveSchedule(String data, HttpSession session);

    boolean removeSchedule(String hosScheduleId, HttpSession session);

    void  saveBatchHospital() throws IOException;
}
