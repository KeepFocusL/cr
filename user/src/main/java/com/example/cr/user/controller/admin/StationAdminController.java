package com.example.cr.user.controller.admin;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.user.request.StationListRequest;
import com.example.cr.user.response.StationResponse;
import com.example.cr.user.service.StationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.user.request.StationRequest;
import java.util.List;

@RestController
@RequestMapping("admin/station")
public class StationAdminController {
    @Autowired
    StationService stationService;

    @GetMapping("list")
    public R<PageResponse<StationResponse>> list(@Valid StationListRequest request) {
        PageResponse<StationResponse> list = stationService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody StationRequest request) {
        stationService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = stationService.deleteBatch(ids);
        return R.ok(result);
    }
}
