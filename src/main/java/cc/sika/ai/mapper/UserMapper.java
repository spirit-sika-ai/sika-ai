package cc.sika.ai.mapper;

import cc.sika.ai.entity.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 针对表【USER(用户表)】的数据库操作Mapper
 *
 * @author 小吴来哩
 * @since 2025-03-08 22:44:58
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 通过用户名查询用户信息
     * @param username 用户名
     * @return 用户po
     */
    User selectByUsername(@Param("username") String username);
}




