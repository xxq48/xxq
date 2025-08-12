package cn.edu.qjnu.xxq.controller;

import cn.edu.qjnu.xxq.entity.News;
import cn.edu.qjnu.xxq.service.INewsService;
import cn.edu.qjnu.xxq.util.ElResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 浏览量统计控制器
 * </p>
 *
 * @author yourname
 * @since 2025-08-07
 */
@RestController
@RequestMapping("/statistics")
@CrossOrigin
public class StatisticsController {
    private static final Logger log = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private INewsService newsService;

    /**
     * 分页查询新闻浏览量统计（与NewsController.pageNews方法格式一致）
     * @param pageNum 页码
     * @param pageSize 每页条数
     */
    @RequestMapping("/pageViewStats")
    public ElResult<Map<String, Object>> pageViewStats(Integer pageNum, Integer pageSize) {
        // 处理空参数，与原控制器保持一致
        pageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        Page<News> page = new Page<>(pageNum, pageSize);
        IPage<News> newsPage = newsService.page(page);
        List<News> records = newsPage.getRecords();
        long total = newsPage.getTotal();

        // 封装统计数据（仅保留statistics.txt中的核心字段）
        List<Map<String, Object>> statsList = new ArrayList<>();
        for (News news : records) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("id", news.getId());
            stats.put("title", news.getTitle());
            stats.put("viewCount", news.getViewCount());
            statsList.add(stats);
        }

        log.info("分页查询浏览量统计：页码={}, 条数={}, 总记录数={}", pageNum, pageSize, total);
        // 沿用ElResult.ok(total, data)格式（与NewsController一致）
        return ElResult.ok(total, statsList);
    }

    /**
     * 获取所有新闻浏览量总和（适配ElResult参数格式）
     */
    @RequestMapping("/totalViews")
    public ElResult<Map<String, Object>> totalViews() {
        List<News> allNews = newsService.list();
        long totalViews = allNews.stream().mapToLong(News::getViewCount).sum();

        Map<String, Object> result = new HashMap<>();
        result.put("totalViews", totalViews);

        // 将Map包装成List
        List<Map<String, Object>> resultList = new ArrayList<>();
        resultList.add(result);

        log.info("总浏览量统计：{}", totalViews);
        // 传入包装后的List
        return ElResult.ok(1L, resultList);
    }

    /**
     * 获取浏览量最高的新闻（适配ElResult参数格式）
     */
    /**
     * 获取浏览量最高的新闻（适配ElResult参数格式）
     */
    @RequestMapping("/topViewNews")
    public ElResult<News> topViewNews() {
        // 假设服务层实现了查询最高浏览量新闻的方法
        List<News> topNewsList = newsService.getTopViewNews(5);
        if (topNewsList == null || topNewsList.isEmpty()) {
            return ElResult.error("暂无新闻数据");
        }

        // （示例取第一条）
        News firstTopNews = topNewsList.get(0);
        log.info("浏览量最高的新闻：ID={}, 标题={}, 浏览量={}",
                firstTopNews.getId(), firstTopNews.getTitle(), firstTopNews.getViewCount());

        // 直接返回查询到的列表（无需额外包装为新列表）
        return ElResult.ok((long) topNewsList.size(), topNewsList);
    }

    /**
     * 按区间统计浏览量分布（适配ElResult参数格式）
     */
    @RequestMapping("/viewRangeStats")
    public ElResult<Map<String, Object>> viewRangeStats(Integer min, Integer max) {
        if (min == null || max == null || min > max) {
            return ElResult.error("参数错误：min不能大于max");
        }

        long count = newsService.countByViewRange(min, max);
        Map<String, Object> result = new HashMap<>();
        result.put("min", min);
        result.put("max", max);
        result.put("count", count);

        // 将Map包装成List
        List<Map<String, Object>> resultList = new ArrayList<>();
        resultList.add(result);

        log.info("浏览量区间[{}, {}]的新闻数量：{}", min, max, count);
        // 传入包装后的List
        return ElResult.ok(1L, resultList);
    }
}