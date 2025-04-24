package com.example.cr.business.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.common.context.UserContext;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.entity.ConfirmOrder;
import com.example.cr.business.entity.ConfirmOrderExample;
import com.example.cr.business.mapper.ConfirmOrderMapper;
import com.example.cr.business.request.ConfirmOrderListRequest;
import com.example.cr.business.response.ConfirmOrderResponse;
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
import com.example.cr.business.request.ConfirmOrderRequest;

@Service
public class ConfirmOrderService {
    private static final Logger log = LoggerFactory.getLogger(ConfirmOrderService.class);
    @Autowired
    ConfirmOrderMapper confirmOrderMapper;

    public PageResponse<ConfirmOrderResponse> list(ConfirmOrderListRequest request) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("id desc");

        // 处理搜索条件
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andTrainCodeLike(keyword);
            ConfirmOrderExample.Criteria criteria2 = confirmOrderExample.createCriteria();
            criteria2.andStartLike(keyword);
            confirmOrderExample.or(criteria2);
            ConfirmOrderExample.Criteria criteria3 = confirmOrderExample.createCriteria();
            criteria3.andEndLike(keyword);
            confirmOrderExample.or(criteria3);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<ConfirmOrder> confirmOrders = confirmOrderMapper.selectByExample(confirmOrderExample);

        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrders);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<ConfirmOrderResponse> list = BeanUtil.copyToList(confirmOrders, ConfirmOrderResponse.class);

        PageResponse<ConfirmOrderResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(ConfirmOrderRequest request) {
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(request, ConfirmOrder.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(confirmOrder.getId()) || confirmOrder.getId() == 0L) {
            confirmOrder.setId(SnowflakeUtil.getId());
            confirmOrder.setCreatedAt(now);
            confirmOrder.setUpdatedAt(now);
            confirmOrder.setUserId(UserContext.getId());
            confirmOrderMapper.insert(confirmOrder);
        } else {
            confirmOrder.setUpdatedAt(now);
            confirmOrderMapper.updateByPrimaryKeySelective(confirmOrder);
        }
    }

    public int deleteBatch(List<Long> ids) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
        criteria.andIdIn(ids);
        return confirmOrderMapper.deleteByExample(confirmOrderExample);
    }
}
