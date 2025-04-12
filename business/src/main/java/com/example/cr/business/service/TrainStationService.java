package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.entity.TrainStation;
import com.example.cr.business.entity.TrainStationExample;
import com.example.cr.business.mapper.TrainStationMapper;
import com.example.cr.business.request.TrainStationListRequest;
import com.example.cr.business.response.TrainStationResponse;
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
import com.example.cr.business.request.TrainStationRequest;

@Service
public class TrainStationService {
    private static final Logger log = LoggerFactory.getLogger(TrainStationService.class);
    @Autowired
    TrainStationMapper trainStationMapper;

    public PageResponse<TrainStationResponse> list(TrainStationListRequest request) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("id desc");

        // 处理搜索条件
        TrainStationExample.Criteria criteria = trainStationExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andTrainCodeLike(keyword);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<TrainStation> trainStations = trainStationMapper.selectByExample(trainStationExample);

        PageInfo<TrainStation> pageInfo = new PageInfo<>(trainStations);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<TrainStationResponse> list = BeanUtil.copyToList(trainStations, TrainStationResponse.class);

        PageResponse<TrainStationResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(TrainStationRequest request) {
        TrainStation trainStation = BeanUtil.copyProperties(request, TrainStation.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(trainStation.getId()) || trainStation.getId() == 0L) {
            trainStation.setId(SnowflakeUtil.getId());
            trainStation.setCreatedAt(now);
            trainStation.setUpdatedAt(now);
            trainStationMapper.insert(trainStation);
        } else {
            trainStation.setUpdatedAt(now);
            trainStationMapper.updateByPrimaryKeySelective(trainStation);
        }
    }

    public int deleteBatch(List<Long> ids) {
        TrainStationExample trainStationExample = new TrainStationExample();
        TrainStationExample.Criteria criteria = trainStationExample.createCriteria();
        criteria.andIdIn(ids);
        return trainStationMapper.deleteByExample(trainStationExample);
    }

    public List<TrainStation> selectByTrainCode(String trainCode) {
        TrainStationExample example = new TrainStationExample();
        example.setOrderByClause("`index` asc");
        example.createCriteria().andTrainCodeEqualTo(trainCode);
        return trainStationMapper.selectByExample(example);
    }
}
