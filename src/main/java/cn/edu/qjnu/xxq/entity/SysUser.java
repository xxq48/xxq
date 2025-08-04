package cn.edu.qjnu.xxq.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author xxq
 * @since 2025-07-29
 */
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @TableId(value = "su_id", type = IdType.AUTO)
    private Integer suId;

    /**
     * 用户名称
     */
    private String suName;

    /**
     * 密码，加密存储
     */
    private String suPwd;

    /**
     * 角色。user;admin
     */
    private String suRole;

    /**
     * 用户创建时间
     * 日期格式：LocalDateTime  "yyyy-MM-dd HH:mm:ss";LocalDate  "yyyy-MM-dd"
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime suTime;

    public Integer getSuId() {
        return suId;
    }

    public void setSuId(Integer suId) {
        this.suId = suId;
    }

    public String getSuName() {
        return suName;
    }

    public void setSuName(String suName) {
        this.suName = suName;
    }

    public String getSuPwd() {
        return suPwd;
    }

    public void setSuPwd(String suPwd) {
        this.suPwd = suPwd;
    }

    public String getSuRole() {
        return suRole;
    }

    public void setSuRole(String suRole) {
        this.suRole = suRole;
    }

    public LocalDateTime getSuTime() {
        return suTime;
    }

    public void setSuTime(LocalDateTime suTime) {
        this.suTime = suTime;
    }

    @Override
    public String toString() {
        return "SysUser{" +
            "suId = " + suId +
            ", suName = " + suName +
            ", suPwd = " + suPwd +
            ", suRole = " + suRole +
            ", suTime = " + suTime +
            "}";
    }
}
