package cn.edu.qjnu.xxq.service;

import java.util.List;

import cn.edu.qjnu.xxq.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yourname
 * @since 2025-08-07
 */
public interface INewsService extends IService<News> {

    List<News> getTopViewNews(int limit);
    // 添加统计浏览量在指定区间的新闻数量的方法
    long countByViewRange(Integer min, Integer max);
}
