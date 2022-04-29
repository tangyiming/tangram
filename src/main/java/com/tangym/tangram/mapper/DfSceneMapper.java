package com.tangym.tangram.mapper;

import com.tangym.tangram.dto.CmpSceneQuery;
import com.tangym.tangram.entity.DfScene;

import java.util.List;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 */
public interface DfSceneMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(DfScene record);

    int insertSelective(DfScene record);

    DfScene selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DfScene record);

    int updateByPrimaryKey(DfScene record);

    List<DfScene> queryBy(CmpSceneQuery cmpSceneQuery);
}
