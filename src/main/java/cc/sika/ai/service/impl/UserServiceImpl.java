package cc.sika.ai.service.impl;

import cc.sika.ai.entity.dto.UserDTO;
import cc.sika.ai.entity.po.User;
import cc.sika.ai.exception.UserException;
import cc.sika.ai.mapper.UserMapper;
import cc.sika.ai.service.UserService;
import cc.sika.ai.util.RSAUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static cc.sika.ai.util.SecurityUtil.encryptPassword;

/**
 * @author 小吴来哩
 * @since 2025-03-08 22:44:58
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public String login(UserDTO userDTO) {
        User user = userDTO.getUser();
        if (ObjectUtil.isNull(user) || StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            throw new UserException("缺少用户名或密码");
        }
        // 解密用户名并做数据库校验
        String decryptedUsername = RSAUtil.decrypt(user.getUsername());
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getUsername, decryptedUsername);
        User selectedUser = baseMapper.selectOne(userQuery);
        if (ObjectUtil.isEmpty(selectedUser)) {
            throw new UserException(String.format("用户[%s]不存在", decryptedUsername));
        }
        // 用户信息存在, 解密密码并校验登录
        String decryptedPassword = RSAUtil.decrypt(user.getPassword());
        if (!BCrypt.checkpw(decryptedPassword, selectedUser.getPassword())) {
            throw new UserException("用户名或密码错误!");
        }
        // 校验通过, 直接登录
        StpUtil.login(selectedUser.getId());
        return StpUtil.getTokenValue();
    }

    @Override
    @Transactional(rollbackFor = UserException.class)
    public String register(User registerDTO) {
        // 校验用户名是否存在
        if (!StrUtil.isBlank(registerDTO.getUsername()) && userExists(registerDTO.getUsername())) {
            throw new UserException(String.format("未找到用户名为[%s]的用户", registerDTO.getUsername()));
        }
        // 判断系统添加还是用户主动注册
        if (StpUtil.isLogin()) {
            String loginId = StpUtil.getLoginId().toString();
            User user = buildUser(registerDTO, loginId);
            int insert = baseMapper.insert(user);
            assertUserExists(insert);
        }
        // 主动注册, 生成新的id
        else {
            User user = buildUser(registerDTO, null);
            int insert = baseMapper.insert(user);
            int i = 1 / 0;
            assertUserExists(insert);
            // 注册完成直接登录
            return doLogin(user.getId());
        }
        return "";
    }

    private void assertUserExists(int rowCount) throws UserException {
        if (rowCount <= 0) {
            throw new UserException();
        }
    }

    /**
     * 构建用户信息对象
     *
     * @param registerDTO 用户信息
     * @param id          用户id, 内部创建时为当前登录人id, 用户主动注册时为新用户id
     * @return 用户信息对象
     */
    private User buildUser(User registerDTO, String id) {
        if (StrUtil.isBlank(id)) {
            id = IdUtil.simpleUUID();
        }
        return User.builder()
                .id(id)
                .nickname(registerDTO.getNickname())
                .username(registerDTO.getUsername())
                .password(encryptPassword(registerDTO.getPassword()))
                .createTime(LocalDateTime.now())
                .createBy(id)
                .build();
    }

    private boolean userExists(String username) {
        User user = baseMapper.selectByUsername(username);
        return !BeanUtil.isEmpty(user);
    }

    private String doLogin(String userId) {
        StpUtil.login(userId);
        return StpUtil.getTokenValue();
    }
}




