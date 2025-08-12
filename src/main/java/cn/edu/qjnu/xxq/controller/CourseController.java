package cn.edu.qjnu.xxq.controller;

import cn.edu.qjnu.xxq.entity.Course;
import cn.edu.qjnu.xxq.service.ICourseService;
import cn.edu.qjnu.xxq.util.ElResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 课程控制器（基于course.txt数据适配）
 * </p>
 *
 * @author xxq
 * @since 2025-08-08
 */
@RestController
@RequestMapping("/course")
@CrossOrigin
public class CourseController {
    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private ICourseService courseService;

    /**
     * 分页查询课程
     * @param pageNum 页码
     * @param pageSize 每页条数
     */
    @RequestMapping("/pageCourse")
    public ElResult<Course> pageCourse(Integer pageNum, Integer pageSize) {
        // 处理空参数
        pageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        Page<Course> page = new Page<>(pageNum, pageSize);
        Page<Course> coursePage = courseService.page(page);
        List<Course> records = coursePage.getRecords();
        long total = coursePage.getTotal();

        log.info("分页查询课程：页码={}, 条数={}, 总记录数={}", pageNum, pageSize, total);
        return ElResult.ok(total, records);
    }

    /**
     * 查询所有课程
     */
    @RequestMapping("/listCourse")
    public List<Course> listCourse() {
        List<Course> courseList = courseService.list();
        log.info("查询所有课程，共{}条", courseList.size());
        return courseList;
    }

    /**
     * 根据课程ID查询课程
     * @param courseId 课程ID
     */
    @RequestMapping("/getCourseById")
    public ElResult<Course> getCourseById(Integer courseId) {
        if (courseId == null) {
            return ElResult.error("课程ID不能为空");
        }

        Course course = courseService.getById(courseId);
        if (course != null) {
            log.info("查询课程成功：ID={}", courseId);
            List<Course> courseList = Collections.singletonList(course);
            return ElResult.ok(1L, courseList);
        } else {
            log.warn("查询课程失败：ID={}不存在", courseId);
            return ElResult.error("课程不存在");
        }
    }

    /**
     * 添加课程
     * @param course 课程对象（需包含名称、描述、价格等核心字段）
     */
    @RequestMapping("/addCourse")
    public ElResult<Course> addCourse(Course course) {
        // 基础参数校验
        if (!StringUtils.hasText(course.getTitle())) {
            return ElResult.error("课程名称不能为空");
        }
        if (course.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return ElResult.error("课程价格必须大于0");
        }

        courseService.save(course);
        log.info("新增课程成功：名称={}, ID={}", course.getTitle(), course.getId());
        return ElResult.ok();
    }

    /**
     * 修改课程信息
     * @param course 课程对象（需包含ID及待修改字段）
     */
    @RequestMapping("/updateCourse")
    public ElResult<Course> updateCourse(Course course) {
        if (course == null || course.getId() == null) {
            return ElResult.error("课程ID不能为空");
        }

        courseService.updateById(course);
        log.info("更新课程成功：ID={}", course.getId());
        return ElResult.ok();
    }

    /**
     * 删除课程
     * @param courseId 课程ID
     */
    @RequestMapping("/delCourse")
    public ElResult<Course> delCourse(Integer courseId) {
        if (courseId == null) {
            return ElResult.error("课程ID不能为空");
        }

        courseService.removeById(courseId);
        log.info("删除课程成功：ID={}", courseId);
        return ElResult.ok();
    }
}