package com.example.cr.business.mapper;

import com.example.cr.business.entity.DailyTrainStation;
import com.example.cr.business.entity.DailyTrainStationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DailyTrainStationMapper {
    long countByExample(DailyTrainStationExample example);

    int deleteByExample(DailyTrainStationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DailyTrainStation row);

    int insertSelective(DailyTrainStation row);

    List<DailyTrainStation> selectByExample(DailyTrainStationExample example);

    DailyTrainStation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") DailyTrainStation row, @Param("example") DailyTrainStationExample example);

    int updateByExample(@Param("row") DailyTrainStation row, @Param("example") DailyTrainStationExample example);

    int updateByPrimaryKeySelective(DailyTrainStation row);

    int updateByPrimaryKey(DailyTrainStation row);
}