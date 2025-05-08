package com.example.cr.business.service;
import java.util.ArrayList;
import java.util.Date;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.cr.business.entity.*;
import com.example.cr.business.enums.ConfirmOrderStatus;
import com.example.cr.business.enums.SeatCol;
import com.example.cr.business.enums.SeatType;
import com.example.cr.business.request.ConfirmOrderTicketRequest;
import com.example.cr.common.context.UserContext;
import com.example.cr.common.exception.CommonBusinessException;
import com.example.cr.common.response.PageResponse;
import com.example.cr.business.mapper.ConfirmOrderMapper;
import com.example.cr.business.request.ConfirmOrderListRequest;
import com.example.cr.business.response.ConfirmOrderResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
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

    @Autowired
    DailyTrainTicketService dailyTrainTicketService;

    @Autowired
    DailyTrainCarriageService dailyTrainCarriageService;

    @Autowired
    DailyTrainSeatService dailyTrainSeatService;

    @Autowired
    AfterConfirmOrderService afterConfirmOrderService;

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

    public void confirm(@Valid ConfirmOrderRequest request) {
        // 业务数据校验【暂略】如：车次、余票是否存在；车次是否有效；tickets 条数是否大于 0；同乘客同车次的票是否已经买过了

        Date date = request.getDate();
        String trainCode = request.getTrainCode();
        String start = request.getStart();
        String end = request.getEnd();

        List<ConfirmOrderTicketRequest> tickets = request.getTickets();

        // 保存到【确认订单】表，订单状态赋初始值
        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setId(SnowflakeUtil.getId());
        confirmOrder.setUserId(UserContext.getId());
        confirmOrder.setDate(date);
        confirmOrder.setTrainCode(trainCode);
        confirmOrder.setStart(start);
        confirmOrder.setEnd(end);
        confirmOrder.setDailyTrainTicketId(request.getDailyTrainTicketId());
        confirmOrder.setStatus(ConfirmOrderStatus.INIT.getCode());
        confirmOrder.setCreatedAt(DateTime.now());
        confirmOrder.setUpdatedAt(null);
        confirmOrder.setTickets(JSONUtil.toJsonStr(tickets));

        confirmOrderMapper.insert(confirmOrder);

        // 查询【余票信息】获取真实的库存
        DailyTrainTicket dailyTrainTicket = dailyTrainTicketService.selectByUnique(date, trainCode, start, end);
        log.info("查到余票信息 = {}", dailyTrainTicket);

        // 预扣减余票数量，并判断余票是否足够
        preReduceTicketCount(request, dailyTrainTicket);

        // 最终的选座结果
        List<DailyTrainSeat> finalSeatList = new ArrayList<>();

        // 选座
        // 区分是否主动选座
        ConfirmOrderTicketRequest firstTicketRequest = tickets.getFirst();
        if (StrUtil.isNotBlank(firstTicketRequest.getSeat())) {
            // 有主动选座
            log.info("本次用户购票【有】主动选座");

            // 辅助：计算座位的相对偏移值，提高多个座位的选座效率
            // 查出用户所选座位类型（总共包含哪些列）比如: 二等座 - A B C D F
            String seatTypeCode = firstTicketRequest.getSeatTypeCode();
            List<SeatCol> colEnumList = SeatCol.getColsByType(seatTypeCode);
            log.info("本次选座的座位类型={}，包含的列={}", SeatType.getEnumByCode(seatTypeCode), colEnumList);
            // 组成和前端一样的两排初始座位列表，用作参照的两排座位
            List<String> referSeatList = new ArrayList<>();
            for (int i = 1; i <= 2; i++) {
                for (SeatCol seatCol : colEnumList) {
                    referSeatList.add(seatCol.getCode() + i);
                }
            }
            log.info("用作参照的两排座位={}", referSeatList);
            // 绝对偏移值
            List<Integer> aboluteOffsetList = new ArrayList<>();
            List<Integer> offsetList = new ArrayList<>();
            for (ConfirmOrderTicketRequest ticketReq : tickets) {
                int index = referSeatList.indexOf(ticketReq.getSeat());
                aboluteOffsetList.add(index);
            }
            log.info("计算得到所选座位的绝对偏移值；{}", aboluteOffsetList);
            // 真正需要的偏移值（相对于第一个座位的偏移值）
            for (Integer index : aboluteOffsetList) {
                int offset = index - aboluteOffsetList.getFirst();
                offsetList.add(offset);
            }
            log.info("计算得到所选座位相对第一个座位的偏移值={}", offsetList);
            // 挑座位 - 按需
            getSeat(finalSeatList, date, trainCode, seatTypeCode, firstTicketRequest.getSeat().split("")[0], offsetList, dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex());
        } else {
            // 没有主动选座
            log.info("本次用户购票【无】主动选座");
            // 挑座位 - 随机
            for (ConfirmOrderTicketRequest ticket : tickets) {
                getSeat(finalSeatList, date, trainCode, ticket.getSeatTypeCode(), null, null, dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex());
            }
        }

        log.info("最终的选座结果={}", finalSeatList);

        // 遍历车厢，获取每个车厢的座位数据
        // 选座：挑选符合条件的座位，如果这个车厢不满足，则进入下个车厢（隐藏条件：多个选座必须在同一个车厢）

        // 选中座位后的事务处理：
        // 座位表修改售卖情况 sell 字段
        // 真实扣减库存，更新【余票信息】的余票
        // 记录会员的购票记录
        // 更新【确认订单】表的订单状态=成功
        afterConfirmOrderService.afterConfirm(dailyTrainTicket, finalSeatList);
    }

    /**
     * 挑座位
     *   如果有选座：根据之前计算好的【选座偏移值】可以一次性挑完
     *   如果无选座：一个一个挑，遇到未售的就选中
     *
     * @param date 日期
     * @param trainCode 车次编号
     * @param seatType 座位类型
     * @param column 列
     * @param offsetList 座位偏移值
     * @param startIndex 出发站序
     * @param endIndex 到站站序
     */
    private void getSeat(List<DailyTrainSeat> finalSeatList, Date date, String trainCode, String seatType, String column, List<Integer> offsetList, Integer startIndex, Integer endIndex) {
        List<DailyTrainSeat> getSeatList = new ArrayList<>();

        List<DailyTrainCarriage> carriageList = dailyTrainCarriageService.selectBySeatType(date, trainCode, seatType);
        log.info("查到符合条件(seatType={})的车厢数量={}", seatType, carriageList.size());
        // 一个车厢一个车厢的获取数据
        for (DailyTrainCarriage dailyTrainCarriage : carriageList) {
            // 换车厢时，需要清空【临时的/可能不一定完整】的选座信息 getSeatList
            getSeatList = new ArrayList<>();

            Integer trainCarriageIndex = dailyTrainCarriage.getIndex();
            log.info("开始从序号={}的车厢选座", trainCarriageIndex);
            List<DailyTrainSeat> seatList = dailyTrainSeatService.selectByCarriage(date, trainCode, trainCarriageIndex);
            log.info("车厢序号={}，座位数={}", trainCarriageIndex, seatList.size());

            for (int i = 0; i < seatList.size(); i++) {
                DailyTrainSeat dailyTrainSeat = seatList.get(i);
                Integer seatIndex = dailyTrainSeat.getCarriageIndex();
                String col = dailyTrainSeat.getCol();

                //判断当前的座位是否被选中过
                boolean alreadyChooseFlag = false;
                for (DailyTrainSeat finalSeat : finalSeatList) {
                    if (finalSeat.getId().equals(dailyTrainSeat.getId())) {
                        alreadyChooseFlag = true;
                        break;
                    }
                }
                if (alreadyChooseFlag) {
                    log.info("座位={}在当前选座过程中已被选中过，不能重复选择，继续判断下一个座位", seatIndex);
                    continue;
                }

                // 判断列号 column，有值的情况下要对比列号，也就是【有选座】的情况
                if (StrUtil.isBlank(column)){
                    log.info("无选座");
                } else {
                    if (!column.equals(col)){
                        log.info("座位={} 列值不对，继续判断下一个座位，当前列值={}，目标列值={}", seatIndex, col, column);
                        continue;
                    }
                }

                boolean isChoose = calSell(dailyTrainSeat, startIndex, endIndex);
                if (isChoose) {
                    log.info("选中座位");
                    getSeatList.add(dailyTrainSeat);
                } else {
                    continue;
                }

                // 根据偏移值 offset 选择剩下的座位
                boolean isGetAllOffsetSeat = true;
                if (CollUtil.isNotEmpty(offsetList)) {
                    log.info("有偏移值={}，根据偏移值校验座位是否可选", offsetList);
                    // 从索引 1 开始，索引 0 就是当前已选中的票
                    for (int j = 1; j < offsetList.size(); j++) {
                        Integer offset = offsetList.get(j);
                        int nextIndex = i + offset;

                        // 额外判断：【有选座】的情况下，一定要在同一个车箱
                        if (nextIndex >= seatList.size()) {
                            log.info("座位={} 不可选，偏移后的索引超出了当前车厢的座位数={}", nextIndex, seatList.size());
                            isGetAllOffsetSeat = false;
                            break;
                        }

                        DailyTrainSeat nextDailyTrainSeat = seatList.get(nextIndex);
                        boolean isChooseNext = calSell(nextDailyTrainSeat, startIndex, endIndex);
                        if (isChooseNext) {
                            log.info("座位={}被选中", nextDailyTrainSeat.getCarriageSeatIndex());
                            getSeatList.add(nextDailyTrainSeat);
                        } else {
                            log.info("座位={}不可选", nextDailyTrainSeat.getCarriageSeatIndex());
                            isGetAllOffsetSeat = false;
                            break;
                        }
                    }
                }
                if (!isGetAllOffsetSeat) {
                    // 未完全选中所有座位时，需要清空【临时的/可能不一定完整】的选座信息
                    getSeatList = new ArrayList<>();
                    continue;
                }

                // 保存选好的座位
                finalSeatList.addAll(getSeatList);
                return;
            }
        }
    }

    /**
     * 计算某座位在区间内是否可卖
     * 例：sell=10001
     *   - 第一位1表示0～1（起始站到第二个站），第二位0代表1～2（第2个站到第3个站） ...
     *   - 这个值表示第 0(起始站)～1 卖出去了，第 4～5 卖出去了
     *   - 本次购买区间站 1~4，则区间已售 000
     * 全部是 0，表示这个区间可买；只要有 1，就表示区间内已售过票
     * <p>
     * 选中后，要计算购票后的 sell，比如原来是 10001，本次购买区间站 1~4
     * 方案：构造本次购票完成之后的新售卖信息 01110，和原来的 sell=10001【按位或】最终得到 11111
     */
    private boolean calSell(DailyTrainSeat dailyTrainSeat, Integer startIndex, Integer endIndex) {
        // 拿到座位的当前的售卖情况
        String sell = dailyTrainSeat.getSell();
        // 拿到它的一小部分
        String sellPart = sell.substring(startIndex, endIndex);
        if (Integer.parseInt(sellPart) > 0){
            log.info("座位={}在本次车站区间={}~{}已出售，不可选", dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
            return false;
        } else {
            log.info("座位={}在本次车站区间={}~{}未出售，可选", dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
            //  111,   111
            String curSell = sellPart.replace('0', '1');
            // 0111,  0111
            curSell = StrUtil.fillBefore(curSell, '0', endIndex);
            // 01110, 01110
            curSell = StrUtil.fillAfter(curSell, '0', sell.length());

            // 当前区间售票信息 curSell=01110 与数据库中已售信息 sell=00001 按位或，即可得到该座位卖出此票后的售票详情
            // 15(01111), 14(01110 = 01110|00000)
            int newSellInt = NumberUtil.binaryToInt(curSell) | NumberUtil.binaryToInt(sell);
            //  1111,  1110
            String newSell = NumberUtil.getBinaryStr(newSellInt);
            // 01111, 01110
            newSell = StrUtil.fillBefore(newSell, '0', sell.length());
            log.info("座位={}被选中，原售票信息={}，车站区间={}~{}，即：{}，最终售票信息={}"
                    , dailyTrainSeat.getCarriageSeatIndex(), sell, startIndex, endIndex, curSell, newSell);
            // 现在只需要在内存中修改，并未更新到数据库
            dailyTrainSeat.setSell(newSell);

            return true;
        }
    }

    private void preReduceTicketCount(@Valid ConfirmOrderRequest request, DailyTrainTicket dailyTrainTicket) {
        // 从用户请求中获取用户要购买的票数
        List<ConfirmOrderTicketRequest> tickets = request.getTickets();
        for (ConfirmOrderTicketRequest ticketRequest : tickets) {
            String seatTypeCode = ticketRequest.getSeatTypeCode();
            SeatType seatType = EnumUtil.getBy(SeatType::getCode, seatTypeCode);
            switch(seatType){
                case YDZ -> {
                    int countAfterReduce = dailyTrainTicket.getYdz() - 1;
                    if (countAfterReduce < 0){
                        throw new CommonBusinessException("余票不足");
                    }
                    dailyTrainTicket.setYdz(countAfterReduce);
                }
                case EDZ -> {
                    int countAfterReduce = dailyTrainTicket.getEdz() - 1;
                    if (countAfterReduce < 0){
                        throw new CommonBusinessException("余票不足");
                    }
                    dailyTrainTicket.setEdz(countAfterReduce);
                }
                case RW -> {
                    int countAfterReduce = dailyTrainTicket.getRw() - 1;
                    if (countAfterReduce < 0){
                        throw new CommonBusinessException("余票不足");
                    }
                    dailyTrainTicket.setRw(countAfterReduce);
                }
                case YW -> {
                    int countAfterReduce = dailyTrainTicket.getYw() - 1;
                    if (countAfterReduce < 0){
                        throw new CommonBusinessException("余票不足");
                    }
                    dailyTrainTicket.setYw(countAfterReduce);
                }
            }
        }
    }
}
