package com.example.cr.business.controller.admin;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.business.request.ConfirmOrderListRequest;
import com.example.cr.business.response.ConfirmOrderResponse;
import com.example.cr.business.service.ConfirmOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.business.request.ConfirmOrderRequest;
import java.util.List;

@RestController
@RequestMapping("admin/confirm-order")
public class ConfirmOrderAdminController {
    @Autowired
    ConfirmOrderService confirmOrderService;

    @GetMapping("list")
    public R<PageResponse<ConfirmOrderResponse>> list(@Valid ConfirmOrderListRequest request) {
        PageResponse<ConfirmOrderResponse> list = confirmOrderService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody ConfirmOrderRequest request) {
        confirmOrderService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = confirmOrderService.deleteBatch(ids);
        return R.ok(result);
    }
}
