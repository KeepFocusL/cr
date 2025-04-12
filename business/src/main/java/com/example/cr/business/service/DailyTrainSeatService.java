package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.business.entity.TrainSeat;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.entity.DailyTrainSeat;
import com.example.cr.business.entity.DailyTrainSeatExample;
import com.example.cr.business.mapper.DailyTrainSeatMapper;
import com.example.cr.business.request.DailyTrainSeatListRequest;
import com.example.cr.business.response.DailyTrainSeatResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.cr.common.util.SnowflakeUtil;
import com.example.cr.business.request.DailyTrainSeatRequest;

@Service
public class DailyTrainSeatService {
    private static final Logger log = LoggerFactory.getLogger(DailyTrainSeatService.class);
    @Autowired
    DailyTrainSeatMapper dailyTrainSeatMapper;

    @Autowired
    TrainSeatService trainSeatService;

    public PageResponse<DailyTrainSeatResponse> list(DailyTrainSeatListRequest request) {
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.setOrderByClause("id desc");

        // 处理搜索条件
        DailyTrainSeatExample.Criteria criteria = dailyTrainSeatExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andTrainCodeLike(keyword);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<DailyTrainSeat> dailyTrainSeats = dailyTrainSeatMapper.selectByExample(dailyTrainSeatExample);

        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(dailyTrainSeats);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<DailyTrainSeatResponse> list = BeanUtil.copyToList(dailyTrainSeats, DailyTrainSeatResponse.class);

        PageResponse<DailyTrainSeatResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }

    public void save(DailyTrainSeatRequest request) {
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(request, DailyTrainSeat.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(dailyTrainSeat.getId()) || dailyTrainSeat.getId() == 0L) {
            dailyTrainSeat.setId(SnowflakeUtil.getId());
            dailyTrainSeat.setCreatedAt(now);
            dailyTrainSeat.setUpdatedAt(now);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        } else {
            dailyTrainSeat.setUpdatedAt(now);
            dailyTrainSeatMapper.updateByPrimaryKeySelective(dailyTrainSeat);
        }
    }

    public int deleteBatch(List<Long> ids) {
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        DailyTrainSeatExample.Criteria criteria = dailyTrainSeatExample.createCriteria();
        criteria.andIdIn(ids);
        return dailyTrainSeatMapper.deleteByExample(dailyTrainSeatExample);
    }

    public void genDaily(Date date, String trainCode) {
        // 1、先删除
        DailyTrainSeatExample example = new DailyTrainSeatExample();
        example.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
        dailyTrainSeatMapper.deleteByExample(example);

        // 2、找到对应基础数据
        List<TrainSeat> trainSeats = trainSeatService.selectByTrainCode(trainCode);

        // 3、在对应基础数据上增加 date 数据
        for (TrainSeat trainSeat : trainSeats) {
            DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(trainSeat, DailyTrainSeat.class);
            dailyTrainSeat.setDate(date);
            dailyTrainSeat.setId(SnowflakeUtil.getId());
            dailyTrainSeat.setCreatedAt(DateTime.now());
            dailyTrainSeat.setUpdatedAt(null);
            dailyTrainSeat.setSell("0");
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        }
    }
}
