package com.example.cr.business.controller.admin;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.business.request.DailyTrainSeatListRequest;
import com.example.cr.business.response.DailyTrainSeatResponse;
import com.example.cr.business.service.DailyTrainSeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.business.request.DailyTrainSeatRequest;
import java.util.List;

@RestController
@RequestMapping("admin/daily-train-seat")
public class DailyTrainSeatAdminController {
    @Autowired
    DailyTrainSeatService dailyTrainSeatService;

    @GetMapping("list")
    public R<PageResponse<DailyTrainSeatResponse>> list(@Valid DailyTrainSeatListRequest request) {
        PageResponse<DailyTrainSeatResponse> list = dailyTrainSeatService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody DailyTrainSeatRequest request) {
        dailyTrainSeatService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = dailyTrainSeatService.deleteBatch(ids);
        return R.ok(result);
    }
}
