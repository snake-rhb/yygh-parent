package com.nsu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nsu.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * mybatis-plus是对mybatis的增强
 * 将Mapper继承BaseMapper，BaseMapper中封装了我们常用的增删改查
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
