package com.example.cr.business.mapper;

import com.example.cr.business.entity.DailyTrain;
import com.example.cr.business.entity.DailyTrainExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DailyTrainMapper {
    long countByExample(DailyTrainExample example);

    int deleteByExample(DailyTrainExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DailyTrain row);

    int insertSelective(DailyTrain row);

    List<DailyTrain> selectByExample(DailyTrainExample example);

    DailyTrain selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") DailyTrain row, @Param("example") DailyTrainExample example);

    int updateByExample(@Param("row") DailyTrain row, @Param("example") DailyTrainExample example);

    int updateByPrimaryKeySelective(DailyTrain row);

    int updateByPrimaryKey(DailyTrain row);
}