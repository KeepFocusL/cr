package com.example.cr.business.controller.admin;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.business.request.DailyTrainListRequest;
import com.example.cr.business.response.DailyTrainResponse;
import com.example.cr.business.service.DailyTrainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.business.request.DailyTrainRequest;
import java.util.List;

@RestController
@RequestMapping("admin/daily-train")
public class DailyTrainAdminController {
    @Autowired
    DailyTrainService dailyTrainService;

    @GetMapping("list")
    public R<PageResponse<DailyTrainResponse>> list(@Valid DailyTrainListRequest request) {
        PageResponse<DailyTrainResponse> list = dailyTrainService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody DailyTrainRequest request) {
        dailyTrainService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = dailyTrainService.deleteBatch(ids);
        return R.ok(result);
    }
}
