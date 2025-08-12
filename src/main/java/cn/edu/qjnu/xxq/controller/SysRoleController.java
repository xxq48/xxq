package cn.edu.qjnu.xxq.controller;

import cn.edu.qjnu.xxq.entity.SysRole;
import cn.edu.qjnu.xxq.service.ISysRoleService;
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

import java.util.List;

/**
 * <p>
 * 系统角色控制器
 * </p>
 *
 * @author xxq
 * @since 2025-08-05
 */
@RestController
@RequestMapping("/sysRole")
@CrossOrigin
public class SysRoleController {
    private static final Logger log = LoggerFactory.getLogger(SysRoleController.class);

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 分页查询角色
     * @param pageNum 页码
     * @param pageSize 每页条数
     */
    @RequestMapping("/pageRole")
//    pageNum:页码    pageSize:每页行数 。   参数名称要跟前端请求的参数名一致
    public ElResult<SysRole> pageRole(Integer pageNum, Integer pageSize) {
//        分页查询的参数
        pageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        Page<SysRole> page = new Page<>(pageNum, pageSize);
//        查询数据库，返回一个page类型对象
        Page<SysRole> rolePage = sysRoleService.page(page);
//        从返回对象获取当前页的数据列表
        List<SysRole> records = rolePage.getRecords();
//        循环设置每一个的类别名称
        for(SysRole sysRole:records){
            Integer categoryIdm = sysRole.getRoleId(); // 获取分类 ID
            // 根据分类 ID 查询分类信息（假设返回 SysCategory 包含名称属性）
            SysRole category = sysRoleService.getById(categoryIdm);
            if (category != null) { // 避免空指针，查询到分类才设置
                sysRole.setName(category.getName());  // 设置分类名称到 SysRole 对象
            }

        }


        long total = rolePage.getTotal();
        log.info("分页查询角色：页码={}, 条数={}, 总记录数={}", pageNum, pageSize, total);
        return ElResult.ok(total, records);
    }

    /**
     * 查询所有角色
     */
    @RequestMapping("/listRole")
    public List<SysRole> listRole() {
        List<SysRole> roleList = sysRoleService.list();
        log.info("查询所有角色，共{}条", roleList.size());
        return roleList;
    }

    public ISysRoleService getSysRoleService() {
        return sysRoleService;
    }

    public void setSysRoleService(ISysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    /**
     * 修改角色
     */
    @RequestMapping("/updateRole")
    public ElResult<SysRole> updateRole(SysRole sysRole) {
        if (sysRole == null || sysRole.getRoleId() == null) {
            return ElResult.error("角色ID不能为空");
        }
        sysRoleService.updateById(sysRole);
        log.info("更新角色成功：ID={}", sysRole.getRoleId());
        return ElResult.ok();
    }

    /**
     * 添加角色
     */
    @RequestMapping("/addRole")
    public ElResult<SysRole> addRole(SysRole sysRole) {
//        if (!StringUtils.hasText(sysRole.getName()) || !StringUtils.hasText(sysRole.getCode())) {
//            return ElResult.error("角色名称和角色编码不能为空");
//        }
        sysRoleService.save(sysRole);
        log.info("新增角色成功：{}）", sysRole.getName());
        return ElResult.ok();
    }

    /**
     * 删除角色
     */
    @RequestMapping("/delRole")
    public ElResult<SysRole> delRole(Integer roleId) {
        if (roleId == null) {
            return ElResult.error("角色ID不能为空");
        }
        sysRoleService.removeById(roleId);
        log.info("删除角色成功：ID={}", roleId);
        return ElResult.ok();
    }

    /**
     * 测试角色数据查询
     */
    public static void main(String[] args) {
        // 示例：输出txt中角色ID与编码的对应关系
        System.out.println("角色ID与编码对应关系");
        System.out.println("1 -> ROLE_SUPER_ADMIN");
        System.out.println("2 -> ROLE_CONTENT_OP");
        System.out.println("3 -> ROLE_CONSULTANT");
        System.out.println("4 -> ROLE_TEACHER");
        System.out.println("5 -> ROLE_USER");
    }
}