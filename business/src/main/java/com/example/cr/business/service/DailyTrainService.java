package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.example.cr.business.entity.Train;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.entity.DailyTrain;
import com.example.cr.business.entity.DailyTrainExample;
import com.example.cr.business.mapper.DailyTrainMapper;
import com.example.cr.business.request.DailyTrainListRequest;
import com.example.cr.business.response.DailyTrainResponse;
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
import com.example.cr.business.request.DailyTrainRequest;

@Service
public class DailyTrainService {
    private static final Logger log = LoggerFactory.getLogger(DailyTrainService.class);
    @Autowired
    DailyTrainMapper dailyTrainMapper;

    @Autowired
    TrainService trainService;

    @Autowired
    DailyTrainStationService dailyTrainStationService;

    @Autowired
    DailyTrainCarriageService dailyTrainCarriageService;

    public PageResponse<DailyTrainResponse> list(DailyTrainListRequest request) {
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.setOrderByClause("id desc");

        // 处理搜索条件
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andCodeLike(keyword);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<DailyTrain> dailyTrains = dailyTrainMapper.selectByExample(dailyTrainExample);

        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrains);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<DailyTrainResponse> list = BeanUtil.copyToList(dailyTrains, DailyTrainResponse.class);

        PageResponse<DailyTrainResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(DailyTrainRequest request) {
        DailyTrain dailyTrain = BeanUtil.copyProperties(request, DailyTrain.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(dailyTrain.getId()) || dailyTrain.getId() == 0L) {
            dailyTrain.setId(SnowflakeUtil.getId());
            dailyTrain.setCreatedAt(now);
            dailyTrain.setUpdatedAt(now);
            dailyTrainMapper.insert(dailyTrain);
        } else {
            dailyTrain.setUpdatedAt(now);
            dailyTrainMapper.updateByPrimaryKeySelective(dailyTrain);
        }
    }

    public int deleteBatch(List<Long> ids) {
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();
        criteria.andIdIn(ids);
        return dailyTrainMapper.deleteByExample(dailyTrainExample);
    }

    /**
     * 生成每日数据
     * 包含：每日车次、每日火车车站、每日火车车厢、每日座位
     * @param date 指定日期
     */
    public void genDaily(Date date){
        // 生成指定日期的每日车次
        //   查出所有的车次
        List<Train> trains = trainService.selectAll();
        for (Train train : trains) {
            genDailyTrain(date, train);
            genDailyTrainStation(date, train);
            genDailyTrainCarriage(date, train);
            genDailyTrainSeat(date, train);
        }
    }

    private void genDailyTrain(Date date, Train train){
        // 删除指定车次在指定日期下的所有每日车次
        System.out.println("先删除日期 = " + DateUtil.formatDate(date) + "，车次编号 = " + train.getCode() + " 的【每日车次】已有数据");
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.createCriteria()
                .andDateEqualTo(date).
                andCodeEqualTo(train.getCode());
        dailyTrainMapper.deleteByExample(dailyTrainExample);


        // 生成指定日期的【每日车次】
        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
        dailyTrain.setId(SnowflakeUtil.getId());
        dailyTrain.setDate(date);
        dailyTrain.setCreatedAt(DateTime.now());

        dailyTrainMapper.insert(dailyTrain);
        System.out.println("生成日期 = " + DateUtil.formatDate(date) + "，车次编号 = " + train.getCode() + " 的【每日车次】数据已完成");
    }

    private void genDailyTrainStation(Date date, Train train){
        // 生成指定日期的【每日火车车站】
        System.out.println("正在生成日期 = " + DateUtil.formatDate(date) + "，车次编号 = " + train.getCode() + " 的【每日火车车站】数据");

        dailyTrainStationService.genDaily(date, train.getCode());
    }

    private void genDailyTrainCarriage(Date date, Train train){
        // 生成指定日期的【每日火车车厢】
        System.out.println("正在生成日期 = " + DateUtil.formatDate(date) + "，车次编号 = " + train.getCode() + " 的【每日火车车厢】数据");

        dailyTrainCarriageService.genDaily(date, train.getCode());
    }

    private void genDailyTrainSeat(Date date, Train train){
        // 生成指定日期的【每日座位】
        System.out.println("正在生成日期 = " + DateUtil.formatDate(date) + "，车次编号 = " + train.getCode() + " 的【每日座位】数据");
    }
}
