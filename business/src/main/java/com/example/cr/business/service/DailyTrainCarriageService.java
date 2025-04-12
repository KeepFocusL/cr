package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.business.entity.TrainCarriage;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.entity.DailyTrainCarriage;
import com.example.cr.business.entity.DailyTrainCarriageExample;
import com.example.cr.business.mapper.DailyTrainCarriageMapper;
import com.example.cr.business.request.DailyTrainCarriageListRequest;
import com.example.cr.business.response.DailyTrainCarriageResponse;
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
import com.example.cr.business.request.DailyTrainCarriageRequest;

@Service
public class DailyTrainCarriageService {
    private static final Logger log = LoggerFactory.getLogger(DailyTrainCarriageService.class);
    @Autowired
    DailyTrainCarriageMapper dailyTrainCarriageMapper;

    @Autowired
    TrainCarriageService trainCarriageService;

    public PageResponse<DailyTrainCarriageResponse> list(DailyTrainCarriageListRequest request) {
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.setOrderByClause("id desc");

        // 处理搜索条件
        DailyTrainCarriageExample.Criteria criteria = dailyTrainCarriageExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andTrainCodeLike(keyword);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<DailyTrainCarriage> dailyTrainCarriages = dailyTrainCarriageMapper.selectByExample(dailyTrainCarriageExample);

        PageInfo<DailyTrainCarriage> pageInfo = new PageInfo<>(dailyTrainCarriages);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<DailyTrainCarriageResponse> list = BeanUtil.copyToList(dailyTrainCarriages, DailyTrainCarriageResponse.class);

        PageResponse<DailyTrainCarriageResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(DailyTrainCarriageRequest request) {
        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(request, DailyTrainCarriage.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(dailyTrainCarriage.getId()) || dailyTrainCarriage.getId() == 0L) {
            dailyTrainCarriage.setId(SnowflakeUtil.getId());
            dailyTrainCarriage.setCreatedAt(now);
            dailyTrainCarriage.setUpdatedAt(now);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        } else {
            dailyTrainCarriage.setUpdatedAt(now);
            dailyTrainCarriageMapper.updateByPrimaryKeySelective(dailyTrainCarriage);
        }
    }

    public int deleteBatch(List<Long> ids) {
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        DailyTrainCarriageExample.Criteria criteria = dailyTrainCarriageExample.createCriteria();
        criteria.andIdIn(ids);
        return dailyTrainCarriageMapper.deleteByExample(dailyTrainCarriageExample);
    }

    public void genDaily(Date date, String trainCode) {
        // 1、删除数据库中的每日火车车站相关的数据
        DailyTrainCarriageExample example = new DailyTrainCarriageExample();
        example.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
        dailyTrainCarriageMapper.deleteByExample(example);

        // 2、查出基础数据之火车车站
        List<TrainCarriage> trainCarriages = trainCarriageService.selectByTrainCode(trainCode);

        // 3、在查到的基础数据的基础上增加每日需要的日期字段和其他相关字段
        for (TrainCarriage trainCarriage : trainCarriages) {
            DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(trainCarriage, DailyTrainCarriage.class);
            dailyTrainCarriage.setDate(date);
            dailyTrainCarriage.setId(SnowflakeUtil.getId());
            dailyTrainCarriage.setCreatedAt(DateTime.now());
            dailyTrainCarriage.setUpdatedAt(null);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        }
    }
}
