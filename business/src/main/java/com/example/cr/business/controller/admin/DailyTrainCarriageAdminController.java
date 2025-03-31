package com.example.cr.business.controller.admin;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.business.request.DailyTrainCarriageListRequest;
import com.example.cr.business.response.DailyTrainCarriageResponse;
import com.example.cr.business.service.DailyTrainCarriageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.business.request.DailyTrainCarriageRequest;
import java.util.List;

@RestController
@RequestMapping("admin/daily-train-carriage")
public class DailyTrainCarriageAdminController {
    @Autowired
    DailyTrainCarriageService dailyTrainCarriageService;

    @GetMapping("list")
    public R<PageResponse<DailyTrainCarriageResponse>> list(@Valid DailyTrainCarriageListRequest request) {
        PageResponse<DailyTrainCarriageResponse> list = dailyTrainCarriageService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody DailyTrainCarriageRequest request) {
        dailyTrainCarriageService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = dailyTrainCarriageService.deleteBatch(ids);
        return R.ok(result);
    }
}
