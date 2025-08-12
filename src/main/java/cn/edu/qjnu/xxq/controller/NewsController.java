package cn.edu.qjnu.xxq.controller;

import cn.edu.qjnu.xxq.entity.News;
import cn.edu.qjnu.xxq.service.INewsService;
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
 * 新闻控制器
 * </p>
 *
 * @author yourname
 * @since 2025-08-07
 */
@RestController
@RequestMapping("/news")
@CrossOrigin
public class NewsController {
    private static final Logger log = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private INewsService newsService;

    /**
     * 分页查询新闻
     * @param pageNum 页码
     * @param pageSize 每页条数
     */
    @RequestMapping("/pageNews")
    public ElResult<News> pageNews(Integer pageNum, Integer pageSize) {
        // 处理空参数
        pageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        Page<News> page = new Page<>(pageNum, pageSize);
        Page<News> newsPage = newsService.page(page);
        List<News> records = newsPage.getRecords();
        long total = newsPage.getTotal();

        log.info("分页查询新闻：页码={}, 条数={}, 总记录数={}", pageNum, pageSize, total);
        return ElResult.ok(total, records);
    }

    /**
     * 修改新闻
     */
    @RequestMapping("/updateNews")
    public ElResult<News> updateNews(News news) {
        if (news == null || news.getId() == null) {
            return ElResult.error("新闻ID不能为空");
        }
        newsService.updateById(news);
        log.info("更新新闻成功：ID={}", news.getId());
        return ElResult.ok();
    }

    /**
     * 添加新闻
     */
    @RequestMapping("/addNews")
    public ElResult<News> addNews(News news) {
        if (!StringUtils.hasText(news.getTitle())) {
            return ElResult.error("新闻标题不能为空");
        }
        newsService.save(news);
        log.info("新增新闻成功：{}", news.getTitle());
        return ElResult.ok();
    }

    /**
     * 删除新闻
     */
    @RequestMapping("/delNews")
    public ElResult<News> delNews(Integer id) {
        if (id == null) {
            return ElResult.error("新闻ID不能为空");
        }
        newsService.removeById(id);
        log.info("删除新闻成功：ID={}", id);
        return ElResult.ok();
    }
}
