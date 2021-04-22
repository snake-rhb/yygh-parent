package com.nsu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nsu.entity.User;
import com.nsu.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MybatisPlusTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void findAllTest() {
        // 查询所有数据
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void insertTest() {
        User user = new User();
        user.setAge(15);
        user.setName("jack");
        user.setEmail("22@qq.com");

        // 插入一条数据
        int insert = userMapper.insert(user);
        System.out.println(insert);
    }

    @Test
    public void updateTest() {
        User user = new User();
        user.setId(6L);
        user.setName("cat");
        // 根据id修改一条数据
        System.out.println(userMapper.updateById(user));
    }

    // 乐观锁测试
    @Test
    public void testOptimisticLocker() {
        // 先根据id查询user
        User user = userMapper.selectById(8L);
        // 在对user进行更新
        user.setName("令狐冲");

        System.out.println(userMapper.updateById(user));
    }

    // 多个id进行查询测试
    @Test
    public void ManyIdSelectTest() {
        // 多个需要查询的id
        List<Long> idList = new ArrayList<>();
        idList.add(1L);
        idList.add(2L);
        idList.add(3L);
        List<User> users = userMapper.selectBatchIds(idList);
        System.out.println(users);
    }

    // 简单条件查询
    @Test
    public void MapSelectTest() {
        // 将需要查询的条件封装在Map中
        Map<String, Object> map = new HashMap<>();
        map.put("age", 18);
        map.put("email", "test1");
        List<User> users = userMapper.selectByMap(map);
        System.out.println(users);
    }

    // 分页查询
    @Test
    public void pageTest() {
        // 传入一个分页对象：当前第一页，每页3条数据
        Page<User> page = new Page<>(1,3);

        // 查询所有数据进行分页
        Page<User> userPage = userMapper.selectPage(page, null);

        System.out.println("当前页：" + userPage.getCurrent());
        System.out.println("总的数据集合：" + userPage.getRecords());
        System.out.println("每页大小：" + userPage.getSize());
        System.out.println("总记录数：" + userPage.getTotal());
        System.out.println("总页数：" + userPage.getPages());
        System.out.println("下一页：" + userPage.hasNext());
        System.out.println("上一页：" + userPage.hasPrevious());
    }

    // 分页查询2
    @Test
    public void selectWherePage() {
        // 新建分页对象
        Page<Map<String, Object>> page = new Page<>(1, 5);
        Page<Map<String, Object>> mapsPage = userMapper.selectMapsPage(page, null);

        System.out.println("当前页：" + mapsPage.getCurrent());
        System.out.println("总的数据集合：" + mapsPage.getRecords());
        System.out.println("每页大小：" + mapsPage.getSize());
        System.out.println("总记录数：" + mapsPage.getTotal());
        System.out.println("总页数：" + mapsPage.getPages());
        System.out.println("下一页：" + mapsPage.hasNext());
        System.out.println("上一页：" + mapsPage.hasPrevious());
    }

    // 根据id删除记录
    @Test
    public void deleteTest() {
        System.out.println(userMapper.deleteById(8));

        // 批量删除
        userMapper.deleteBatchIds(Arrays.asList(1,2,3));

        // 简单条件删除
        // userMapper.deleteByMap()
    }

    // 进行复杂的查询
    @Test
    public void fzSelectTest() {
        // 使用QueryWrapper封装查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // age大于等于18的
        // queryWrapper.ge("age", 18);

        // 模糊查询like
        queryWrapper.like("name", "j").like("email", "test");

        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);
    }
}
