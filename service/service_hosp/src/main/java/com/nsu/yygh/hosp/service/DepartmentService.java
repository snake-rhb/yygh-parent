package com.nsu.yygh.hosp.service;

import com.nsu.yygh.model.hosp.Department;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface DepartmentService {
    // 将科室信息保存到mongodb中
    void saveDepartment(Map<String, Object> resultMap);

    // 查找医院中所有的科室
    Page<Department> findDepartment(Map<String, Object> map);

    // 删除科室
    void removeDept(Map<String, Object> map);
}
