package com.example.cr.user.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.common.request.UserTicketRequest;
import com.example.cr.common.response.PageResponse;
import com.example.cr.user.entity.Ticket;
import com.example.cr.user.entity.TicketExample;
import com.example.cr.user.mapper.TicketMapper;
import com.example.cr.user.request.TicketListRequest;
import com.example.cr.user.response.TicketResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.cr.common.util.SnowflakeUtil;
import com.example.cr.user.request.TicketRequest;

@Service
public class TicketService {
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);
    @Autowired
    TicketMapper ticketMapper;

    public PageResponse<TicketResponse> list(TicketListRequest request) {
        TicketExample ticketExample = new TicketExample();
        ticketExample.setOrderByClause("id desc");

        // 处理搜索条件
        TicketExample.Criteria criteria = ticketExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andPassengerNameLike(keyword);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<Ticket> tickets = ticketMapper.selectByExample(ticketExample);

        PageInfo<Ticket> pageInfo = new PageInfo<>(tickets);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<TicketResponse> list = BeanUtil.copyToList(tickets, TicketResponse.class);

        PageResponse<TicketResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(UserTicketRequest request) {
        Ticket ticket = BeanUtil.copyProperties(request, Ticket.class);
        DateTime now = DateTime.now();
        ticket.setId(SnowflakeUtil.getId());
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);
        ticketMapper.insert(ticket);
    }

    public int deleteBatch(List<Long> ids) {
        TicketExample ticketExample = new TicketExample();
        TicketExample.Criteria criteria = ticketExample.createCriteria();
        criteria.andIdIn(ids);
        return ticketMapper.deleteByExample(ticketExample);
    }
}
