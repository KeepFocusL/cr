package com.example.cr.business.feign;

import com.example.cr.common.request.UserTicketRequest;
import com.example.cr.common.response.R;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user")
public interface UserFeign {

    @PostMapping("/feign/ticket/save")
    public R<Object> save(@Valid @RequestBody UserTicketRequest request);
}
