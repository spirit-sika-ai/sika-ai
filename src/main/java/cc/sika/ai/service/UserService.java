package cc.sika.ai.service;

import cc.sika.ai.entity.dto.UserDTO;
import cc.sika.ai.entity.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 小吴来哩
 * @since 2025-03-08 22:44:58
 */
public interface UserService extends IService<User> {
    String login(UserDTO userDTO);

    String register(User registerDTO);
}
