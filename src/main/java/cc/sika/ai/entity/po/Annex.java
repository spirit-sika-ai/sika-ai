package cc.sika.ai.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 附件表
 * @TableName ANNEX
 */
@TableName(value ="ANNEX")
@Data
public class Annex implements Serializable {
    /**
     * 附件ID
     */
    @TableId
    private String id;

    /**
     * 文件名
     */
    private String displayName;

    /**
     * 扩展名,文件后缀
     */
    private String extensionType;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 文件状态:1正常,2删除
     */
    private Integer status;

    /**
     * 分区名
     */
    private String bucketName;

    /**
     * 分区ID
     */
    private String bucketId;

    /**
     * 所属用户ID
     */
    private String staffId;

    /**
     * 所属消息ID
     */
    private String messageId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人ID
     */
    private String createBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}