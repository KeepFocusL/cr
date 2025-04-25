package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import com.example.cr.business.entity.Train;
import com.example.cr.business.entity.TrainStation;
import com.example.cr.business.enums.SeatType;
import com.example.cr.business.enums.TrainType;
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
import java.math.RoundingMode;
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

    public void genDaily(Date date, Train train) {
        String trainCode = train.getCode();
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
            BigDecimal kmSum = BigDecimal.ZERO;
            for (int j = (i + 1); j < trainStations.size(); j++) {
                // 第二层循环 得到到达站
                TrainStation trainStationEnd = trainStations.get(j);
                kmSum = kmSum.add(trainStationEnd.getKm());

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
                int edzCount = dailyTrainSeatService.countSeat(date, trainCode, SeatType.EDZ.getCode());
                int rwCount = dailyTrainSeatService.countSeat(date, trainCode, SeatType.RW.getCode());
                int ywCount = dailyTrainSeatService.countSeat(date, trainCode, SeatType.YW.getCode());

                // 火车系数
                BigDecimal priceRate = EnumUtil.getFieldBy(TrainType::getPriceRate, TrainType::getCode, train.getType());
                // 座位单价
                BigDecimal ydzPriceBySeatType = SeatType.YDZ.getPrice();
                // 计算票价 = 里程(公里) * 座位单价(元/公里) * 火车类型(系数)
                BigDecimal ydzPrice = kmSum.multiply(priceRate).multiply(ydzPriceBySeatType).setScale(2, RoundingMode.HALF_UP);

                BigDecimal edzPrice = kmSum.multiply(priceRate).multiply(SeatType.EDZ.getPrice()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal rwPrice = kmSum.multiply(priceRate).multiply(SeatType.RW.getPrice()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ywPrice = kmSum.multiply(priceRate).multiply(SeatType.YW.getPrice()).setScale(2, RoundingMode.HALF_UP);

                dailyTrainTicket.setYdz(ydzCount);
                dailyTrainTicket.setYdzPrice(ydzPrice);
                dailyTrainTicket.setEdz(edzCount);
                dailyTrainTicket.setEdzPrice(edzPrice);
                dailyTrainTicket.setRw(rwCount);
                dailyTrainTicket.setRwPrice(rwPrice);
                dailyTrainTicket.setYw(ywCount);
                dailyTrainTicket.setYwPrice(ywPrice);

                dailyTrainTicket.setCreatedAt(DateTime.now());
                dailyTrainTicket.setUpdatedAt(null);

                dailyTrainTicketMapper.insert(dailyTrainTicket);
            }
        }
    }

    public DailyTrainTicket selectByUnique(Date date, String trainCode, String start, String end) {
        DailyTrainTicketExample example = new DailyTrainTicketExample();
        example.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode)
                .andStartEqualTo(start)
                .andEndEqualTo(end);
        List<DailyTrainTicket> dailyTrainTickets = dailyTrainTicketMapper.selectByExample(example);
        if (CollUtil.isNotEmpty(dailyTrainTickets)) {
            return dailyTrainTickets.getFirst();
        } else {
            return null;
        }
    }
}
