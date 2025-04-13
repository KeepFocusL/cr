package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.entity.DailyTrainTicket;
import com.example.cr.business.entity.DailyTrainTicketExample;
import com.example.cr.business.mapper.DailyTrainTicketMapper;
import com.example.cr.business.request.DailyTrainTicketListRequest;
import com.example.cr.business.response.DailyTrainTicketResponse;
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
import com.example.cr.business.request.DailyTrainTicketRequest;

@Service
public class DailyTrainTicketService {
    private static final Logger log = LoggerFactory.getLogger(DailyTrainTicketService.class);
    @Autowired
    DailyTrainTicketMapper dailyTrainTicketMapper;

    public PageResponse<DailyTrainTicketResponse> list(DailyTrainTicketListRequest request) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.setOrderByClause("id desc");

        // 处理搜索条件
        DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andTrainCodeLike(keyword);
            DailyTrainTicketExample.Criteria criteria2 = dailyTrainTicketExample.createCriteria();
            criteria2.andStartLike(keyword);
            dailyTrainTicketExample.or(criteria2);
            DailyTrainTicketExample.Criteria criteria3 = dailyTrainTicketExample.createCriteria();
            criteria3.andEndLike(keyword);
            dailyTrainTicketExample.or(criteria3);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<DailyTrainTicket> dailyTrainTickets = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);

        PageInfo<DailyTrainTicket> pageInfo = new PageInfo<>(dailyTrainTickets);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<DailyTrainTicketResponse> list = BeanUtil.copyToList(dailyTrainTickets, DailyTrainTicketResponse.class);

        PageResponse<DailyTrainTicketResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(DailyTrainTicketRequest request) {
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(request, DailyTrainTicket.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(dailyTrainTicket.getId()) || dailyTrainTicket.getId() == 0L) {
            dailyTrainTicket.setId(SnowflakeUtil.getId());
            dailyTrainTicket.setCreatedAt(now);
            dailyTrainTicket.setUpdatedAt(now);
            dailyTrainTicketMapper.insert(dailyTrainTicket);
        } else {
            dailyTrainTicket.setUpdatedAt(now);
            dailyTrainTicketMapper.updateByPrimaryKeySelective(dailyTrainTicket);
        }
    }

    public int deleteBatch(List<Long> ids) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();
        criteria.andIdIn(ids);
        return dailyTrainTicketMapper.deleteByExample(dailyTrainTicketExample);
    }
}
