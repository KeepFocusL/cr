package com.example.cr.business.mapper;

import com.example.cr.business.entity.TrainStation;
import com.example.cr.business.entity.TrainStationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TrainStationMapper {
    long countByExample(TrainStationExample example);

    int deleteByExample(TrainStationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TrainStation row);

    int insertSelective(TrainStation row);

    List<TrainStation> selectByExample(TrainStationExample example);

    TrainStation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") TrainStation row, @Param("example") TrainStationExample example);

    int updateByExample(@Param("row") TrainStation row, @Param("example") TrainStationExample example);

    int updateByPrimaryKeySelective(TrainStation row);

    int updateByPrimaryKey(TrainStation row);
}