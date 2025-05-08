package com.example.cr.business.service;

import com.example.cr.business.entity.DailyTrainSeat;
import com.example.cr.business.mapper.DailyTrainSeatMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AfterConfirmOrderService {
    @Resource
    DailyTrainSeatMapper dailyTrainSeatMapper;

    /**
     * 选中座位后的事务处理：
     *         座位表修改售卖情况 sell 字段  （✔）
     *         真实扣减库存，更新【余票信息】的余票
     *         记录会员的购票记录
     *         更新【确认订单】表的订单状态=成功
     */
    @Transactional
    public void afterConfirm(List<DailyTrainSeat> finalSeatList){
        for (DailyTrainSeat dailyTrainSeat : finalSeatList) {
            DailyTrainSeat seatForUpdate = new DailyTrainSeat();
            seatForUpdate.setId(dailyTrainSeat.getId());
            seatForUpdate.setSell(dailyTrainSeat.getSell());
            seatForUpdate.setUpdatedAt(new Date());
            dailyTrainSeatMapper.updateByPrimaryKeySelective(seatForUpdate);
        }
    }
}
