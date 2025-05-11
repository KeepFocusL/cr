package com.example.cr.user.request;

import com.example.cr.common.request.PageRequest;

public class TicketListRequest extends PageRequest {
    /**
     * 搜索关键字
     * 支持搜索的字段是：建表语句中，注释以 "|searchable" 结尾的字段
     */
    private String keyword;

    /**
     * 当前登录的会员id
     */
    Long userId;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TicketListRequest{" +
                "keyword='" + keyword + '\'' +
                '}';
    }
}
