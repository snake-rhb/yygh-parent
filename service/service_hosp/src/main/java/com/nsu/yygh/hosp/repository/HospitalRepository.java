package com.nsu.yygh.hosp.repository;

import com.nsu.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 新建一个接口继承mongoRepository，实现hospital实体类与mongodb数据库的操作
 */

@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {
    // 根据hoscode查询医院是否存在
    // 在MongoRepository中，我们只需要按照一定的规范对方法进行命名，就可以自动帮我们实现方法
    Hospital getHospitalByHoscode(String hoscode);
}
