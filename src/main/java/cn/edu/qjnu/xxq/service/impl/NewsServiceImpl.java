package cn.edu.qjnu.xxq.service.impl;

import cn.edu.qjnu.xxq.entity.News;
import cn.edu.qjnu.xxq.mapper.NewsMapper;
import cn.edu.qjnu.xxq.service.INewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yourname
 * @since 2025-08-07
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

    @Autowired  // 或者 @Resource
    private NewsMapper newsMapper;
    @Override
    public List<News> getTopViewNews(int count) {
        // 实现获取指定数量的热门新闻逻辑
        // 示例实现：
        return newsMapper.selectTopViewNews(count);
    }
    // 关键：返回值类型必须与接口完全一致（这里以long为例，需替换为实际类型）
    @Override
    public long countByViewRange(Integer minView, Integer maxView) {
        return baseMapper.countByViewRange(minView, maxView);
    }

}