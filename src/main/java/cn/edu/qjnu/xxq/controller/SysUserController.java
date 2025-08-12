package cn.edu.qjnu.xxq.controller;

import cn.edu.qjnu.xxq.entity.SysRole;
import cn.edu.qjnu.xxq.entity.SysUser;
import cn.edu.qjnu.xxq.entity.SysUserRoleMerged;
import cn.edu.qjnu.xxq.service.ISysRoleService;
import cn.edu.qjnu.xxq.service.ISysUserRoleMergedService;
import cn.edu.qjnu.xxq.service.ISysUserService;
import cn.edu.qjnu.xxq.util.ElResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>
 * 系统用户控制器（基于sys_user.txt数据适配）
 * </p>
 *
 * @author xxq
 * @since 2025-07-29
 */
@RestController
@RequestMapping("/sysUser")
@CrossOrigin
public class SysUserController {
    private static final Logger log = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysUserRoleMergedService sysUserRoleMergedService;

    /**
     * 分页查询用户
     * @param pageNum 页码
     * @param pageSize 每页条数
     */
    @RequestMapping("/pageUser")
    public ElResult<SysUser> pageUser(Integer pageNum, Integer pageSize) {
        // 处理空参数
        pageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        Page<SysUser> page = new Page<>(pageNum, pageSize);
        Page<SysUser> userPage = sysUserService.page(page);
        List<SysUser> records = userPage.getRecords();
        for(SysUser sysUser: records){
            Integer id = sysUser.getId();
            QueryWrapper<SysUserRoleMerged> qw = new QueryWrapper<>();
            qw.eq("user_id", id);
            List<SysUserRoleMerged> list = sysUserRoleMergedService.list(qw);
            if(list.size()>0){
                Integer roleId = list.get(0).getRoleId();
                SysRole sysRole = sysRoleService.getById(roleId);
                sysUser.setRoleName(sysRole.getName());
                sysUser.setRoleId(roleId);
            }
        }
        long total = userPage.getTotal();

        log.info("分页查询用户：页码={}, 条数={}, 总记录数={}", pageNum, pageSize, total);
        return ElResult.ok(total, records);
    }

    /**
     * 查询所有用户
     */
    @RequestMapping("/listUser")
    public List<SysUser> listUser() {
        List<SysUser> userList = sysUserService.list();
        log.info("查询所有用户，共{}条", userList.size());
        return userList;
    }

    /**
     * 用户登录
     * 例如：用户"李留学"的密码"3333"加密后匹配数据库
     */
    @RequestMapping("/login")
    public ElResult<SysUser> login(SysUser sysUser) {
        // 校验参数
        if (sysUser == null || !StringUtils.hasText(sysUser.getName())
                || !StringUtils.hasText(sysUser.getPwd())) {
            return ElResult.error("用户名或密码不能为空");
        }

        // 加密密码
        String encryptedPwd = DigestUtils.md5DigestAsHex(
                sysUser.getPwd().getBytes(StandardCharsets.UTF_8));
        sysUser.setPwd(encryptedPwd);

        QueryWrapper<SysUser> qw = new QueryWrapper<>(sysUser);
        List<SysUser> users = sysUserService.list(qw);

        if (users.size() > 0) {
            log.info("用户登录成功：{}", sysUser.getName());
            return ElResult.ok(0L, users);
        } else {
            log.warn("登录失败：用户名或密码错误", sysUser.getName());
            return ElResult.error("用户名或密码错误（例如：李留学/3333）");
        }
    }

    /**
     * 修改用户
     */
    @RequestMapping("/updateUser")
    public ElResult<SysUser> updateUser(SysUser sysUser) {
        if (sysUser == null || sysUser.getId() == null) {
            return ElResult.error("用户ID不能为空");
        }
        // 若修改密码，重新加密
        if (StringUtils.hasText(sysUser.getPwd())) {
            sysUser.setPwd(DigestUtils.md5DigestAsHex(
                    sysUser.getPwd().getBytes(StandardCharsets.UTF_8)));
        }

        QueryWrapper<SysUserRoleMerged> qw = new QueryWrapper<>();
        qw.eq("user_id", sysUser.getId());
        List<SysUserRoleMerged> list = sysUserRoleMergedService.list(qw);
        if(list.size()>0){
            SysUserRoleMerged urm = list.get(0);
            urm.setRoleId(sysUser.getRoleId());
            sysUserRoleMergedService.saveOrUpdate(urm);
        }
        sysUserService.updateById(sysUser);
        log.info("更新用户成功：ID={}", sysUser.getId());
        return ElResult.ok();
    }

    /**
     * 添加用户
     */
    @RequestMapping("/addUser")
    public ElResult<SysUser> addUser(SysUser sysUser) {
        if (!StringUtils.hasText(sysUser.getPwd())) {
            return ElResult.error("密码不能为空");
        }
        // 加密新用户密码
        sysUser.setPwd(DigestUtils.md5DigestAsHex(
                sysUser.getPwd().getBytes(StandardCharsets.UTF_8)));


        sysUserService.save(sysUser);
        SysUserRoleMerged urm = new SysUserRoleMerged();
        urm.setUserId(sysUser.getId());
        urm.setRoleId(sysUser.getRoleId());
        sysUserRoleMergedService.saveOrUpdate(urm);
        log.info("新增用户成功：{}", sysUser.getName());
        return ElResult.ok();
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delUser")
    public ElResult<SysUser> delUser(Integer suId) {
        if (suId == null) {
            return ElResult.error("用户ID不能为空");
        }
        sysUserService.removeById(suId);
        log.info("删除用户成功：ID={}", suId);
        return ElResult.ok();
    }

    /**
     * 测试用户密码的加密结果
     */
    public static void main(String[] args) {
        // 输出所有密码的加密值，方便数据库初始化
        String[] passwords = {"1111", "2222", "3333", "4444", "5555"};
        for (String pwd : passwords) {
            String encrypted = DigestUtils.md5DigestAsHex(pwd.getBytes());
            System.out.println("明文密码" + pwd + "的加密值：" + encrypted);
        }
    }
}