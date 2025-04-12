package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.example.cr.business.entity.TrainStation;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.entity.DailyTrainStation;
import com.example.cr.business.entity.DailyTrainStationExample;
import com.example.cr.business.mapper.DailyTrainStationMapper;
import com.example.cr.business.request.DailyTrainStationListRequest;
import com.example.cr.business.response.DailyTrainStationResponse;
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
import com.example.cr.business.request.DailyTrainStationRequest;

@Service
public class DailyTrainStationService {
    private static final Logger log = LoggerFactory.getLogger(DailyTrainStationService.class);
    @Autowired
    DailyTrainStationMapper dailyTrainStationMapper;

    @Autowired
    TrainStationService trainStationService;

    public PageResponse<DailyTrainStationResponse> list(DailyTrainStationListRequest request) {
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        dailyTrainStationExample.setOrderByClause("id desc");

        // 处理搜索条件
        DailyTrainStationExample.Criteria criteria = dailyTrainStationExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andTrainCodeLike(keyword);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<DailyTrainStation> dailyTrainStations = dailyTrainStationMapper.selectByExample(dailyTrainStationExample);

        PageInfo<DailyTrainStation> pageInfo = new PageInfo<>(dailyTrainStations);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<DailyTrainStationResponse> list = BeanUtil.copyToList(dailyTrainStations, DailyTrainStationResponse.class);

        PageResponse<DailyTrainStationResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(DailyTrainStationRequest request) {
        DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(request, DailyTrainStation.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(dailyTrainStation.getId()) || dailyTrainStation.getId() == 0L) {
            dailyTrainStation.setId(SnowflakeUtil.getId());
            dailyTrainStation.setCreatedAt(now);
            dailyTrainStation.setUpdatedAt(now);
            dailyTrainStationMapper.insert(dailyTrainStation);
        } else {
            dailyTrainStation.setUpdatedAt(now);
            dailyTrainStationMapper.updateByPrimaryKeySelective(dailyTrainStation);
        }
    }

    public int deleteBatch(List<Long> ids) {
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        DailyTrainStationExample.Criteria criteria = dailyTrainStationExample.createCriteria();
        criteria.andIdIn(ids);
        return dailyTrainStationMapper.deleteByExample(dailyTrainStationExample);
    }

    public void genDaily(Date date, String trainCode) {
        log.debug("生成日期【{}】车次【{}】的【每日火车车站】数据 - 开始", DateUtil.formatDate(date), trainCode);
        // 1、清除已有的相关数据
        DailyTrainStationExample example = new DailyTrainStationExample();
        example.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
        dailyTrainStationMapper.deleteByExample(example);
        log.debug("先删除日期【{}】车次【{}】的已有【每日火车车站】数据", DateUtil.formatDate(date), trainCode);

        // 2、查出当前车次经过的所有车站
        List<TrainStation> trainStations = trainStationService.selectByTrainCode(trainCode);
        for (TrainStation trainStation : trainStations) {
            DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(trainStation, DailyTrainStation.class);
            dailyTrainStation.setDate(date);
            dailyTrainStation.setId(SnowflakeUtil.getId());
            dailyTrainStation.setCreatedAt(DateTime.now());
            dailyTrainStation.setUpdatedAt(null);
            dailyTrainStationMapper.insert(dailyTrainStation);
        }
        log.debug("生成日期【{}】车次【{}】的【每日火车车站】数据 - 完成", DateUtil.formatDate(date), trainCode);
    }
}
