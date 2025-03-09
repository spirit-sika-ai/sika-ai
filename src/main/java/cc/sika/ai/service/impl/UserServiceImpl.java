package cc.sika.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.sika.ai.entity.po.User;
import cc.sika.ai.service.UserService;
import cc.sika.ai.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @author 22355
 * @createDate 2025-03-08 22:44:58
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}




