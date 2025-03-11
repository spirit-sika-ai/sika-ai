package cc.sika.ai.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 会话记录表
 * @since 25-3-8
 */
@TableName(value ="\"SESSIONS\"")
@Data
public class Sessions implements Serializable {
    /**
     * 会话ID
     */
    @TableId("\"ID\"")
    private String id;

    /**
     * 会话名称
     */
    @TableField("\"SESSION_NAME\"")
    private String sessionName;

    /**
     * 类型: 1单聊, 2群聊
     */
    @TableField("\"SESSION_TYPE\"\"")
    private Integer sessionType;

    /**
     * 操作人ID
     */
    @TableField("\"STAFF_ID\"")
    private String staffId;

    /**
     * 创建时间
     */
    @TableField("\"CREATE_TIME\"")
    private Date createTime;

    /**
     * 会话状态
     */
    @TableField("\"STATUS\"")
    private Integer status;

    @TableField(exist = false)
    @JsonIgnore
    @Serial
    private static final long serialVersionUID = 1L;
}