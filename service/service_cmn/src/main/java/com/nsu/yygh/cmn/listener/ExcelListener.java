package com.nsu.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nsu.yygh.cmn.mapper.DictMapper;
import com.nsu.yygh.model.cmn.Dict;
import com.nsu.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 读取excel的监听器
 */

public class ExcelListener extends AnalysisEventListener<DictEeVo> {
    private BaseMapper<Dict> baseMapper;

    // 查询出所有数据
    private List<Dict> list;

    public ExcelListener(BaseMapper<Dict> baseMapper) {
        this.baseMapper = baseMapper;
        list = baseMapper.selectList(null);
    }

    /**
     * 读取excel表格中的值
     * @param dictEeVo
     * @param analysisContext
     */
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        // 将读取出来的对象赋值给Dict对象
        BeanUtils.copyProperties(dictEeVo, dict);

        // 判断数据库中是否已存在该值，重写了Dict的equals方法，根据id进行比较
        boolean contains = list.contains(dict);
        if(!contains) {
            // 如果不包含就插入到数据库中
            baseMapper.insert(dict);

            // 拿到parent_id，更新父节点的has_children值
            Long parentId = dict.getParentId();
            // 判断是否有父节点
            if(parentId != null) {
                // 新建一个父节点的dict对象
                Dict d = new Dict();
                d.setId(parentId);
                d.setHasChildren(1);
                // 更新父节点的值
                baseMapper.updateById(d);
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
