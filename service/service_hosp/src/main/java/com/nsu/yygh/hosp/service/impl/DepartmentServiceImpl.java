package com.nsu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nsu.yygh.hosp.repository.DepartmentRepository;
import com.nsu.yygh.hosp.service.DepartmentService;
import com.nsu.yygh.model.hosp.Department;
import com.nsu.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    // 将科室信息保存到mongodb中
    @Override
    public void saveDepartment(Map<String, Object> resultMap) {
        // 转化成一个map字符串
        String mapStr = JSONObject.toJSONString(resultMap);
        Department department = JSONObject.parseObject(mapStr, Department.class);

        // 获取医院编号
        String hoscode = (String) resultMap.get("hoscode");
        // 获取科室编号
        String depcode = (String) resultMap.get("depcode");

        // 查询科室是否存在
        Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);

        if(departmentExist != null) {
            // 科室存在就进行更新
            department.setId(departmentExist.getId());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);

            departmentRepository.save(department);
        } else {
            // 不存在就直接保存
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);

            // departmentRepository.save(department);
            departmentRepository.insert(department);
        }
    }

    /**
     * 查找医院中所有的科室
     * @param map
     * @return
     */
    @Override
    public Page<Department> findDepartment(Map<String, Object> map) {
        // 取出请求参数中的当前页，和每页显示数量
        int curPage = StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String) map.get("page"));
        int limit = StringUtils.isEmpty(map.get("limit")) ? 1 : Integer.parseInt((String) map.get("limit"));

        // 获取医院编号
        String hoscode = (String) map.get("hoscode");

        // 封装一个条件查询对象
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        // 创建分页对象：当前页是从0开始
        Pageable pageable = PageRequest.of(curPage - 1, limit);

        // 创建条件查询的对象
        ExampleMatcher matcher = ExampleMatcher.matching()
                // 改变默认字符串匹配，模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                // 忽略大小写
                .withIgnoreCase(true);

        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);

        Example<Department> example = Example.of(department, matcher);

        return departmentRepository.findAll(example, pageable);
    }

    /**
     * 删除科室
     * @param map
     */
    @Override
    public void removeDept(Map<String, Object> map) {
        // 获取医院编号
        String hoscode = (String) map.get("hoscode");
        // 获取科室编号
        String depcode = (String) map.get("depcode");

        // 先进行查询，看科室是否存在
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department != null) {
            // 删除科室
            departmentRepository.deleteById(department.getId());
        }
    }
}
