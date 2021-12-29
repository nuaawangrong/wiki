package com.wangrong.wiki.controller;

import com.wangrong.wiki.req.UserQueryReq;
import com.wangrong.wiki.req.UserSaveReq;
import com.wangrong.wiki.resp.CommonResp;
import com.wangrong.wiki.resp.UserQueryResp;
import com.wangrong.wiki.resp.PageResp;
import com.wangrong.wiki.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(("/user"))
public class UserController {

    @Resource
    private UserService userService;


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

}






