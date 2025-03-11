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
 * 用户表
 *
 * @since 25-3-8
 */
@TableName(value = "\"USER\"")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
	/**
	 * ID
	 */
	@TableId("\"ID\"")
	private String id;

	/**
	 * 昵称
	 */
	@TableField("\"NICKNAME\"")
	private String nickname;

	/**
	 * 用户名
	 */
	@TableField("\"USERNAME\"")
	private String username;

	/**
	 * 密码
	 */
	@TableField("\"PASSWORD\"")
	private String password;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField("\"CREATE_TIME\"")
	private LocalDateTime createTime;

	/**
	 * 创建人ID
	 */
	@TableField("\"CREATE_BY\"")
	private String createBy;

	@Serial
	@TableField(exist = false)
	@JsonIgnore
	private static final long serialVersionUID = 1L;
}