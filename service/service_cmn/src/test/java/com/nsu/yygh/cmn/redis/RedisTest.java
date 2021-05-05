package com.nsu.yygh.cmn.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Test
    public void redisTest() {
        ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
        operations.set("test", "hello world");
    }
}
