package com.example.cr.business.controller.admin;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.business.request.TrainStationListRequest;
import com.example.cr.business.response.TrainStationResponse;
import com.example.cr.business.service.TrainStationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.business.request.TrainStationRequest;
import java.util.List;

@RestController
@RequestMapping("admin/train-station")
public class TrainStationAdminController {
    @Autowired
    TrainStationService trainStationService;

    @GetMapping("list")
    public R<PageResponse<TrainStationResponse>> list(@Valid TrainStationListRequest request) {
        PageResponse<TrainStationResponse> list = trainStationService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody TrainStationRequest request) {
        trainStationService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = trainStationService.deleteBatch(ids);
        return R.ok(result);
    }
}
