package com.wangrong.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangrong.wiki.domain.Content;
import com.wangrong.wiki.domain.Doc;
import com.wangrong.wiki.domain.DocExample;
import com.wangrong.wiki.exception.BusinessException;
import com.wangrong.wiki.exception.BusinessExceptionCode;
import com.wangrong.wiki.mapper.ContentMapper;
import com.wangrong.wiki.mapper.DocMapper;
import com.wangrong.wiki.mapper.DocMapperCust;
import com.wangrong.wiki.req.DocQueryReq;
import com.wangrong.wiki.req.DocSaveReq;
import com.wangrong.wiki.resp.DocQueryResp;
import com.wangrong.wiki.resp.PageResp;
import com.wangrong.wiki.util.CopyUtil;
import com.wangrong.wiki.util.RedisUtil;
import com.wangrong.wiki.util.RequestContext;
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
    private DocMapperCust docMapperCust;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RedisUtil redisUtil;

    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample( );
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
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
        Content content = CopyUtil.copy(req, Content.class);
        if(ObjectUtils.isEmpty(req.getId())) {
            //新增
            doc.setId(snowFlake.nextId());//雪花算法生成新的自增ID
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            //更新
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if(count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    /**
     * 删除
     */
    public void delete(long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(long id) {
        Content content =  contentMapper.selectByPrimaryKey(id);
        if(content == null) {
            return "";
        } else {
            //文档阅读数加一
            docMapperCust.increaseViewCount(id);
            return content.getContent();
        }
    }

    /**
     * 点赞
     */
    public void vote(Long id) {
//        docMapperCust.increaseVoteCount(id);
        //远程IP+doc.id 作为key,24小时内不能重复
        String ip = RequestContext.getRemoteAddr();
        if( redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600*24)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
    }

    public void updateEbookInfo() {
        docMapperCust.updateEbookInfo();
    }

}
