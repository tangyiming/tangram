package com.tangym.tangram.mapper;

import com.tangym.tangram.dto.CmpSceneQuery;
import com.tangym.tangram.entity.DfComponent;

import java.util.List;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 */

public interface DfComponentMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(DfComponent record);

    int insertSelective(DfComponent record);

    DfComponent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DfComponent record);

    int updateByPrimaryKey(DfComponent record);

    List<DfComponent> queryBy(CmpSceneQuery cmpQuery);


}
