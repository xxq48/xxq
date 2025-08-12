package cn.edu.qjnu.xxq.mapper;

import cn.edu.qjnu.xxq.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yourname
 * @since 2025-08-07
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {

    // 在 NewsMapper 方法上添加注解
    @Select("SELECT COUNT(*) FROM news WHERE view_count BETWEEN #{minView} AND #{maxView}")
    long countByViewRange(@Param("minView") Integer minView, @Param("maxView") Integer maxView);
    List<News> selectTopViewNews(int count);
}
