package cn.edu.qjnu.xxq.controller;

import cn.edu.qjnu.xxq.entity.Teacher;
import cn.edu.qjnu.xxq.service.ITeacherService;
import cn.edu.qjnu.xxq.util.ElResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 教师信息控制器
 * </p>
 *
 * @author xxq
 * @since 2025-08-06
 */
@RestController
@RequestMapping("/teacher")
@CrossOrigin
public class TeacherController {
    private static final Logger log = LoggerFactory.getLogger(TeacherController.class);

    @Autowired    //自动给变量赋值
    private ITeacherService teacherService;


    /**
     * 分页查询教师信息
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果（总记录数+当前页数据）
     */
    @RequestMapping("/pageTeacher")
    public ElResult<Teacher> pageTeacher(Integer pageNum, Integer pageSize) {
        // 页码与每页条数默认值处理
        pageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        //分页查询的参数
        Page<Teacher> page = new Page<>(pageNum, pageSize);
        //查询数据库，返回一个paga类型对象
        IPage<Teacher> PageResult = teacherService.page(page);
        List<Teacher> records = PageResult.getRecords();
//        循环设置每一个的类别名称

        long total = PageResult.getTotal();

        log.info("分页查询教师：页码={}, 条数={}, 总记录数={}", pageNum, pageSize, total);
        return ElResult.ok(total, records);
    }

    /**
     * 查询所有教师信息
     * @return 教师列表
     */
    @RequestMapping("/listTeacher")
    public List<Teacher> listTeacher() {
        List<Teacher> teacherList = teacherService.list();
        log.info("查询所有教师，共{}条", teacherList.size());
        return teacherList;
    }

    /**
     * 修改教师信息
     * @param teacher 教师实体（需包含有效ID）
     * @return 操作结果
     */
    @RequestMapping("/updateTeacher")
    public ElResult<Teacher> updateTeacher(Teacher teacher) {
        if (teacher == null || teacher.getId() == null) {
            return ElResult.error("教师ID不能为空");
        }
        teacherService.updateById(teacher);
        log.info("更新教师成功：ID={}", teacher.getId());
        return ElResult.ok();
    }

    /**
     * 添加教师信息
     * @param teacher 教师实体（需包含姓名信息）
     * @return 操作结果
     */
    @RequestMapping("/addTeacher")
    public ElResult<Teacher> addTeacher(Teacher teacher) {
        // 只校验姓名不为空（移除岗位校验）
        if (!StringUtils.hasText(teacher.getName())) {
            return ElResult.error("教师姓名不能为空");
        }
        teacherService.save(teacher);
        log.info("新增教师成功：{}", teacher.getName());
        return ElResult.ok();
    }

    /**
     * 删除教师信息
     * @param id 教师ID
     * @return 操作结果
     */
    @RequestMapping("/delTeacher")
    public ElResult<Teacher> delTeacher(Integer id) {
        if (id == null) {
            return ElResult.error("教师ID不能为空");
        }
//        删除数据
        teacherService.removeById(id);
        log.info("删除教师成功：ID={}", id);
        return ElResult.ok();
    }
}