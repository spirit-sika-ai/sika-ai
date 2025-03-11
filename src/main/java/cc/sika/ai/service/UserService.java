package cc.sika.ai.service;

import cc.sika.ai.entity.dto.UserDTO;
import cc.sika.ai.entity.po.User;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 小吴来哩
 * @since 2025-03-08 22:44:58
 */
public interface UserService extends IService<User> {
    
    /**
     * <p>登录接口</p>
     * <p>
     *     登录逻辑: 前端获取公钥, 使用公钥对用户名密码加密并提交加密后的表单信息.
     *     尝试获取用户名密码并对用户名使用私钥解密, 解密后通过用户名查询数据库获取用户信息对象
     * </p>
     * <ul>
     *     <li>获取不成功响应 '用户名或密码' 错误</li>
     *     <li>获取成功对用户提交的密码进行解密, 并使用BCrypt比较密码</li>
     * </ul>
     * @param userDTO -
     * @return 登录成功返回token
     */
    String login(UserDTO userDTO);
    
    /**
     * 将用户名用私钥解密后以明文的形式写入数据库
     * 将密码解密为明文并使用不可逆加密加密后入库
     * 判断用户时系统添加还是主动注册, 如果为主动注册直接登录并响应token
     * @param registerDTO -
     * @return 有登录时返回token, 无登录返回空字符串
     */
    String register(User registerDTO);
    
    default boolean userExists(String username) {
        if (ObjectUtil.isEmpty(username)) {
            return false;
        }
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getUsername, username);
        return !BeanUtil.isEmpty(getOne(userQuery, false));
    }
}
