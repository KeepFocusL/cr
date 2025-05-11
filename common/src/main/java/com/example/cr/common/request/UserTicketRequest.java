package com.example.cr.common.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class UserTicketRequest {

    /**
     * 乘客id
     */
    @NotNull(message = "【会员id】不能为空")
    private Long userId;

    /**
     * 乘客id
     */
    @NotNull(message = "【乘客id】不能为空")
    private Long passengerId;

    /**
     * 乘客id
     */
    @NotNull(message = "【乘客名字】不能为空")
    private String passengerName;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @NotNull(message = "【日期】不能为空")
    private Date trainDate;

    /**
     * 车次编号
     */
    @NotBlank(message = "【车次编号】不能为空")
    private String trainCode;

    /**
     * 箱序
     */
    @NotNull(message = "【箱序】不能为空")
    private Integer carriageIndex;

    /**
     * 排号|01,02...
     */
    @NotBlank(message = "【排号】不能为空")
    private String seatRow;

    /**
     * 列号|枚举[SeatColumn]
     */
    @NotBlank(message = "【列号】不能为空")
    private String seatCol;

    /**
     * 出发站
     */
    @NotBlank(message = "【出发站】不能为空")
    private String startStation;

    /**
     * 出发时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【出发时间】不能为空")
    private Date startTime;

    /**
     * 到达站
     */
    @NotBlank(message = "【到达站】不能为空")
    private String endStation;

    /**
     * 到站时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【到站时间】不能为空")
    private Date endTime;

    /**
     * 座位类型|枚举[SeatType]
     */
    @NotBlank(message = "【座位类型】不能为空")
    private String seatType;

    public @NotNull(message = "【会员id】不能为空") Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull(message = "【会员id】不能为空") Long userId) {
        this.userId = userId;
    }

    public @NotNull(message = "【乘客id】不能为空") Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(@NotNull(message = "【乘客id】不能为空") Long passengerId) {
        this.passengerId = passengerId;
    }

    public @NotNull(message = "【乘客名字】不能为空") String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(@NotNull(message = "【乘客名字】不能为空") String passengerName) {
        this.passengerName = passengerName;
    }

    public @NotNull(message = "【日期】不能为空") Date getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(@NotNull(message = "【日期】不能为空") Date trainDate) {
        this.trainDate = trainDate;
    }

    public @NotBlank(message = "【车次编号】不能为空") String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(@NotBlank(message = "【车次编号】不能为空") String trainCode) {
        this.trainCode = trainCode;
    }

    public @NotNull(message = "【箱序】不能为空") Integer getCarriageIndex() {
        return carriageIndex;
    }

    public void setCarriageIndex(@NotNull(message = "【箱序】不能为空") Integer carriageIndex) {
        this.carriageIndex = carriageIndex;
    }

    public @NotBlank(message = "【排号】不能为空") String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(@NotBlank(message = "【排号】不能为空") String seatRow) {
        this.seatRow = seatRow;
    }

    public @NotBlank(message = "【列号】不能为空") String getSeatCol() {
        return seatCol;
    }

    public void setSeatCol(@NotBlank(message = "【列号】不能为空") String seatCol) {
        this.seatCol = seatCol;
    }

    public @NotBlank(message = "【出发站】不能为空") String getStartStation() {
        return startStation;
    }

    public void setStartStation(@NotBlank(message = "【出发站】不能为空") String startStation) {
        this.startStation = startStation;
    }

    public @NotNull(message = "【出发时间】不能为空") Date getStartTime() {
        return startTime;
    }

    public void setStartTime(@NotNull(message = "【出发时间】不能为空") Date startTime) {
        this.startTime = startTime;
    }

    public @NotBlank(message = "【到达站】不能为空") String getEndStation() {
        return endStation;
    }

    public void setEndStation(@NotBlank(message = "【到达站】不能为空") String endStation) {
        this.endStation = endStation;
    }

    public @NotNull(message = "【到站时间】不能为空") Date getEndTime() {
        return endTime;
    }

    public void setEndTime(@NotNull(message = "【到站时间】不能为空") Date endTime) {
        this.endTime = endTime;
    }

    public @NotBlank(message = "【座位类型】不能为空") String getSeatType() {
        return seatType;
    }

    public void setSeatType(@NotBlank(message = "【座位类型】不能为空") String seatType) {
        this.seatType = seatType;
    }

    @Override
    public String toString() {
        return "UserTicketRequest{" +
                "userId=" + userId +
                ", passengerId=" + passengerId +
                ", passengerName='" + passengerName + '\'' +
                ", trainDate=" + trainDate +
                ", trainCode='" + trainCode + '\'' +
                ", carriageIndex=" + carriageIndex +
                ", seatRow='" + seatRow + '\'' +
                ", seatCol='" + seatCol + '\'' +
                ", startStation='" + startStation + '\'' +
                ", startTime=" + startTime +
                ", endStation='" + endStation + '\'' +
                ", endTime=" + endTime +
                ", seatType='" + seatType + '\'' +
                '}';
    }
}