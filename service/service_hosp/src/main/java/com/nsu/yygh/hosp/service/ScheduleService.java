package com.nsu.yygh.hosp.service;

import com.nsu.yygh.model.hosp.Schedule;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ScheduleService {
    // 保存一个排班信息
    void saveSchedule(Map<String, Object> map);

    Page<Schedule> findSchedule(Map<String, Object> map);

    void removeSchedule(Map<String, Object> map);
}
