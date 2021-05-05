package com.nsu.yygh.cmn.insert;

import com.nsu.yygh.cmn.mapper.DictMapper;
import com.nsu.yygh.model.cmn.Dict;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InsertTest {

    @Autowired
    private DictMapper dictMapper;

    @Test
    public void addOne() {
        Dict dict = new Dict();
        dict.setId(1245L);
        dict.setName("test");

        System.out.println(dictMapper.insert(dict));
    }
}
