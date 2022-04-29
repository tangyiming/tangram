package com.tangym.tangram.mapper;

import com.tangym.tangram.entity.DfBizLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 */

@Mapper
public interface DfBizLineMapper {
    int insert(DfBizLine record);

    int insertSelective(DfBizLine record);

    int updateByPrimaryKeySelective(DfBizLine record);

    int updateByPrimaryKey(DfBizLine record);

    int deleteByPrimaryKey(Integer id);

    DfBizLine selectByPrimaryKey(Integer id);

    @Select({"select * from df_biz_line"})
    List<DfBizLine> selectAll();
}
