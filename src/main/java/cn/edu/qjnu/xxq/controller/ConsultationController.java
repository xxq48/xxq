package cn.edu.qjnu.xxq.controller;
import cn.edu.qjnu.xxq.entity.Consultation;
import cn.edu.qjnu.xxq.service.IConsultationService;
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
 * 咨询信息控制器
 * </p>
 *
 * @author xxq
 * @since 2025-08-08
 */
@RestController
@RequestMapping("/consultation")
@CrossOrigin
public class ConsultationController {
    private static final Logger log = LoggerFactory.getLogger(ConsultationController.class);

    @Autowired
    private IConsultationService consultationService;

    /**
     * 分页查询咨询信息
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果（总记录数+当前页数据）
     */
    @RequestMapping("/pageConsultation")
    public ElResult<Consultation> pageConsultation(Integer pageNum, Integer pageSize) {
        // 页码与每页条数默认值处理
        pageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        // 分页查询参数
        Page<Consultation> page = new Page<>(pageNum, pageSize);
        // 查询数据库
        IPage<Consultation> pageResult = consultationService.page(page);
        List<Consultation> records = pageResult.getRecords();
        long total = pageResult.getTotal();
        log.info("分页查询咨询：页码={}, 条数={}, 总记录数={}", pageNum, pageSize, total);
        return ElResult.ok(total, records);
    }

    /**
     * 查询所有咨询信息
     * @return 咨询列表
     */
    @RequestMapping("/listConsultation")
    public List<Consultation> listConsultation() {
        List<Consultation> consultationList = consultationService.list();
        log.info("查询所有咨询，共{}条", consultationList.size());
        return consultationList;
    }

    /**
     * 修改咨询信息
     * @param consultation 咨询实体（需包含有效ID）
     * @return 操作结果
     */
    @RequestMapping("/updateConsultation")
    public ElResult<Consultation> updateConsultation(Consultation consultation) {
        if (consultation == null || consultation.getId() == null) {
            return ElResult.error("咨询ID不能为空");
        }
        consultationService.updateById(consultation);
        log.info("更新咨询成功：ID={}", consultation.getId());
        return ElResult.ok();
    }

    /**
     * 添加咨询信息
     * @param consultation 咨询实体（需包含咨询人姓名和咨询内容）
     * @return 操作结果
     */
    @RequestMapping("/addConsultation")
    public ElResult<Consultation> addConsultation(Consultation consultation) {
        if (!StringUtils.hasText(consultation.getName()) || !StringUtils.hasText(consultation.getContent())) {
            return ElResult.error("咨询人姓名和咨询内容不能为空");
        }
        consultationService.save(consultation);
        log.info("新增咨询成功：{}（内容：{}）", consultation.getName(), consultation.getContent());
        return ElResult.ok();
    }

    /**
     * 删除咨询信息
     * @param id 咨询ID
     * @return 操作结果
     */
    @RequestMapping("/delConsultation")
    public ElResult<Consultation> delConsultation(Integer id) {
        if (id == null) {
            return ElResult.error("咨询ID不能为空");
        }
        consultationService.removeById(id);
        log.info("删除咨询成功：ID={}", id);
        return ElResult.ok();
    }
}