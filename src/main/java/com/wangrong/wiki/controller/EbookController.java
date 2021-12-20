package com.wangrong.wiki.controller;

import com.wangrong.wiki.req.EbookReq;
import com.wangrong.wiki.resp.CommonResp;
import com.wangrong.wiki.resp.EbookResp;
import com.wangrong.wiki.resp.PageResp;
import com.wangrong.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(("/ebook"))
public class EbookController {

    @Resource
    private EbookService ebookService;


    @GetMapping("/list")
    public CommonResp list(EbookReq req) {

        CommonResp< PageResp<EbookResp> > resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);

        resp.setContent(list);
        return resp;

    }


}
