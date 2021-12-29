package com.wangrong.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangrong.wiki.domain.User;
import com.wangrong.wiki.domain.UserExample;
import com.wangrong.wiki.exception.BusinessException;
import com.wangrong.wiki.exception.BusinessExceptionCode;
import com.wangrong.wiki.mapper.UserMapper;
import com.wangrong.wiki.req.UserQueryReq;
import com.wangrong.wiki.req.UserSaveReq;
import com.wangrong.wiki.resp.PageResp;
import com.wangrong.wiki.resp.UserQueryResp;
import com.wangrong.wiki.util.CopyUtil;
import com.wangrong.wiki.util.SnowFlake;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req) {

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();

        if(!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andNameLike("%" + req.getLoginName() + "%");
        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);

        //列表复制
        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存
     */
    public void save( UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if(ObjectUtils.isEmpty(req.getId())) {
            User userDB = selectByLoginName(req.getLoginName());
            if(ObjectUtils.isEmpty(userDB)) {
                //新增
                user.setId(snowFlake.nextId());//雪花算法生成新的自增ID
                userMapper.insert(user);
            } else {
                //用户名已存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            //更新
            user.setLoginName(null);
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * 删除
     */
    public void delete(long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String loginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }

}
