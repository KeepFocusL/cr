package com.example.cr.business.mapper;

import com.example.cr.business.entity.TrainSeat;
import com.example.cr.business.entity.TrainSeatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TrainSeatMapper {
    long countByExample(TrainSeatExample example);

    int deleteByExample(TrainSeatExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TrainSeat row);

    int insertSelective(TrainSeat row);

    List<TrainSeat> selectByExample(TrainSeatExample example);

    TrainSeat selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") TrainSeat row, @Param("example") TrainSeatExample example);

    int updateByExample(@Param("row") TrainSeat row, @Param("example") TrainSeatExample example);

    int updateByPrimaryKeySelective(TrainSeat row);

    int updateByPrimaryKey(TrainSeat row);
}