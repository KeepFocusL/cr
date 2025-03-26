package com.example.cr.user.controller.admin;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.user.request.SbListRequest;
import com.example.cr.user.response.SbResponse;
import com.example.cr.user.service.SbService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.user.request.SbRequest;
import java.util.List;

@RestController
@RequestMapping("admin/sb")
public class SbAdminController {
    @Autowired
    SbService sbService;

    @GetMapping("list")
    public R<PageResponse<SbResponse>> list(@Valid SbListRequest request) {
        PageResponse<SbResponse> list = sbService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody SbRequest request) {
        sbService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = sbService.deleteBatch(ids);
        return R.ok(result);
    }
}
