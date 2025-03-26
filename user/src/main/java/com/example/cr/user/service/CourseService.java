package com.example.cr.user.service;

import cn.hutool.core.bean.BeanUtil;
import com.example.cr.common.response.PageResponse;
import com.example.cr.user.entity.Course;
import com.example.cr.user.entity.CourseExample;
import com.example.cr.user.mapper.CourseMapper;
import com.example.cr.user.request.CourseListRequest;
import com.example.cr.user.response.CourseResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.cr.common.util.SnowflakeUtil;
import com.example.cr.user.request.CourseRequest;

@Service
public class CourseService {
    private static final Logger log = LoggerFactory.getLogger(CourseService.class);
    @Autowired
    CourseMapper courseMapper;

    public PageResponse<CourseResponse> list(CourseListRequest request) {
        CourseExample courseExample = new CourseExample();
        courseExample.setOrderByClause("id desc");

        // 处理搜索条件
        CourseExample.Criteria criteria = courseExample.createCriteria();
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            criteria.andNameLike(keyword);
            CourseExample.Criteria criteria2 = courseExample.createCriteria();
            criteria2.andDescriptionLike(keyword);
            courseExample.or(criteria2);
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        log.info("当前页码 current_page = {}", request.getPage());
        log.info("每页记录数 per_page = {}", request.getSize());

        List<Course> courses = courseMapper.selectByExample(courseExample);

        PageInfo<Course> pageInfo = new PageInfo<>(courses);
        log.info("总记录数 total = {}", pageInfo.getTotal());
        log.info("总页数 pages = {}", pageInfo.getPages());

        List<CourseResponse> list = BeanUtil.copyToList(courses, CourseResponse.class);

        PageResponse<CourseResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);

        return pageResponse;
    }
    public void save(CourseRequest request) {
        Course course = BeanUtil.copyProperties(request, Course.class);
        DateTime now = DateTime.now();
        if (ObjectUtil.isNull(course.getId()) || course.getId() == 0L) {
            course.setId(SnowflakeUtil.getId());
            course.setCreatedAt(now);
            course.setUpdatedAt(now);
            courseMapper.insert(course);
        } else {
            course.setUpdatedAt(now);
            courseMapper.updateByPrimaryKeySelective(course);
        }
    }

    public int deleteBatch(List<Long> ids) {
        CourseExample courseExample = new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andIdIn(ids);
        return courseMapper.deleteByExample(courseExample);
    }
}
