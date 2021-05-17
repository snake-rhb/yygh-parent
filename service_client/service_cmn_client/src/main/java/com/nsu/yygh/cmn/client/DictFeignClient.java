package com.nsu.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 创建一个远程调用dict数据字典的接口
 */

// 远程调用的服务
@FeignClient("service-cmn")
// 加入到容器中
@Repository
public interface DictFeignClient {
    /**
     * 根据医院的类型hospCode和值value查询医院等级接口
     * @param hospCode 医院类型，Hospital
     * @param value 医院的值
     * @return
     */
    @GetMapping("/admin/cmn/dict/getHospitalGrade/{hospCode}/{value}")
    String getHospitalGrade(@PathVariable("hospCode") String hospCode,
                                   @PathVariable("value") String value);

    /**
     * 根据value值查询所在地区
     * @param value
     * @return
     */
    @GetMapping("/admin/cmn/dict/getLocation/{value}")
    String getLocation(@PathVariable("value") String value);
}
