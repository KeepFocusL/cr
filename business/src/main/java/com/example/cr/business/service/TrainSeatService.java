package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.entity.TrainSeat;
import com.example.cr.business.entity.TrainSeatExample;
import com.example.cr.business.mapper.TrainSeatMapper;
import com.example.cr.business.request.TrainSeatListRequest;
import com.example.cr.business.response.TrainSeatResponse;
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
import com.example.cr.business.request.TrainSeatRequest;

@Service
public class TrainSeatService {
    private static final Logger log = LoggerFactory.getLogger(TrainSeatService.class);
    @Autowired
    TrainSeatMapper trainSeatMapper;

    public PageResponse<TrainSeatResponse> list(TrainSeatListRequest request) {
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.setOrderByClause("id desc");

        // 处理搜索条件
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andTrainCodeLike(keyword);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<TrainSeat> trainSeats = trainSeatMapper.selectByExample(trainSeatExample);

        PageInfo<TrainSeat> pageInfo = new PageInfo<>(trainSeats);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<TrainSeatResponse> list = BeanUtil.copyToList(trainSeats, TrainSeatResponse.class);

        PageResponse<TrainSeatResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(TrainSeatRequest request) {
        TrainSeat trainSeat = BeanUtil.copyProperties(request, TrainSeat.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(trainSeat.getId()) || trainSeat.getId() == 0L) {
            trainSeat.setId(SnowflakeUtil.getId());
            trainSeat.setCreatedAt(now);
            trainSeat.setUpdatedAt(now);
            trainSeatMapper.insert(trainSeat);
        } else {
            trainSeat.setUpdatedAt(now);
            trainSeatMapper.updateByPrimaryKeySelective(trainSeat);
        }
    }

    public int deleteBatch(List<Long> ids) {
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        criteria.andIdIn(ids);
        return trainSeatMapper.deleteByExample(trainSeatExample);
    }

    public List<TrainSeat> selectByTrainCode(String trainCode) {
        TrainSeatExample example = new TrainSeatExample();
        example.createCriteria().andTrainCodeEqualTo(trainCode);
        return trainSeatMapper.selectByExample(example);
    }
}
