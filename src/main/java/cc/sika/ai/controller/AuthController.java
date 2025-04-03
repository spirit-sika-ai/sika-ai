package cc.sika.ai.controller;

import cc.sika.ai.entity.dto.UserDTO;
import cc.sika.ai.entity.po.User;
import cc.sika.ai.entity.vo.R;
import cc.sika.ai.service.persistence.UserService;
import cc.sika.ai.util.RSAUtil;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    @Resource
    private UserService userService;

    @GetMapping("who")
    public R<String> who() {
        return R.success(StpUtil.getLoginIdAsString());
    }

    @GetMapping("public-key")
    public R<String> getPublicKey() {
        return R.success(RSAUtil.getPUBLIC_KEY());
    }

    @PostMapping
    public R<String> login(@RequestBody UserDTO userDTO) {
        return R.success(userService.login(userDTO));
    }

    @PostMapping("register")
    public R<String> register(@RequestBody User userDTO) {
        return R.success(userService.register(userDTO));
    }
    
    @GetMapping("encrypt")
    public R<String> encrypt(String plainText) {
        return R.success(RSAUtil.encrypt(plainText));
    }
    
    @PostMapping("encrypt-user")
    public R<Map<String, String>> encrypt(@RequestBody UserDTO userDTO) {
        Map<String, String> encryptObj = HashMap.newHashMap(2);
        encryptObj.put("username", RSAUtil.encrypt(userDTO.getUser().getUsername()));
        encryptObj.put("password", RSAUtil.encrypt(userDTO.getUser().getPassword()));
        return R.success(encryptObj);
    }
}
