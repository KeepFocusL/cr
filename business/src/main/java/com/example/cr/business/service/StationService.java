package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.entity.Station;
import com.example.cr.business.entity.StationExample;
import com.example.cr.business.mapper.StationMapper;
import com.example.cr.business.request.StationListRequest;
import com.example.cr.business.response.StationResponse;
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
import com.example.cr.business.request.StationRequest;

@Service
public class StationService {
    private static final Logger log = LoggerFactory.getLogger(StationService.class);
    @Autowired
    StationMapper stationMapper;

    public PageResponse<StationResponse> list(StationListRequest request) {
        StationExample stationExample = new StationExample();
        stationExample.setOrderByClause("id desc");

        // 处理搜索条件
        StationExample.Criteria criteria = stationExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<Station> stations = stationMapper.selectByExample(stationExample);

        PageInfo<Station> pageInfo = new PageInfo<>(stations);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<StationResponse> list = BeanUtil.copyToList(stations, StationResponse.class);

        PageResponse<StationResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(StationRequest request) {
        Station station = BeanUtil.copyProperties(request, Station.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(station.getId()) || station.getId() == 0L) {
            station.setId(SnowflakeUtil.getId());
            station.setCreatedAt(now);
            station.setUpdatedAt(now);
            stationMapper.insert(station);
        } else {
            station.setUpdatedAt(now);
            stationMapper.updateByPrimaryKeySelective(station);
        }
    }

    public int deleteBatch(List<Long> ids) {
        StationExample stationExample = new StationExample();
        StationExample.Criteria criteria = stationExample.createCriteria();
        criteria.andIdIn(ids);
        return stationMapper.deleteByExample(stationExample);
    }
}
