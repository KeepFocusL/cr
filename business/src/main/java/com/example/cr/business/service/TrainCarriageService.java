package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.entity.TrainCarriage;
import com.example.cr.business.entity.TrainCarriageExample;
import com.example.cr.business.mapper.TrainCarriageMapper;
import com.example.cr.business.request.TrainCarriageListRequest;
import com.example.cr.business.response.TrainCarriageResponse;
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
import com.example.cr.business.request.TrainCarriageRequest;

@Service
public class TrainCarriageService {
    private static final Logger log = LoggerFactory.getLogger(TrainCarriageService.class);
    @Autowired
    TrainCarriageMapper trainCarriageMapper;

    public PageResponse<TrainCarriageResponse> list(TrainCarriageListRequest request) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("id desc");

        // 处理搜索条件
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andTrainCodeLike(keyword);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<TrainCarriage> trainCarriages = trainCarriageMapper.selectByExample(trainCarriageExample);

        PageInfo<TrainCarriage> pageInfo = new PageInfo<>(trainCarriages);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<TrainCarriageResponse> list = BeanUtil.copyToList(trainCarriages, TrainCarriageResponse.class);

        PageResponse<TrainCarriageResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(TrainCarriageRequest request) {
        TrainCarriage trainCarriage = BeanUtil.copyProperties(request, TrainCarriage.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(trainCarriage.getId()) || trainCarriage.getId() == 0L) {
            trainCarriage.setId(SnowflakeUtil.getId());
            trainCarriage.setCreatedAt(now);
            trainCarriage.setUpdatedAt(now);
            trainCarriageMapper.insert(trainCarriage);
        } else {
            trainCarriage.setUpdatedAt(now);
            trainCarriageMapper.updateByPrimaryKeySelective(trainCarriage);
        }
    }

    public int deleteBatch(List<Long> ids) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();
        criteria.andIdIn(ids);
        return trainCarriageMapper.deleteByExample(trainCarriageExample);
    }

    public List<TrainCarriage> selectByTrainCode(String trainCode) {
        TrainCarriageExample example = new TrainCarriageExample();
        example.createCriteria().andTrainCodeEqualTo(trainCode);
        return trainCarriageMapper.selectByExample(example);
    }
}
