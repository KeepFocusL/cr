package com.example.cr.${module}.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.common.response.PageResponse;
import com.example.cr.${module}.entity.${Domain};
import com.example.cr.${module}.entity.${Domain}Example;
import com.example.cr.${module}.mapper.${Domain}Mapper;
import com.example.cr.${module}.request.${Domain}ListRequest;
import com.example.cr.${module}.response.${Domain}Response;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
<#if !readOnly>
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.cr.common.util.SnowflakeUtil;
import com.example.cr.${module}.request.${Domain}Request;
</#if>

@Service
public class ${Domain}Service {
    private static final Logger log = LoggerFactory.getLogger(${Domain}Service.class);
    @Autowired
    ${Domain}Mapper ${domain}Mapper;

    public PageResponse<${Domain}Response> list(${Domain}ListRequest request) {
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        ${domain}Example.setOrderByClause("id desc");

        // 处理搜索条件
        ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            <#assign counter = 0>
            <#list fieldList as field>
                <#if field.searchable>
                    <#assign counter = counter + 1>
                    <#if counter == 1>
            criteria.and${field.nameUpperCamelCase}Like(keyword);
                    <#else>
            ${Domain}Example.Criteria criteria${counter} = ${domain}Example.createCriteria();
            criteria${counter}.and${field.nameUpperCamelCase}Like(keyword);
            ${domain}Example.or(criteria${counter});
                    </#if>
                </#if>
            </#list>
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<${Domain}> ${domain}s = ${domain}Mapper.selectByExample(${domain}Example);

        PageInfo<${Domain}> pageInfo = new PageInfo<>(${domain}s);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<${Domain}Response> list = BeanUtil.copyToList(${domain}s, ${Domain}Response.class);

        PageResponse<${Domain}Response> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
<#if !readOnly>
    public void save(${Domain}Request request) {
        ${Domain} ${domain} = BeanUtil.copyProperties(request, ${Domain}.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(${domain}.getId()) || ${domain}.getId() == 0L) {
            ${domain}.setId(SnowflakeUtil.getId());
            ${domain}.setCreatedAt(now);
            ${domain}.setUpdatedAt(now);
            ${domain}Mapper.insert(${domain});
        } else {
            ${domain}.setUpdatedAt(now);
            ${domain}Mapper.updateByPrimaryKeySelective(${domain});
        }
    }

    public int deleteBatch(List<Long> ids) {
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();
        criteria.andIdIn(ids);
        return ${domain}Mapper.deleteByExample(${domain}Example);
    }
</#if>
}
