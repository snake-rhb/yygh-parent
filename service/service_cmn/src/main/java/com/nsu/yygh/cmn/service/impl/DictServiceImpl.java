package com.nsu.yygh.cmn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsu.yygh.cmn.mapper.DictMapper;
import com.nsu.yygh.cmn.service.DictService;
import com.nsu.yygh.model.cmn.Dict;
import org.springframework.stereotype.Service;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
}
