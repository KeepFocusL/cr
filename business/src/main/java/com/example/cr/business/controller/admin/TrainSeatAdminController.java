package com.example.cr.business.controller.admin;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.business.request.TrainSeatListRequest;
import com.example.cr.business.response.TrainSeatResponse;
import com.example.cr.business.service.TrainSeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.business.request.TrainSeatRequest;
import java.util.List;

@RestController
@RequestMapping("admin/train-seat")
public class TrainSeatAdminController {
    @Autowired
    TrainSeatService trainSeatService;

    @GetMapping("list")
    public R<PageResponse<TrainSeatResponse>> list(@Valid TrainSeatListRequest request) {
        PageResponse<TrainSeatResponse> list = trainSeatService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody TrainSeatRequest request) {
        trainSeatService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = trainSeatService.deleteBatch(ids);
        return R.ok(result);
    }
}
