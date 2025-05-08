package com.example.cr.business.service;

import com.example.cr.business.entity.DailyTrainSeat;
import com.example.cr.business.entity.DailyTrainTicket;
import com.example.cr.business.mapper.DailyTrainSeatMapper;
import com.example.cr.business.mapper.custom.DailyTrainTicketMapperCustom;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AfterConfirmOrderService {
    private static final Logger log = LoggerFactory.getLogger(AfterConfirmOrderService.class);

    @Resource
    DailyTrainSeatMapper dailyTrainSeatMapper;

    @Autowired
    DailyTrainTicketMapperCustom dailyTrainTicketMapperCustom;

    /**
     * 选中座位后的事务处理：
     *         座位表修改售卖情况 sell 字段  （✔）
     *         真实扣减库存，更新【余票信息】的余票
     *         记录会员的购票记录
     *         更新【确认订单】表的订单状态=成功
     */
    @Transactional
    public void afterConfirm(DailyTrainTicket dailyTrainTicket, List<DailyTrainSeat> finalSeatList){
        for (DailyTrainSeat dailyTrainSeat : finalSeatList) {
            DailyTrainSeat seatForUpdate = new DailyTrainSeat();
            seatForUpdate.setId(dailyTrainSeat.getId());
            seatForUpdate.setSell(dailyTrainSeat.getSell());
            seatForUpdate.setUpdatedAt(new Date());
            dailyTrainSeatMapper.updateByPrimaryKeySelective(seatForUpdate);
            log.info("完成：更新座位表的售卖情况 sell 字段");

            Integer startIndex = dailyTrainTicket.getStartIndex();
            Integer endIndex = dailyTrainTicket.getEndIndex();
            char[] chars = seatForUpdate.getSell().toCharArray();
            Integer maxStartIndex = endIndex - 1;
            Integer minEndIndex = startIndex + 1;
            int minStartIndex = 0;
            for (int i = startIndex - 1; i >= 0; i--) {
                char aChar = chars[i];
                if (aChar == '1') {
                    minStartIndex = i + 1;
                    break;
                }
            }
            log.info("影响出发站区间：最小出发站index={} ~ 最大出发站index={}", minStartIndex, maxStartIndex);

            int maxEndIndex = seatForUpdate.getSell().length();
            for (int i = endIndex; i < seatForUpdate.getSell().length(); i++) {
                char aChar = chars[i];
                if (aChar == '1') {
                    maxEndIndex = i;
                    break;
                }
            }
            log.info("影响到达站区间：最小到达站index={} ~ 最大到达站index={}", minEndIndex, maxEndIndex);

            dailyTrainTicketMapperCustom.updateCountBySell(
                    dailyTrainSeat.getDate(),
                    dailyTrainSeat.getTrainCode(),
                    dailyTrainSeat.getSeatType(),
                    minStartIndex,
                    maxStartIndex,
                    minEndIndex,
                    maxEndIndex);
            log.info("完成：真实扣减库存，更新【余票信息】的余票");
        }
    }
}
