package com.example.cr.scheduler.feign;

import com.example.cr.common.response.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@FeignClient(name = "business", url = "http://localhost:8082")
public interface BusinessFeign {
    @GetMapping("test")
    String test();

    @GetMapping("admin/daily-train/gen-daily/{date}")
    R<Object> genDaily(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);
}
