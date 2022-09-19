package com.WhseApi.service.impl;

import cn.hutool.core.date.DateUtil;
import com.WhseApi.common.ConsumeHelper;
import com.WhseApi.common.PageList;
import com.WhseApi.common.RedisHelp;
import com.WhseApi.dao.TypechoContentsDao;
import com.WhseApi.dao.TypechoPaylogDao;
import com.WhseApi.dao.TypechoStandardFeeDao;
import com.WhseApi.dao.TypechoUsersDao;
import com.WhseApi.entity.TypechoContents;
import com.WhseApi.entity.TypechoPaylog;
import com.WhseApi.entity.TypechoStandardFee;
import com.WhseApi.entity.TypechoUsers;
import com.WhseApi.query.ContentQuery;
import com.WhseApi.service.TypechoUsersService;
import com.WhseApi.vo.UserVO;
import com.WhseApi.entity.*;
import com.WhseApi.dao.*;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务层实现类
 * TypechoUsersServiceImpl
 *
 * @author buxia97
 * @date 2021/11/29
 */
@Service
public class TypechoUsersServiceImpl implements TypechoUsersService {

    @Autowired
    TypechoUsersDao dao;
    @Autowired
    TypechoContentsDao contentsDao;
    @Autowired
    TypechoStandardFeeDao standardFeeDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ConsumeHelper consumeHelper;
    @Autowired
    private TypechoPaylogDao payLogDao;
    @Autowired
    private TypechoApiconfigDao apiconfigDao;

    @Override
    public int insert(TypechoUsers typechoUsers) {
        return dao.insert(typechoUsers);
    }

    @Override
    public int batchInsert(List<TypechoUsers> list) {
        return dao.batchInsert(list);
    }

    @Override
    public int update(TypechoUsers typechoUsers) {
        return dao.update(typechoUsers);
    }

    @Override
    public int delete(Object key) {
        return dao.delete(key);
    }

    @Override
    public int batchDelete(List<Object> keys) {
        return dao.batchDelete(keys);
    }

    @Override
    public TypechoUsers selectByKey(Object key) {
        return dao.selectByKey(key);
    }


    @Override
    public List<TypechoUsers> selectList(TypechoUsers typechoUsers) {
        return dao.selectList(typechoUsers);
    }

    @Override
    public PageList<TypechoUsers> selectPage(TypechoUsers typechoUsers, Integer offset, Integer pageSize, String searchKey, String order) {
        PageList<TypechoUsers> pageList = new PageList<>();

        int total = this.total(typechoUsers);

        Integer totalPage;
        if (total % pageSize != 0) {
            totalPage = (total / pageSize) + 1;
        } else {
            totalPage = total / pageSize;
        }

        int page = (offset - 1) * pageSize;

        List<TypechoUsers> list = dao.selectPage(typechoUsers, page, pageSize, searchKey, order);

        pageList.setList(list);
        pageList.setStartPageNo(offset);
        pageList.setPageSize(pageSize);
        pageList.setSearchKey(searchKey);
        pageList.setOrder(order);
        pageList.setTotalCount(total);
        pageList.setTotalPageCount(totalPage);
        return pageList;
    }

    @Override
    public int total(TypechoUsers typechoUsers) {
        return dao.total(typechoUsers);
    }

    @Override
    public void buyRenovate(TypechoUsers user, ContentQuery buy) {
        TypechoStandardFee standardFee = standardFeeDao.getById(buy.getStandardFeeId());
        Assert.notNull(standardFee, "标准资费ID异常，没有查到数据");
        Assert.isTrue(standardFee.getType() == 4, "标准资费ID异常，非刷新次数资费ID！");
        Assert.isTrue(user.getAssets() >= standardFee.getPrice(), "余额不足请充值！");

        // 扣除积分，增加刷新次数
        user.setAssets(user.getAssets() - standardFee.getPrice());
        if (user.getRenovate() == null) {
            user.setRenovate(standardFee.getTimes());
        } else {
            user.setRenovate(user.getRenovate() + standardFee.getTimes());
        }
        dao.update(user);

        // 上级增加积分
        consumeHelper.cashBack(user.getUid(), standardFee.getPrice());

        // 记录消费日志
        TypechoPaylog paylog = new TypechoPaylog();
        paylog.setStatus(1);
        Long date = System.currentTimeMillis();
        String userTime = String.valueOf(date).substring(0, 10);
        paylog.setCreated(Integer.parseInt(userTime));
        paylog.setUid(user.getUid());
        paylog.setOutTradeNo(userTime + "buyStick");
        paylog.setTotalAmount("-" + standardFee.getPrice());
        paylog.setPaytype("buyStick");
        paylog.setSubject("购买刷新次数");
        payLogDao.insert(paylog);
    }

    @Override
    public int read(TypechoUsers user, ContentQuery read) {
        TypechoContents content = contentsDao.selectByKey(read.getContentId());
        Assert.notNull(content, "文章ID异常，没有查到数据");
        // 本人文章增加刷新次数
        if (user.getUid().equals(content.getAuthorId())) {
            return 0;
        }
        RedisHelp redisHelp = new RedisHelp();
        JSONArray array = redisHelp.getJsonArray(user.getUid() + "_read_" + DateUtil.today(), redisTemplate);
        if (array.isEmpty()) {
            addRenovate(user, array, content);
            redisHelp.setList(user.getUid() + "_read_" + DateUtil.today(), array, 86400, redisTemplate);
            return 1;
        } else if (!array.contains(content.getCid())) {
            // 每日最大可获取免费刷新数
            TypechoApiconfig apiconfig = apiconfigDao.selectByKey(1);
            if (array.size() >= (apiconfig.getFreeRenovateTimes() == null ? 0 : apiconfig.getFreeRenovateTimes())) {
                return 0;
            }
            addRenovate(user, array, content);
            redisHelp.setList(user.getUid() + "_read_" + DateUtil.today(), array, 86400, redisTemplate);
            return 1;
        }
        return 0;
    }
    @Override
    public UserVO getSuperior(Integer uid) {
        TypechoUsers userInfo = selectByKey(uid);
        if (userInfo.getInviterId() == null) {
            return null;
        }
        TypechoUsers superior = selectByKey(userInfo.getInviterId());
        return new UserVO(superior);
    }

    @Override
    public List<UserVO> getSubordinates(Integer uid) {
        TypechoUsers query = new TypechoUsers();
        query.setInviterId(uid);
        List<TypechoUsers> list = selectList(query);
        return list.stream().map(
                UserVO::new
        ).collect(Collectors.toList());
    }

    private void addRenovate(TypechoUsers user, JSONArray array, TypechoContents content) {
        if (user.getRenovate() == null) {
            user.setRenovate(1);
        } else {
            user.setRenovate(user.getRenovate() + 1);
        }
        dao.update(user);
        array.add(content.getCid());
    }
}