package com.example.cr.business.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ConfirmOrderTicketRequest {
    /**
     * 乘客ID
     */
    @NotNull(message = "【乘客ID】不能为空")
    private Long passengerId;

    /**
     * 乘客票种
     */
    @NotBlank(message = "【乘客票种】不能为空")
    private String passengerType;

    /**
     * 乘客名称
     */
    @NotBlank(message = "【乘客名称】不能为空")
    private String passengerName;

    /**
     * 乘客身份证
     */
    @NotBlank(message = "【乘客身份证】不能为空")
    private String passengerIdCard;

    /**
     * 座位类型code
     */
    @NotBlank(message = "【座位类型code】不能为空")
    private String seatTypeCode;

    /**
     * 选座，可空，值示例：A1
     */
    private String seat;

    public @NotNull(message = "【乘客ID】不能为空") Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(@NotNull(message = "【乘客ID】不能为空") Long passengerId) {
        this.passengerId = passengerId;
    }

    public @NotBlank(message = "【乘客票种】不能为空") String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(@NotBlank(message = "【乘客票种】不能为空") String passengerType) {
        this.passengerType = passengerType;
    }

    public @NotBlank(message = "【乘客名称】不能为空") String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(@NotBlank(message = "【乘客名称】不能为空") String passengerName) {
        this.passengerName = passengerName;
    }

    public @NotBlank(message = "【乘客身份证】不能为空") String getPassengerIdCard() {
        return passengerIdCard;
    }

    public void setPassengerIdCard(@NotBlank(message = "【乘客身份证】不能为空") String passengerIdCard) {
        this.passengerIdCard = passengerIdCard;
    }

    public @NotBlank(message = "【座位类型code】不能为空") String getSeatTypeCode() {
        return seatTypeCode;
    }

    public void setSeatTypeCode(@NotBlank(message = "【座位类型code】不能为空") String seatTypeCode) {
        this.seatTypeCode = seatTypeCode;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "ConfirmOrderTicketRequest{" +
                "passengerId=" + passengerId +
                ", passengerType='" + passengerType + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", passengerIdCard='" + passengerIdCard + '\'' +
                ", seatTypeCode='" + seatTypeCode + '\'' +
                ", seat='" + seat + '\'' +
                '}';
    }
}
