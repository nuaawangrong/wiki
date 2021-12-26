package com.wangrong.wiki.controller;

import com.wangrong.wiki.req.DocQueryReq;
import com.wangrong.wiki.req.DocSaveReq;
import com.wangrong.wiki.resp.CommonResp;
import com.wangrong.wiki.resp.DocQueryResp;
import com.wangrong.wiki.resp.PageResp;
import com.wangrong.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(("/doc"))
public class DocController {

    @Resource
    private DocService docService;


    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req) {
        CommonResp< PageResp<DocQueryResp> > resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/all")
    public CommonResp all() {
        CommonResp< List<DocQueryResp> > resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        //保存成功后，不需要返回任何东西
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr) {
        //删除成功后，不需要返回任何东西
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return resp;
    }

}






