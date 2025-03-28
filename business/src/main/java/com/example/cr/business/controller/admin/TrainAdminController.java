package com.example.cr.business.controller.admin;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.business.request.TrainListRequest;
import com.example.cr.business.response.TrainResponse;
import com.example.cr.business.service.TrainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.business.request.TrainRequest;
import java.util.List;

@RestController
@RequestMapping("admin/train")
public class TrainAdminController {
    @Autowired
    TrainService trainService;

    @GetMapping("list")
    public R<PageResponse<TrainResponse>> list(@Valid TrainListRequest request) {
        PageResponse<TrainResponse> list = trainService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody TrainRequest request) {
        trainService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = trainService.deleteBatch(ids);
        return R.ok(result);
    }
}
