package com.example.cr.user.controller;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.user.request.PassengerListRequest;
import com.example.cr.user.response.PassengerResponse;
import com.example.cr.user.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.user.request.PassengerRequest;
import java.util.List;

@RestController
@RequestMapping("passenger")
public class PassengerController {
    @Autowired
    PassengerService passengerService;

    @GetMapping("list")
    public R<PageResponse<PassengerResponse>> list(@Valid PassengerListRequest request) {
        PageResponse<PassengerResponse> list = passengerService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody PassengerRequest request) {
        passengerService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = passengerService.deleteBatch(ids);
        return R.ok(result);
    }
}
