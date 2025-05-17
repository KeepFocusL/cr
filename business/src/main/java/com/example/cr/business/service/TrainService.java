package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.example.cr.business.entity.*;
import com.example.cr.business.enums.SeatCol;
import com.example.cr.business.mapper.TrainSeatMapper;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.mapper.TrainMapper;
import com.example.cr.business.request.TrainListRequest;
import com.example.cr.business.response.TrainResponse;
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
import com.example.cr.business.request.TrainRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainService {
    private static final Logger log = LoggerFactory.getLogger(TrainService.class);
    @Autowired
    TrainMapper trainMapper;

    @Autowired
    TrainCarriageService trainCarriageService;

    @Autowired
    TrainSeatMapper trainSeatMapper;

    public PageResponse<TrainResponse> list(TrainListRequest request) {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("id desc");

        // 处理搜索条件
        TrainExample.Criteria criteria = trainExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andCodeLike(keyword);
            TrainExample.Criteria criteria2 = trainExample.createCriteria();
            criteria2.andStartLike(keyword);
            trainExample.or(criteria2);
            TrainExample.Criteria criteria3 = trainExample.createCriteria();
            criteria3.andStartPinyinLike(keyword);
            trainExample.or(criteria3);
            TrainExample.Criteria criteria4 = trainExample.createCriteria();
            criteria4.andEndLike(keyword);
            trainExample.or(criteria4);
            TrainExample.Criteria criteria5 = trainExample.createCriteria();
            criteria5.andEndPinyinLike(keyword);
            trainExample.or(criteria5);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<Train> trains = trainMapper.selectByExample(trainExample);

        PageInfo<Train> pageInfo = new PageInfo<>(trains);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<TrainResponse> list = BeanUtil.copyToList(trains, TrainResponse.class);

        PageResponse<TrainResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(TrainRequest request) {
        Train train = BeanUtil.copyProperties(request, Train.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(train.getId()) || train.getId() == 0L) {
            train.setId(SnowflakeUtil.getId());
            train.setCreatedAt(now);
            train.setUpdatedAt(now);
            trainMapper.insert(train);
        } else {
            train.setUpdatedAt(now);
            trainMapper.updateByPrimaryKeySelective(train);
        }
    }

    public int deleteBatch(List<Long> ids) {
        TrainExample trainExample = new TrainExample();
        TrainExample.Criteria criteria = trainExample.createCriteria();
        criteria.andIdIn(ids);
        return trainMapper.deleteByExample(trainExample);
    }

    public List<Train> selectAll(){
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("id asc");
        return trainMapper.selectByExample(trainExample);
    }

    public void genSeat(String trainCode) {
        DateTime now = DateTime.now();

        // 1. 车次：[前置准备] 清空当前车次下的所有座位数据（或者检测到当前车次已经有座位就不允许重复生成）
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        trainSeatMapper.deleteByExample(trainSeatExample);
        log.debug("车次[{}]：[前置准备] 清空当前车次下的所有座位数据", trainCode);

        // 2. 车厢：查找当前车次下的所有车厢
        List<TrainCarriage> trainCarriages = trainCarriageService.selectByTrainCode(trainCode);
        log.debug("车厢：查找当前车次下的所有车厢，共 {} 节", trainCarriages.size());

        // 3. 座位：循环生成每个车厢的座位
        for (TrainCarriage trainCarriage : trainCarriages) {
            // 3.1 获取车厢的详情：行数、座位类型(根据座位类型可以自动算出列数)
            Integer rowCount = trainCarriage.getRowCount();
            String seatType = trainCarriage.getSeatType();
            log.debug("获取车厢的详情：行数{}、座位类型{}", rowCount, seatType);
            int seatIndex = 1;

            // 3.2 根据座位类型，获取所有的列。比如车厢类型是一等座，则可以获取到列 A、C、D、F
            List<SeatCol> seatCols = SeatCol.getColsByType(seatType);
            log.debug("根据座位类型[{}]，获取所有的列[{}]", seatType, seatCols);

            // 3.3 行数：循环
            for (int row = 1; row <= rowCount; row++) {
                log.debug("开始生成车次{}，车厢{}，排数{}，具体座位{}", trainCode, trainCarriage.getIndex(), row, seatCols);
                // 3.4 列数：循环
                for (SeatCol seatCol: seatCols) {
                    // 3.5 自动构造出座位数据并入库
                    TrainSeat trainSeat = new TrainSeat();
                    trainSeat.setId(SnowflakeUtil.getId());
                    trainSeat.setTrainCode(trainCode);
                    trainSeat.setCarriageIndex(trainCarriage.getIndex());
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(row), '0', 2));
                    trainSeat.setCol(seatCol.getCode());
                    trainSeat.setSeatType(seatType);
                    trainSeat.setCarriageSeatIndex(seatIndex++);
                    trainSeat.setCreatedAt(now);
                    trainSeatMapper.insert(trainSeat);
                }
            }
        }
    }

    @Transactional
    public void testCache() {
        System.out.println("TrainService.testCache - 第一次");
        List<Train> trains = selectAll();
        // 模拟经过了很多复杂的业务 又需要查询同样的 sql
        System.out.println("TrainService.testCache - 第二次");
        trains = selectAll();
        // 查看控制台的日志输出
    }
}
