package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.example.cr.business.entity.TrainStation;
import com.example.cr.business.enums.SeatType;
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

import java.math.BigDecimal;
import java.util.Date;
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

    @Autowired
    TrainStationService trainStationService;

    @Autowired
    DailyTrainSeatService dailyTrainSeatService;

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

    public void genDaily(Date date, String trainCode) {
        log.debug("生成日期【{}】车次【{}】的【余票信息】数据 - 开始", DateUtil.formatDate(date), trainCode);
        // 删除已有的相关数据
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainTicketMapper.deleteByExample(dailyTrainTicketExample);
        log.debug("先删除日期【{}】车次【{}】的已有【余票信息】数据", DateUtil.formatDate(date), trainCode);

        // 查询途径的车站信息
        List<TrainStation> trainStations = trainStationService.selectByTrainCode(trainCode);
        log.debug("车次【{}】的车站总数={}", trainCode, trainStations.size());

        for (int i = 0; i < trainStations.size(); i++) {
            // 第一层循环 得到出发站
            TrainStation trainStationStart = trainStations.get(i);
            for (int j = (i + 1); j < trainStations.size(); j++) {
                // 第二层循环 得到到达站
                TrainStation trainStationEnd = trainStations.get(j);

                // 构造余票信息数据
                DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
                dailyTrainTicket.setId(SnowflakeUtil.getId());
                dailyTrainTicket.setDate(date);
                dailyTrainTicket.setTrainCode(trainCode);
                dailyTrainTicket.setStart(trainStationStart.getName());
                dailyTrainTicket.setStartPinyin(trainStationStart.getNamePinyin());
                dailyTrainTicket.setStartTime(trainStationStart.getOutTime());
                dailyTrainTicket.setStartIndex(trainStationStart.getIndex());


                dailyTrainTicket.setEnd(trainStationEnd.getName());
                dailyTrainTicket.setEndPinyin(trainStationEnd.getNamePinyin());
                // 车票的终点站的到达时间就是终点站车站表中记录的进站时间
                dailyTrainTicket.setEndTime(trainStationEnd.getInTime());
                dailyTrainTicket.setEndIndex(trainStationEnd.getIndex());

                int ydzCount = dailyTrainSeatService.countSeat(date, trainCode, SeatType.YDZ.getCode());

                dailyTrainTicket.setYdz(ydzCount);
                dailyTrainTicket.setYdzPrice(BigDecimal.ZERO);
                dailyTrainTicket.setEdz(0);
                dailyTrainTicket.setEdzPrice(BigDecimal.ZERO);
                dailyTrainTicket.setRw(0);
                dailyTrainTicket.setRwPrice(BigDecimal.ZERO);
                dailyTrainTicket.setYw(0);
                dailyTrainTicket.setYwPrice(BigDecimal.ZERO);

                dailyTrainTicket.setCreatedAt(DateTime.now());
                dailyTrainTicket.setUpdatedAt(null);

                dailyTrainTicketMapper.insert(dailyTrainTicket);
            }
        }
    }
}
