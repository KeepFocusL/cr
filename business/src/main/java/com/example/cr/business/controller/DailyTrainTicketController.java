package com.example.cr.business.controller;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.business.request.DailyTrainTicketListRequest;
import com.example.cr.business.response.DailyTrainTicketResponse;
import com.example.cr.business.service.DailyTrainTicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.business.request.DailyTrainTicketRequest;
import java.util.List;

@RestController
@RequestMapping("daily-train-ticket")
public class DailyTrainTicketController {
    @Autowired
    DailyTrainTicketService dailyTrainTicketService;

    @GetMapping("list")
    public R<PageResponse<DailyTrainTicketResponse>> list(@Valid DailyTrainTicketListRequest request) {
        PageResponse<DailyTrainTicketResponse> list = dailyTrainTicketService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody DailyTrainTicketRequest request) {
        dailyTrainTicketService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = dailyTrainTicketService.deleteBatch(ids);
        return R.ok(result);
    }
}
