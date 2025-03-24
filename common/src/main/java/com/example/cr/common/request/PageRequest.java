package com.example.cr.common.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class PageRequest {
    @Min(value = 1, message = "当前页码不能小于1")
    private Integer page;

    @Max(value = 100, message = "每页不能超过100条")
    private Integer size;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
