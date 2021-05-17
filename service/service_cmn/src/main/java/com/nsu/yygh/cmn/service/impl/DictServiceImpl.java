package com.nsu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nsu.yygh.cmn.listener.ExcelListener;
import com.nsu.yygh.cmn.mapper.DictMapper;
import com.nsu.yygh.cmn.service.DictService;
import com.nsu.yygh.model.cmn.Dict;
import com.nsu.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    /**
     * 查询数据字典中对应id的子节点
     * @param id
     * @return
     */
    @Override
    // 加入缓存注解，会将查询出来的数据存到redis缓存中
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildrenData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 将数据字典写入到excel中
     * @param response 会导出一个excel文件，需要用到response对象
     * @return
     */
    @Override
    public void exportDictToExcel(HttpServletResponse response) {
        try {
            // 设置下载的是一个excel文件
            response.setContentType("application/vnd.ms-excel");
            // 设置响应的字符集
            response.setCharacterEncoding("UTF-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode("数据字典", "UTF-8");
            // 设置响应头下载文件的名称
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");

            // 从数据库查询出数据字典的信息
            List<Dict> dicts = baseMapper.selectList(null);
            // 要将数据库中的字典信息，转化为excel封装的实体类
            List<DictEeVo> dictEeVoList = new ArrayList<>();
            for (Dict dict : dicts) {
                DictEeVo dictEeVo = new DictEeVo();
                // 将属性进行赋值
                BeanUtils.copyProperties(dict, dictEeVo);
                dictEeVoList.add(dictEeVo);
            }

            // 使用easyexcel将数据写入文件
            EasyExcel.write(response.getOutputStream(), DictEeVo.class)
                    .sheet("数据字典")
                    .doWrite(dictEeVoList);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传一个excel文件，将这个文件中的数据插入到数据字典中
     * @param file
     */
    @Override
    // 新增数据就清空redis缓存中的内容
    @CacheEvict(value = "dict", allEntries=true)
    public void addDictData(MultipartFile file) {
        try {
            // 对excel进行读取，并在监听器中将数据写入数据库
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new ExcelListener(baseMapper))
                .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 根据字段的类型和值value查询字典接口
    @Override
    public String getDictName(String code, String value) {
        Dict dict = null;
        // 判断code是否为空,为空就是查询省份或其他字典值的，不为空就是查询医院等级
        if(StringUtils.isEmpty(code)) {
            // 查询省份
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            dict = baseMapper.selectOne(wrapper);
        } else {
            // 如果不为空就是查询医院等级的
            // 先根据类型code查询字典类型
            Dict parentDict = selectParentDict(code);
            // 在取出父节点的id
            Long parentId = parentDict.getId();
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", parentId);
            wrapper.eq("value", value);

            dict = baseMapper.selectOne(wrapper);
        }

        return dict.getName();
    }

    // 根据dict_id查询数据字典中的子节点
    @Override
    public List<Dict> getListByDictId(String dictCode) {
        // 查询父节点的id
        Dict dict = selectParentDict(dictCode);
        // 根据父节点的id，查询所有的子节点
        List<Dict> dictList = findChildrenData(dict.getId());
        return dictList;
    }

    // 根据类型code查询字典类型
    private Dict selectParentDict(String code) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", code);
        return baseMapper.selectOne(wrapper);
    }
}
