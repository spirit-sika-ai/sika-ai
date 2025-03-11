package cc.sika.ai.controller;

import cc.sika.ai.entity.dto.UserDTO;
import cc.sika.ai.entity.po.User;
import cc.sika.ai.entity.vo.R;
import cc.sika.ai.service.UserService;
import cc.sika.ai.util.RSAUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@RestController
@RequestMapping("auth")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("public-key")
    public R<String> getPublicKey() {
        return R.success(RSAUtil.getPublicKey());
    }

    @PostMapping
    public R<String> login(@RequestBody UserDTO userDTO) {
        return R.success(null);
    }

    @PostMapping("register")
    public R<String> register(@RequestBody User userDTO) {
        return R.success(userService.register(userDTO));
    }
}
