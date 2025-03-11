package cc.sika.ai.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息表
 * @since 25-3-8
 */
@TableName(value ="\"MESSAGE\"")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    /**
     * 消息ID
     */
    @TableId("\"ID\"")
    private String id;

    /**
     * 会话ID
     */
    @TableField("\"SESSION_ID\"")
    private String sessionId;

    /**
     * 发送人ID
     */
    @TableField("\"SEND_ID\"")
    private String sendId;

    /**
     * 发送人昵称
     */
    @TableField("\"SEND_NAME\"")
    private String sendName;

    /**
     * 消息内容
     */
    @TableField("\"CONTENT\"")
    private String content;

    /**
     * 消息类型
     */
    @TableField("\"TYPE\"")
    private Integer type;

    /**
     * 是否有附件:1是2否
     */
    @TableField("\"ANNEX\"")
    private Integer annex;

    /**
     * 创建时间
     */
    @TableField("\"CREATE_TIME\"")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField(exist = false)
    @JsonIgnore
    @Serial
    private static final long serialVersionUID = 1L;
}