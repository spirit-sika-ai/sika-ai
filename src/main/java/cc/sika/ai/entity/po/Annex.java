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
 * 附件表
 * @since 25-3-8
 */
@TableName(value ="\"ANNEX\"")
@Data
public class Annex implements Serializable {
    /**
     * 附件ID
     */
    @TableId("\"ID\"")
    private String id;

    /**
     * 文件名
     */
    @TableField("\"DISPLAY_NAME\"")
    private String displayName;

    /**
     * 扩展名,文件后缀
     */
    @TableField("\"EXTENSION_TYPE\"")
    private String extensionType;

    /**
     * 文件URL
     */
    @TableField("\"FILE_URL\"")
    private String fileUrl;

    /**
     * 文件状态:1正常,2删除
     */
    @TableField("\"STATUS\"")
    private Integer status;

    /**
     * 分区名
     */
    @TableField("\"BUCKET_NAME\"")
    private String bucketName;

    /**
     * 分区ID
     */
    @TableField("\"BUCKET_ID\"")
    private String bucketId;

    /**
     * 所属用户ID
     */
    @TableField("\"STAFF_ID\"")
    private String staffId;

    /**
     * 所属消息ID
     */
    @TableField("\"MESSAGE_ID\"")
    private String messageId;

    /**
     * 创建时间
     */
    @TableField("\"CREATE_TIME\"")
    private Date createTime;

    /**
     * 创建人ID
     */
    @TableField("\"CREATE_BY\"")
    private String createBy;

    @TableField(exist = false)
    @Serial
    @JsonIgnore
    private static final long serialVersionUID = 1L;
}