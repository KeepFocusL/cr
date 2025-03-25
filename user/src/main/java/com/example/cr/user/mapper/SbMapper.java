package com.example.cr.user.mapper;

import com.example.cr.user.entity.Sb;
import com.example.cr.user.entity.SbExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SbMapper {
    long countByExample(SbExample example);

    int deleteByExample(SbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Sb row);

    int insertSelective(Sb row);

    List<Sb> selectByExample(SbExample example);

    Sb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Sb row, @Param("example") SbExample example);

    int updateByExample(@Param("row") Sb row, @Param("example") SbExample example);

    int updateByPrimaryKeySelective(Sb row);

    int updateByPrimaryKey(Sb row);
}