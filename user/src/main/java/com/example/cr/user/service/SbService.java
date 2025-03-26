package com.example.cr.user.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.common.response.PageResponse;
import com.example.cr.user.entity.Sb;
import com.example.cr.user.entity.SbExample;
import com.example.cr.user.mapper.SbMapper;
import com.example.cr.user.request.SbListRequest;
import com.example.cr.user.response.SbResponse;
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
import com.example.cr.user.request.SbRequest;

@Service
public class SbService {
    private static final Logger log = LoggerFactory.getLogger(SbService.class);
    @Autowired
    SbMapper sbMapper;

    public PageResponse<SbResponse> list(SbListRequest request) {
        SbExample sbExample = new SbExample();
        sbExample.setOrderByClause("id desc");

        // 处理搜索条件
        SbExample.Criteria criteria = sbExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andNameLike(keyword);
            SbExample.Criteria criteria2 = sbExample.createCriteria();
            criteria2.andEmailLike(keyword);
            sbExample.or(criteria2);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<Sb> sbs = sbMapper.selectByExample(sbExample);

        PageInfo<Sb> pageInfo = new PageInfo<>(sbs);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<SbResponse> list = BeanUtil.copyToList(sbs, SbResponse.class);

        PageResponse<SbResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(SbRequest request) {
        Sb sb = BeanUtil.copyProperties(request, Sb.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(sb.getId()) || sb.getId() == 0L) {
            sb.setId(SnowflakeUtil.getId());
            sb.setCreatedAt(now);
            sb.setUpdatedAt(now);
            sbMapper.insert(sb);
        } else {
            sb.setUpdatedAt(now);
            sbMapper.updateByPrimaryKeySelective(sb);
        }
    }

    public int deleteBatch(List<Long> ids) {
        SbExample sbExample = new SbExample();
        SbExample.Criteria criteria = sbExample.createCriteria();
        criteria.andIdIn(ids);
        return sbMapper.deleteByExample(sbExample);
    }
}
