package com.example.cr.user.controller.feign;


import com.example.cr.common.request.UserTicketRequest;
import com.example.cr.common.response.R;
import com.example.cr.user.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign/ticket")
public class FeignTicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/save")
    public R<Object> save(@Valid @RequestBody UserTicketRequest request) {
        ticketService.save(request);
        return R.ok();
    }

}