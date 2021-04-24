package com.nsu.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsu.yygh.hosp.mapper.HospitalSetMapper;
import com.nsu.yygh.hosp.service.HospitalSetService;
import com.nsu.yygh.model.hosp.HospitalSet;
import com.nsu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
}
