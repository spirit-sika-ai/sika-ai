package cc.sika.ai.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息表
 * @TableName MESSAGE
 */
@TableName(value ="MESSAGE")
@Data
public class Message implements Serializable {
    /**
     * 消息ID
     */
    @TableId
    private String id;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 发送人ID
     */
    private String sendId;

    /**
     * 发送人昵称
     */
    private String sendName;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 是否有附件:1是2否
     */
    private Integer annex;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}