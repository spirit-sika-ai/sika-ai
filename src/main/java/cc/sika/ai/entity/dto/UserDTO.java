package cc.sika.ai.entity.dto;

import cc.sika.ai.entity.po.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private User user;
    /**
     * 验证码
     */
    private String captcha;

    @Serial
    @JsonIgnore
    private static final long serialVersionUID = 1L;
}
