package cn.edu.qjnu.xxq.controller;

import cn.edu.qjnu.xxq.entity.SysUser;
import cn.edu.qjnu.xxq.service.ISysUserService;
import cn.edu.qjnu.xxq.util.ElResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xxq
 * @since 2025-07-29
 */
@RestController
@RequestMapping("/sysUser")
@CrossOrigin
public class SysUserController {
    @Autowired
    private ISysUserService sysUserService;



    @RequestMapping("/pageUser")
//    pageNum: 页码；  pageSize: 每页行数。  参数名称要跟前端请求的参数名一致
    public ElResult<SysUser> pageUser(Integer pageNum, Integer pageSize){
//        分页查询的参数
        Page<SysUser> page = new Page<>(pageNum, pageSize);
//        查询数据库，返回一个Page类型对象
        Page<SysUser> userPage = sysUserService.page(page);
//        从返回对象获取当前页的数据列表
        List<SysUser> records = userPage.getRecords();
//        从返回对象获取这个表的总的记录的数量
        long total = userPage.getTotal();
        return ElResult.ok(total, records);
    }

    @RequestMapping("/listUser")
    public List<SysUser> listUser(){
        return sysUserService.list();
    }

    @RequestMapping("/login")
    public ElResult<SysUser> login(SysUser sysUser){
//         创建查询条件
        sysUser.setSuPwd(DigestUtils.md5DigestAsHex(sysUser.getSuPwd().getBytes()));
        QueryWrapper<SysUser> qw = new QueryWrapper<>(sysUser);
//        根据条件（用户名和密码）从数据库查询用户
        List<SysUser> users = sysUserService.list(qw);
        if(users.size() > 0){
            return ElResult.ok(0L, users);
        }else {
            return ElResult.error("用户名或密码错误");
        }
    }

    @RequestMapping("/updateUser")
    public ElResult<SysUser> updateUser(SysUser sysUser){
//        修改数据
        sysUserService.updateById(sysUser);
        return ElResult.ok();
    }

    @RequestMapping("/addUser")
    public ElResult<SysUser> addUser(SysUser sysUser){
        //        对明文密码进行加密
        sysUser.setSuPwd(DigestUtils.md5DigestAsHex(sysUser.getSuPwd().getBytes()));
//        修改数据
        sysUserService.save(sysUser);
        return ElResult.ok();
    }

    @RequestMapping("/delUser")
    public ElResult<SysUser> delUser(Integer suId){
//        删除数据
        sysUserService.removeById(suId);
        return ElResult.ok();
    }

    public static void main(String[] args) {
        String pwd = "3333";
        pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());
        System.out.println(pwd);
    }
    }


