package com.nsu.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 可以自动填充实体类中的createTime和updateTime
 * 实现MetaObjectHandler
 */

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    // 在插入时自动更新createTime字段
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        // 乐观锁的版本号，默认插入数据为1
        this.setFieldValByName("version", 1, metaObject);
    }

    // 在修改时自动更新updateTime字段
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
