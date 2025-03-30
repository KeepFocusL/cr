package com.example.cr.business.controller.admin;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.business.request.TrainCarriageListRequest;
import com.example.cr.business.response.TrainCarriageResponse;
import com.example.cr.business.service.TrainCarriageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.business.request.TrainCarriageRequest;
import java.util.List;

@RestController
@RequestMapping("admin/train-carriage")
public class TrainCarriageAdminController {
    @Autowired
    TrainCarriageService trainCarriageService;

    @GetMapping("list")
    public R<PageResponse<TrainCarriageResponse>> list(@Valid TrainCarriageListRequest request) {
        PageResponse<TrainCarriageResponse> list = trainCarriageService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody TrainCarriageRequest request) {
        trainCarriageService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = trainCarriageService.deleteBatch(ids);
        return R.ok(result);
    }
}
