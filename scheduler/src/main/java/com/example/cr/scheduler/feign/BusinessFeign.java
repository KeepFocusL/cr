package com.example.cr.scheduler.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "business", url = "http://localhost:8082")
public interface BusinessFeign {
    @GetMapping("test")
    String test();
}
