package com.nsu.mongodb;

import com.mongodb.client.result.UpdateResult;
import com.nsu.mongodb.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
public class MongodbTest {
    // 注入MongoTemplate
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 向集合中添加一条数据
     */
    @Test
    public void insert() {
        User user = new User();
        user.setName("guanyu");
        user.setAge(15);
        user.setEmail("21345@qq.com");

        User insert = mongoTemplate.insert(user);
        System.out.println(insert);
    }

    // 查询User表中的所有记录
    @Test
    public void select() {
        List<User> userList = mongoTemplate.findAll(User.class);
        System.out.println(userList);
    }

    @Test
    public void selectById() {
        // id查询
        // User user = mongoTemplate.findById("6092270b08aa6c19a73c7dc4", User.class);
        // System.out.println(user);

        // 条件查询 name = guanyu and age = 15
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("guanyu").and("age").is(15));
        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println(users);
    }

    // 模糊查询
    @Test
    public void selectLike() {
        String name = "guan";
        // 拼接正则：查询所有名字带guan的
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        // regex匹配模糊查询的正则
        Query query = new Query(Criteria.where("name").regex(pattern));
        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println(users);
    }

    // 分页查询
    @Test
    public void findPage() {
        // 当前页数
        int currentPage = 1;
        // 每页显示条数
        int pageSize = 3;

        // 构建查询条件
        String name = "guan";
        // 拼接正则：查询所有名字带guan的
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        // regex匹配模糊查询的正则
        Query query = new Query(Criteria.where("name").regex(pattern));

        // 总记录数
        long count = mongoTemplate.count(query, User.class);
        System.out.println(count);
        // skip跳过多少条记录
        List<User> users = mongoTemplate.find(query.skip((currentPage - 1) * pageSize).limit(currentPage * pageSize), User.class);
        System.out.println(users);
    }

    // 修改值
    @Test
    public void update() {
        // 根据id值查询需要修改的user
//        User user = mongoTemplate.findById("609227880f290e690a4d05c3", User.class);
//        // 设置修改值
//        user.setName("张飞");
//        user.setAge(50);
//        user.setEmail("000@qq.com");

        Query query = new Query(Criteria.where("_id").is("609227880f290e690a4d05"));
        // 要修改的值
        Update update = new Update();
        update.set("name", "张飞");
        update.set("age", 50);
        update.set("email", "000@qq.com");

        UpdateResult upsert = mongoTemplate.upsert(query, update, User.class);
        // 受影响的行数
        long matchedCount = upsert.getMatchedCount();
        System.out.println(matchedCount);
    }
}
