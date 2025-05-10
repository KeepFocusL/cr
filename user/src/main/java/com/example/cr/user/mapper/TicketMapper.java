package com.example.cr.user.mapper;

import com.example.cr.user.entity.Ticket;
import com.example.cr.user.entity.TicketExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TicketMapper {
    long countByExample(TicketExample example);

    int deleteByExample(TicketExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Ticket row);

    int insertSelective(Ticket row);

    List<Ticket> selectByExample(TicketExample example);

    Ticket selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Ticket row, @Param("example") TicketExample example);

    int updateByExample(@Param("row") Ticket row, @Param("example") TicketExample example);

    int updateByPrimaryKeySelective(Ticket row);

    int updateByPrimaryKey(Ticket row);
}