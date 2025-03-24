package com.example.cr.common.response;

import java.util.List;

public class PageResponse<T> {
    // 总记录数
    private Long total;

    // 当前页码的具体数据
    private List<T> list;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
