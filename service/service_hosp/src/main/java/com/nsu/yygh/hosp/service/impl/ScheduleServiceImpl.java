package com.nsu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nsu.yygh.hosp.repository.ScheduleRepository;
import com.nsu.yygh.hosp.service.ScheduleService;
import com.nsu.yygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    /**
     * 保存一个排班信息
     * @param map
     */
    @Override
    public void saveSchedule(Map<String, Object> map) {
        // 使用JSON将map转化为String
        String mapStr = JSONObject.toJSONString(map);
        // 将String转化为对象
        Schedule schedule = JSONObject.parseObject(mapStr, Schedule.class);

        // 根据排班编号查询，该排班是否存在
        String hoscode = schedule.getHoscode();
        String hosScheduleId = schedule.getHosScheduleId();

        Schedule scheduleExist = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);

        // 设置基础属性
        schedule.setUpdateTime(new Date());
        schedule.setIsDeleted(0);
        schedule.setStatus(1);
        // 判断排班是否存在
        if(scheduleExist == null) {
            // 排班不存在就添加
            schedule.setCreateTime(new Date());
        } else {
            schedule.setId(scheduleExist.getId());
            schedule.setCreateTime(scheduleExist.getCreateTime());
        }

        scheduleRepository.save(schedule);
    }

    /**
     * 查找排班
     * @param map
     * @return
     */
    @Override
    public Page<Schedule> findSchedule(Map<String, Object> map) {
        // 获取当前页和每页显示数量
        int curPage = StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String) map.get("page"));
        int limit = StringUtils.isEmpty(map.get("limit")) ? 5 : Integer.parseInt((String) map.get("limit"));

        // 构造一个分页对象：
        Pageable pageable = PageRequest.of(curPage - 1, limit);

        // 构造一个查询条件对象
        ExampleMatcher matching = ExampleMatcher.matching()
                // 字符串模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                // 忽略大小写
                .withIgnoreCase(true);

        Schedule schedule = new Schedule();
        // 获取条件参数
        String hoscode = (String) map.get("hoscode");
        schedule.setHoscode(hoscode);
        schedule.setStatus(1);
        schedule.setIsDeleted(0);

        Example<Schedule> example = Example.of(schedule, matching);
        // 分页条件查询
        Page<Schedule> page = scheduleRepository.findAll(example, pageable);
        return page;
    }

    @Override
    public void removeSchedule(Map<String, Object> map) {
        // 根据排班编号查询，该排班是否存在
        String hoscode = (String) map.get("hoscode");
        String hosScheduleId = (String) map.get("hosScheduleId");

        Schedule scheduleExist = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);

        if(scheduleExist != null) {
            scheduleRepository.deleteById(scheduleExist.getId());
        }
    }
}
