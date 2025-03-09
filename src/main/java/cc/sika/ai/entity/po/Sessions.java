package cc.sika.ai.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 会话记录表
 * @TableName SESSIONS
 */
@TableName(value ="SESSIONS")
@Data
public class Sessions implements Serializable {
    /**
     * 会话ID
     */
    @TableId
    private String id;

    /**
     * 会话名称
     */
    private String sessionName;

    /**
     * 类型: 1单聊, 2群聊
     */
    private Integer sessionType;

    /**
     * 操作人ID
     */
    private String staffId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 会话状态
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}