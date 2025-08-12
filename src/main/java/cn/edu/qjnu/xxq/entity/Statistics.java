package cn.edu.qjnu.xxq.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yourname
 * @since 2025-08-11
 */
@Getter
@Setter
@ToString
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String titel;

    private String viewCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }
}
