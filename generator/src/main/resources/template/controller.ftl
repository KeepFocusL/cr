package com.example.cr.${module}.controller<#if isAdmin>.admin</#if>;

import com.example.cr.common.response.PageResponse;
import com.example.cr.common.response.R;
import com.example.cr.${module}.request.${Domain}ListRequest;
import com.example.cr.${module}.response.${Domain}Response;
import com.example.cr.${module}.service.${Domain}Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
<#if !readOnly>
import com.example.cr.${module}.request.${Domain}Request;
import java.util.List;
</#if>

@RestController
@RequestMapping("<#if isAdmin>admin/</#if>${do_main}")
public class ${Domain}<#if isAdmin>Admin</#if>Controller {
    @Autowired
    ${Domain}Service ${domain}Service;

    @GetMapping("list")
    public R<PageResponse<${Domain}Response>> list(@Valid ${Domain}ListRequest request) {
        PageResponse<${Domain}Response> list = ${domain}Service.list(request);
        return R.ok(list);
    }
<#if !readOnly>
    @PostMapping("save")
    public R<Object> save(@Valid @RequestBody ${Domain}Request request) {
        ${domain}Service.save(request);
        return R.ok();
    }

    @DeleteMapping("delete")
    public R<Integer> delete(@RequestBody List<Long> ids) {
        int result = ${domain}Service.deleteBatch(ids);
        return R.ok(result);
    }
</#if>
}
