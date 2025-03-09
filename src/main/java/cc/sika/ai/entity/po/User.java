package cc.sika.ai.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @since 25-3-8
 */
@TableName(value ="USER")
@Data
public class User implements Serializable {
    /**
     * ID
     */
    @TableId
    private String id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

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