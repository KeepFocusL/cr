package com.example.cr.business.request;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DailyTrainSeatRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @NotNull(message = "【日期】不能为空")
    private Date date;

    /**
     * 车次编号
     */
    @NotBlank(message = "【车次编号】不能为空")
    @Size(max = 20)
    private String trainCode;

    /**
     * 厢序
     */
    @NotNull(message = "【厢序】不能为空")
    private Integer carriageIndex;

    /**
     * 排号
     */
    @NotBlank(message = "【排号】不能为空")
    private String row;

    /**
     * 列号
     */
    @NotBlank(message = "【列号】不能为空")
    private String col;

    /**
     * 座位类型
     */
    @NotBlank(message = "【座位类型】不能为空")
    private String seatType;

    /**
     * 同车厢座序
     */
    @NotNull(message = "【同车厢座序】不能为空")
    private Integer carriageSeatIndex;

    /**
     * 售卖情况
     */
    @NotBlank(message = "【售卖情况】不能为空")
    @Size(max = 50)
    private String sell;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updatedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public Integer getCarriageIndex() {
        return carriageIndex;
    }

    public void setCarriageIndex(Integer carriageIndex) {
        this.carriageIndex = carriageIndex;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public Integer getCarriageSeatIndex() {
        return carriageSeatIndex;
    }

    public void setCarriageSeatIndex(Integer carriageSeatIndex) {
        this.carriageSeatIndex = carriageSeatIndex;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", date=").append(date);
        sb.append(", trainCode=").append(trainCode);
        sb.append(", carriageIndex=").append(carriageIndex);
        sb.append(", row=").append(row);
        sb.append(", col=").append(col);
        sb.append(", seatType=").append(seatType);
        sb.append(", carriageSeatIndex=").append(carriageSeatIndex);
        sb.append(", sell=").append(sell);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}