package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.entity.Train;
import com.example.cr.business.entity.TrainExample;
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

@Service
public class TrainService {
    private static final Logger log = LoggerFactory.getLogger(TrainService.class);
    @Autowired
    TrainMapper trainMapper;

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
}
