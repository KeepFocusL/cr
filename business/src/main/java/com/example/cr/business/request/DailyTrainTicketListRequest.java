package com.example.cr.business.request;

import com.example.cr.common.request.PageRequest;

import java.util.Objects;

public class DailyTrainTicketListRequest extends PageRequest {
    /**
     * 搜索关键字
     * 支持搜索的字段是：建表语句中，注释以 "|searchable" 结尾的字段
     */
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "DailyTrainTicketListRequest{" +
                "keyword='" + keyword + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailyTrainTicketListRequest that)) return false;
        return Objects.equals(getKeyword(), that.getKeyword()) && Objects.equals(getPage(), that.getPage()) && Objects.equals(getSize(), that.getSize());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKeyword(), getPage(), getSize());
    }
}
