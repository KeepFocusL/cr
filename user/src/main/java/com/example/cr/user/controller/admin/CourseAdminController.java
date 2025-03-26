package com.example.cr.user.controller.admin;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.user.request.CourseListRequest;
import com.example.cr.user.response.CourseResponse;
import com.example.cr.user.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.cr.user.request.CourseRequest;
import java.util.List;

@RestController
@RequestMapping("admin/course")
public class CourseAdminController {
    @Autowired
    CourseService courseService;

    @GetMapping("list")
    public R<PageResponse<CourseResponse>> list(@Valid CourseListRequest request) {
        PageResponse<CourseResponse> list = courseService.list(request);
        return R.ok(list);
    }
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody CourseRequest request) {
        courseService.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = courseService.deleteBatch(ids);
        return R.ok(result);
    }
}
