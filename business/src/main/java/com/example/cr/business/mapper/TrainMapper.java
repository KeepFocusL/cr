package com.example.cr.business.mapper;

import com.example.cr.business.entity.Train;
import com.example.cr.business.entity.TrainExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TrainMapper {
    long countByExample(TrainExample example);

    int deleteByExample(TrainExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Train row);

    int insertSelective(Train row);

    List<Train> selectByExample(TrainExample example);

    Train selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Train row, @Param("example") TrainExample example);

    int updateByExample(@Param("row") Train row, @Param("example") TrainExample example);

    int updateByPrimaryKeySelective(Train row);

    int updateByPrimaryKey(Train row);
}