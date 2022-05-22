package com.tangym.tangram.mapper;

import com.tangym.tangram.entity.BizClasses;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BizClassesMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(BizClasses record);

    int insertSelective(BizClasses record);

    BizClasses selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BizClasses record);

    int updateByPrimaryKey(BizClasses record);

    @Select({"select clz_full_name from biz_classes where biz_id=#{bizId}"})
    List<String> selectByBizId(Integer bizId);

    @Select({"select * from biz_classes where component_id=#{id}"})
    BizClasses selectByComponentId(Integer id);
}
