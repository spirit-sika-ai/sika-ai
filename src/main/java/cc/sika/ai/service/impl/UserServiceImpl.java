package cc.sika.ai.service.impl;

import cc.sika.ai.entity.dto.UserDTO;
import cc.sika.ai.entity.po.User;
import cc.sika.ai.exception.UserException;
import cc.sika.ai.mapper.UserMapper;
import cc.sika.ai.service.UserService;
import cc.sika.ai.util.RSAUtil;
import cc.sika.ai.util.SecurityUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static cc.sika.ai.util.SecurityUtil.verifyPassword;

/**
 * @author 小吴来哩
 * @since 2025-03-08 22:44:58
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public String login(UserDTO userDTO) {
        User user = userDTO.getUser();
        if (ObjectUtil.isNull(user) || CharSequenceUtil.isBlank(user.getUsername()) || CharSequenceUtil.isBlank(user.getPassword())) {
            throw new UserException(HttpStatus.HTTP_BAD_REQUEST, "缺少用户名或密码");
        }
        // 解密用户名并做数据库校验
        String decryptedUsername = RSAUtil.decrypt(user.getUsername());
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getUsername, decryptedUsername);
        User selectedUser = baseMapper.selectOne(userQuery);
        if (ObjectUtil.isEmpty(selectedUser)) {
            throw new UserException(HttpStatus.HTTP_BAD_REQUEST, String.format("用户[%s]不存在", decryptedUsername));
        }
        // 用户信息存在, 解密密码并校验登录
        String decryptedPassword = RSAUtil.decrypt(user.getPassword());
        if (!verifyPassword(decryptedPassword, selectedUser.getPassword())) {
            throw new UserException(HttpStatus.HTTP_BAD_REQUEST, "用户名或密码错误!");
        }
        // 校验通过, 直接登录
        StpUtil.login(selectedUser.getId());
        return StpUtil.getTokenValue();
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, IOException.class, SQLException.class})
    public String register(User registerDTO) {
        // 尝试获取用户名并解密
        String name;
        try {
	        if (CharSequenceUtil.isBlank(registerDTO.getUsername())) {
                throw new UserException(HttpStatus.HTTP_BAD_REQUEST, "请提交正确加密的用户信息");
	        }
            name = RSAUtil.decrypt(registerDTO.getUsername());
        } catch (CryptoException e) {
            log.error("解密失败", e);
            throw new UserException(HttpStatus.HTTP_BAD_REQUEST, "请提交正确加密的用户信息");
        }
        // 避免用户名重复
        if (userExists(name)) {
            throw new UserException(HttpStatus.HTTP_UNPROCESSABLE_ENTITY, String.format("用户名[%s]已存在", name));
        }
        // 用户注册成功时直接登录并响应token
        if (StpUtil.isLogin()) {
            String loginId = StpUtil.getLoginId().toString();
            User user = buildUser(registerDTO, loginId);
            assertInsertSuccess(baseMapper.insert(user));
        }
        // 主动注册, 生成新的id
        else {
            User user = buildUser(registerDTO, null);
            assertInsertSuccess(baseMapper.insert(user));
            // 注册完成直接登录
            return doLogin(user.getId());
        }
        return "";
    }
    
    private void assertInsertSuccess(int rowCount) throws UserException {
        if (rowCount <= 0) {
            throw new UserException();
        }
    }

    /**
     * 构建用户信息对象, 将加密的用户名解密为明文
     *
     * @param registerDTO 用户信息
     * @param id          用户id, 内部创建时为当前登录人id, 用户主动注册时为新用户id
     * @return 用户信息对象
     */
    private User buildUser(User registerDTO, String id) {
        if (CharSequenceUtil.isBlank(id)) {
            id = IdUtil.simpleUUID();
        }
        String pw;
        String name;
        try {
            pw = RSAUtil.decrypt(registerDTO.getPassword());
            name = RSAUtil.decrypt(registerDTO.getUsername());
        } catch (CryptoException e) {
            log.error("解密失败!", e);
            throw new UserException();
        }
        return User.builder()
                .id(id)
                .nickname(registerDTO.getNickname())
                // 解密用户名
                .username(name)
                // 密码解密后转为不可逆写入数据库
                .password(SecurityUtil.encryptPassword(pw))
                .createTime(LocalDateTime.now())
                .createBy(id)
                .updateTime(LocalDateTime.now())
                .updateBy(id)
                .build();
    }

    private String doLogin(String userId) {
        StpUtil.login(userId);
        return StpUtil.getTokenValue();
    }
}




