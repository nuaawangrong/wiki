package com.wangrong.wiki.controller;

import com.wangrong.wiki.req.CategoryQueryReq;
import com.wangrong.wiki.req.CategorySaveReq;
import com.wangrong.wiki.resp.CategoryQueryResp;
import com.wangrong.wiki.resp.CommonResp;
import com.wangrong.wiki.resp.PageResp;
import com.wangrong.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(("/category"))
public class CategoryController {

    @Resource
    private CategoryService categoryService;


    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq req) {
        CommonResp< PageResp<CategoryQueryResp> > resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/all")
    public CommonResp all() {
        CommonResp< List<CategoryQueryResp> > resp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all();
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req) {
        //保存成功后，不需要返回任何东西
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id) {
        //删除成功后，不需要返回任何东西
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }

}






