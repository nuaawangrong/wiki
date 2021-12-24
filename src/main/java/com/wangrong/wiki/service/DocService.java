package com.wangrong.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangrong.wiki.domain.Doc;
import com.wangrong.wiki.domain.DocExample;
import com.wangrong.wiki.mapper.DocMapper;
import com.wangrong.wiki.req.DocQueryReq;
import com.wangrong.wiki.req.DocSaveReq;
import com.wangrong.wiki.resp.DocQueryResp;
import com.wangrong.wiki.resp.PageResp;
import com.wangrong.wiki.util.CopyUtil;
import com.wangrong.wiki.util.SnowFlake;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {

    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;


    public List<DocQueryResp> all() {
        DocExample docExample = new DocExample( );
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        //列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;
    }



    public PageResp<DocQueryResp> list(DocQueryReq req) {

        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();


        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);

        //列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存
     */
    public void save( DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        if(ObjectUtils.isEmpty(req.getId())) {
            //新增  未完成 todo
            doc.setId(snowFlake.nextId());//雪花算法生成新的自增ID
            docMapper.insert(doc);
        } else {
            //更新
            docMapper.updateByPrimaryKey(doc);
        }
    }

    /**
     * 删除
     */
    public void delete(long id) {
        docMapper.deleteByPrimaryKey(id);
    }





}
