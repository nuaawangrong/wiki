package com.wangrong.wiki.controller;

import com.wangrong.wiki.req.EbookQueryReq;
import com.wangrong.wiki.req.EbookSaveReq;
import com.wangrong.wiki.resp.CommonResp;
import com.wangrong.wiki.resp.EbookQueryResp;
import com.wangrong.wiki.resp.PageResp;
import com.wangrong.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(("/ebook"))
public class EbookController {

    @Resource
    private EbookService ebookService;


    @GetMapping("/list")
    public CommonResp list(@Valid EbookQueryReq req) {
        CommonResp< PageResp<EbookQueryResp> > resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq req) {
        //保存成功后，不需要返回任何东西
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id) {
        //删除成功后，不需要返回任何东西
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }

}






