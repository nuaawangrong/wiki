package com.wangrong.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.wangrong.wiki.req.UserLoginReq;
import com.wangrong.wiki.req.UserQueryReq;
import com.wangrong.wiki.req.UserResetPasswordReq;
import com.wangrong.wiki.req.UserSaveReq;
import com.wangrong.wiki.resp.CommonResp;
import com.wangrong.wiki.resp.PageResp;
import com.wangrong.wiki.resp.UserLoginResp;
import com.wangrong.wiki.resp.UserQueryResp;
import com.wangrong.wiki.service.UserService;
import com.wangrong.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(("/user"))
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowFlake snowFlake;


    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        CommonResp< PageResp<UserQueryResp> > resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {
        //保存成功后，不需要返回任何东西
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id) {
        //删除成功后，不需要返回任何东西
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req) {
        //保存成功后，不需要返回任何东西
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(req);
        return resp;
    }

    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req) {
        //登录成功后，返回用户信息
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);

        Long token = snowFlake.nextId();
        LOG.info("生成单点登录token:{},并放入redis中",token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(userLoginResp), 3600*24, TimeUnit.SECONDS);

        resp.setContent(userLoginResp);
        return resp;
    }


}
