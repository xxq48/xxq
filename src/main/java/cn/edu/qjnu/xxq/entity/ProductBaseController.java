package cn.edu.qjnu.hqyj.controller;

import cn.edu.qjnu.hqyj.entity.ProductBase;
import cn.edu.qjnu.hqyj.entity.SysUser;
import cn.edu.qjnu.hqyj.service.IProductBaseService;
import cn.edu.qjnu.hqyj.service.ISysUserService;
import cn.edu.qjnu.hqyj.util.ElResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品基础信息表 前端控制器
 * </p>
 *
 * @author HQYJ
 * @since 2025-08-04
 */
@RestController
@RequestMapping("/productBase")
@CrossOrigin   //允许跨域请求
public class ProductBaseController {
    @Autowired
    private IProductBaseService productBaseService;

    @RequestMapping("/pageProductBase")
//    pageNum: 页码；  pageSize: 每页行数。  参数名称要跟前端请求的参数名一致
    public ElResult<ProductBase> pageProductBase(Integer pageNum, Integer pageSize){
//        分页查询的参数
        Page<ProductBase> page = new Page<>(pageNum, pageSize);
//        查询数据库，返回一个Page类型对象
        Page<ProductBase> paroductBasePage = productBaseService.page(page);
//        从返回对象获取当前页的数据列表
        List<ProductBase> records = paroductBasePage.getRecords();
//        从返回对象获取这个表的总的记录的数量
        long total = paroductBasePage.getTotal();
        return ElResult.ok(total, records);
    }

    @RequestMapping("/updateProductBase")
    public ElResult<ProductBase> updateProductBase(ProductBase productBase){
        // 把更新时间设置为当前时间
        productBase.setUpdateTime(LocalDateTime.now());
//        修改数据
        productBaseService.updateById(productBase);
        return ElResult.ok();
    }

    @RequestMapping("/addProductBase")
    public ElResult<ProductBase> addProductBase(ProductBase productBase){
//        修改数据
        productBaseService.save(productBase);
        return ElResult.ok();
    }

    @RequestMapping("/delProductBase")
    public ElResult<ProductBase> delProductBase(Integer id){
//        删除数据
        productBaseService.removeById(id);
        return ElResult.ok();
    }

}
