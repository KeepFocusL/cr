package com.example.cr.business.mapper;

import com.example.cr.business.entity.DailyTrainCarriage;
import com.example.cr.business.entity.DailyTrainCarriageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DailyTrainCarriageMapper {
    long countByExample(DailyTrainCarriageExample example);

    int deleteByExample(DailyTrainCarriageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DailyTrainCarriage row);

    int insertSelective(DailyTrainCarriage row);

    List<DailyTrainCarriage> selectByExample(DailyTrainCarriageExample example);

    DailyTrainCarriage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") DailyTrainCarriage row, @Param("example") DailyTrainCarriageExample example);

    int updateByExample(@Param("row") DailyTrainCarriage row, @Param("example") DailyTrainCarriageExample example);

    int updateByPrimaryKeySelective(DailyTrainCarriage row);

    int updateByPrimaryKey(DailyTrainCarriage row);
}