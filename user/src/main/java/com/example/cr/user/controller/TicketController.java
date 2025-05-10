package com.example.cr.user.controller;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.user.request.TicketListRequest;
import com.example.cr.user.response.TicketResponse;
import com.example.cr.user.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ticket")
public class TicketController {
    @Autowired
    TicketService ticketService;

    @GetMapping("list")
    public R<PageResponse<TicketResponse>> list(@Valid TicketListRequest request) {
        PageResponse<TicketResponse> list = ticketService.list(request);
        return R.ok(list);
    }
}
