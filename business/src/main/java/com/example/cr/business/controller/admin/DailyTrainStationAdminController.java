package com.example.cr.business.controller.admin;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.business.request.DailyTrainStationListRequest;
import com.example.cr.business.response.DailyTrainStationResponse;
import com.example.cr.business.service.DailyTrainStationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.business.request.DailyTrainStationRequest;
import java.util.List;

@RestController
@RequestMapping("admin/daily-train-station")
public class DailyTrainStationAdminController {
    @Autowired
    DailyTrainStationService dailyTrainStationService;

    @GetMapping("list")
    public R<PageResponse<DailyTrainStationResponse>> list(@Valid DailyTrainStationListRequest request) {
        PageResponse<DailyTrainStationResponse> list = dailyTrainStationService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody DailyTrainStationRequest request) {
        dailyTrainStationService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = dailyTrainStationService.deleteBatch(ids);
        return R.ok(result);
    }
}
