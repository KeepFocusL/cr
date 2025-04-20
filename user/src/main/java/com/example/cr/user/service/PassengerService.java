package com.example.cr.user.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.common.response.PageResponse;
import com.example.cr.user.entity.Passenger;
import com.example.cr.user.entity.PassengerExample;
import com.example.cr.user.mapper.PassengerMapper;
import com.example.cr.user.request.PassengerListRequest;
import com.example.cr.user.response.PassengerResponse;
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
import com.example.cr.user.request.PassengerRequest;

@Service
public class PassengerService {
    private static final Logger log = LoggerFactory.getLogger(PassengerService.class);
    @Autowired
    PassengerMapper passengerMapper;

    public PageResponse<PassengerResponse> list(PassengerListRequest request) {
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.setOrderByClause("id desc");

        // 处理搜索条件
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andNameLike(keyword);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<Passenger> passengers = passengerMapper.selectByExample(passengerExample);

        PageInfo<Passenger> pageInfo = new PageInfo<>(passengers);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<PassengerResponse> list = BeanUtil.copyToList(passengers, PassengerResponse.class);

        PageResponse<PassengerResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(PassengerRequest request) {
        Passenger passenger = BeanUtil.copyProperties(request, Passenger.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(passenger.getId()) || passenger.getId() == 0L) {
            passenger.setId(SnowflakeUtil.getId());
            passenger.setCreatedAt(now);
            passenger.setUpdatedAt(now);
            passengerMapper.insert(passenger);
        } else {
            passenger.setUpdatedAt(now);
            passengerMapper.updateByPrimaryKeySelective(passenger);
        }
    }

    public int deleteBatch(List<Long> ids) {
        PassengerExample passengerExample = new PassengerExample();
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        criteria.andIdIn(ids);
        return passengerMapper.deleteByExample(passengerExample);
    }
}
