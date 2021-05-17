package com.nsu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nsu.yygh.cmn.client.DictFeignClient;
import com.nsu.yygh.hosp.repository.HospitalRepository;
import com.nsu.yygh.hosp.service.HospitalService;
import com.nsu.yygh.model.hosp.Hospital;
import com.nsu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    // 注入一个Hospital实体类操作mongodb数据库的Repository
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

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
     * 根据hoscode查询医院
     * @return
     */
    @Override
    public Hospital getHospital(Map<String, Object> map) {
        String hoscode = (String) map.get("hoscode");
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }

    // 查找分页数据，带查询条件
    @Override
    public Page<Hospital> findHospitalPageAndCondition(int curPage, int size, HospitalQueryVo hospitalQueryVo) {
        // 创建一个分页对象
        Pageable pageable = PageRequest.of(curPage - 1, size);

        // 构造一个条件对象
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

        // 构造条件查询
        Example<Hospital> example = Example.of(hospital, exampleMatcher);

        Page<Hospital> page = hospitalRepository.findAll(example, pageable);

        // 遍历每个医院：使用java8中流的方式遍历
        page.getContent().stream().forEach(hosp -> {
            // 获取医院类型的value
            String hostype = hosp.getHostype();
            // 查询数据字典，获取医院类型的等级
            String type = dictFeignClient.getHospitalGrade("Hostype", hostype);

            // 设置到Hospital对象的Map中
            hosp.getParam().put("hosType", type);

            // 查询医院所在地区
            // 省
            String provinceCode = hosp.getProvinceCode();
            // 市
            String cityCode = hosp.getCityCode();
            // 地区
            String districtCode = hosp.getDistrictCode();
            String province = dictFeignClient.getLocation(provinceCode);
            String city = dictFeignClient.getLocation(cityCode);
            String district = dictFeignClient.getLocation(districtCode);
            hosp.getParam().put("location", province + city + district);
        });

        return page;
    }

    /**
     * 更新医院的状态值
     * @param id
     * @param status
     */
    @Override
    public void updateHospStatus(String id, Integer status) {
        // 根据id查找出医院
        Hospital hospital = hospitalRepository.findById(id).get();
        // 设置上线状态和更新时间
        hospital.setUpdateTime(new Date());
        hospital.setStatus(status);

        hospitalRepository.save(hospital);
    }
}
