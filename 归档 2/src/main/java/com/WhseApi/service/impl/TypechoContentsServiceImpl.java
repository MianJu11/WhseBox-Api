package com.WhseApi.service.impl;

import com.WhseApi.common.BaseFull;
import com.WhseApi.common.ConsumeHelper;
import com.WhseApi.common.ContentStatueEnum;
import com.WhseApi.entity.*;
import com.WhseApi.common.PageList;
import com.WhseApi.dao.*;
import com.WhseApi.query.ContentQuery;
import com.WhseApi.service.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 业务层实现类
 * TypechoContentsServiceImpl
 *
 * @author buxia97
 * @date 2021/11/29
 */
@Service
public class TypechoContentsServiceImpl implements TypechoContentsService {

    @Autowired
    TypechoContentsDao dao;
    @Autowired
    TypechoStandardFeeDao standardFeeDao;
    @Autowired
    TypechoContentsTaskDao contentsTaskDao;
    @Autowired
    TypechoContentsRenovateTaskDao contentsRenovateTaskDao;
    @Autowired
    TypechoUsersDao usersDao;
    @Autowired
    private ConsumeHelper consumeHelper;
    @Autowired
    private TypechoPaylogDao payLogDao;
    @Autowired
    private TypechoRelationshipsDao relationshipsDao;
    @Autowired
    private TypechoMetasDao metasDao;


    BaseFull baseFull = new BaseFull();

    @Override
    public int insert(TypechoContents typechoContents) {
        return dao.insert(typechoContents);
    }

    @Override
    public int batchInsert(List<TypechoContents> list) {
        return dao.batchInsert(list);
    }

    @Override
    public int update(TypechoContents typechoContents) {
        return dao.update(typechoContents);
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
    public TypechoContents selectByKey(Object key) {
        return dao.selectByKey(key);
    }

    @Override
    public List<TypechoContents> selectList(TypechoContents typechoContents) {
        return dao.selectList(typechoContents);
    }

    @Override
    public PageList<TypechoContents> selectPage(TypechoContents typechoContents, Integer offset, Integer pageSize, String searchKey, String order, Integer random) {
        PageList<TypechoContents> pageList = new PageList<>();

        int total = this.total(typechoContents);

        Integer totalPage;
        if (total % pageSize != 0) {
            totalPage = (total / pageSize) + 1;
        } else {
            totalPage = total / pageSize;
        }

        int page = (offset - 1) * pageSize;

        List<TypechoContents> list = dao.selectPage(typechoContents, page, pageSize, searchKey, order, random);

        pageList.setList(list);
        pageList.setStartPageNo(offset);
        pageList.setPageSize(pageSize);
        pageList.setTotalCount(total);
        pageList.setTotalPageCount(totalPage);
        return pageList;
    }

    @Override
    public int total(TypechoContents typechoContents) {
        return dao.total(typechoContents);
    }

    @Override
    public JSONArray queryRecommendList(Integer page, Integer limit, TypechoContents query) {
        JSONArray jsonList = new JSONArray();

        // TypechoApiconfig apiconfig = apiconfigService.selectByKey(1);
        PageList<TypechoContents> pageList = this.selectPage(query, page, limit, null, "modified", null);
        List<TypechoContents> list = pageList.getList();
        for (TypechoContents typechoContents : list) {
            JSONObject json = (JSONObject) JSONObject.toJSON(typechoContents);
            String text = json.getString("text");
            List<String> imgList = baseFull.getImageSrc(text);

            text = BaseFull.toStrByChinese(text);

            json.put("images", imgList);
            json.put("text", text.length() > 200 ? text.substring(0, 200) : text);

            List<TypechoRelationships> relationships = relationshipsDao.selectByKey(typechoContents.getCid());
            if (!relationships.isEmpty() && relationships.size() > 0) {
                for (TypechoRelationships r : relationships) {
                    TypechoMetas meta = metasDao.selectByKey(r.getMid());
                    if("tag".equals(meta.getType())){
                        json.put("tag", meta);
                    }else if("category".equals(meta.getType())){
                        json.put("category", meta);
                    }
                }
            }
            // json.put("tag", tags);
            json.remove("password");
            jsonList.add(json);
        }
        return jsonList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buyStick(TypechoUsers user, ContentQuery buy) {
        TypechoContents content = dao.selectByKey(buy.getContentId());
        TypechoStandardFee standardFee = standardFeeDao.getById(buy.getStandardFeeId());

        Assert.notNull(content, "文章ID异常，没有查到数据");
        Assert.isTrue(content.getAuthorId().equals(user.getUid()), "非本人文章！不可操作！");
        Assert.notNull(standardFee, "标准资费ID异常，没有查到数据");
        Assert.isTrue(user.getAssets() >= standardFee.getPrice(), "余额不足请充值！");

        // 1.扣除积分
        user.setAssets(user.getAssets() - standardFee.getPrice());
        usersDao.update(user);

        // 上级增加积分
        consumeHelper.cashBack(user.getUid(), standardFee.getPrice());

        // 2.更新为相应状态
        if (standardFee.getType() == ContentStatueEnum.STICKY.getCode()) {
            content.setSticky(1);
            content.setModified(System.currentTimeMillis()/1000);
        } else if (standardFee.getType() == ContentStatueEnum.CAROUSEL.getCode()) {
            content.setCarousel(1);
            content.setModified(System.currentTimeMillis()/1000);
        } else if (standardFee.getType() == ContentStatueEnum.BURNING.getCode()) {
            content.setBurning(1);
            content.setModified(System.currentTimeMillis()/1000);
        }
        dao.update(content);

        // 3.记录消费日志
        TypechoPaylog paylog = new TypechoPaylog();
        paylog.setStatus(1);
        Long date = System.currentTimeMillis();
        String userTime = String.valueOf(date).substring(0, 10);
        paylog.setCreated(Integer.parseInt(userTime));
        paylog.setUid(user.getUid());
        paylog.setOutTradeNo(userTime + "buyStick");
        paylog.setTotalAmount("-" + standardFee.getPrice());
        paylog.setPaytype("buyStick");
        paylog.setSubject("购买" + ContentStatueEnum.BURNING.get(standardFee.getType()).getDesc());
        payLogDao.insert(paylog);

        // 4.记录购买的服务结束时间
        TypechoContentsTask task = contentsTaskDao.getByIdAndType(buy.getContentId(), standardFee.getType());
        if (task == null) {
            task = new TypechoContentsTask();
            task.setContentId(content.getCid());
            task.setType(standardFee.getType());
            task.setStatus(1);
            task.setEndTime(System.currentTimeMillis() + standardFee.getTimes() * 86400 * 1000);
            contentsTaskDao.insert(task);
        } else {
            task.setEndTime(task.getEndTime() + standardFee.getTimes() * 86400 * 1000);
            contentsTaskDao.update(task);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void renovate(TypechoUsers user, ContentQuery renovate) {
        TypechoContents content = dao.selectByKey(renovate.getContentId());
        Assert.notNull(content, "文章ID异常，没有查到数据");
        Assert.isTrue(content.getAuthorId().equals(user.getUid()), "非本人文章！不可操作！");

        if (renovate.getRenovateTime() == null) {
            Assert.notNull(user.getRenovate(), "刷新次数不足，请购买！");
            Assert.isTrue(user.getRenovate() > 0, "刷新次数不足，请购买！");
            user.setRenovate(user.getRenovate() - 1);
            usersDao.update(user);
            // 立即刷新
            content.setModified(System.currentTimeMillis()/1000);
            dao.update(content);
        } else {
            Assert.notNull(renovate.getIntervalTime(), "预约刷新间隔时间不可为空！");
            Assert.notNull(renovate.getRenovateTimes(), "预约刷新刷新次数不可为空！");
            Assert.notNull(user.getRenovate(), "刷新次数不足，请购买！");
            Assert.isTrue(user.getRenovate() >= renovate.getRenovateTimes(), "刷新次数不足，请购买！");
            user.setRenovate(user.getRenovate() - renovate.getRenovateTimes());
            usersDao.update(user);
            // 预约刷新
            TypechoContentsRenovateTask task = new TypechoContentsRenovateTask();
            task.setContentId(content.getCid());
            task.setStatus(1);
            task.setRenovateTime(renovate.getRenovateTime());
            task.setIntervalTime(renovate.getIntervalTime());
            task.setRenovateTimes(renovate.getRenovateTimes());
            task.setResidueTimes(renovate.getRenovateTimes());
            contentsRenovateTaskDao.insert(task);
        }
    }

    @Override
    public void updatePaymentViews() {
        dao.updatePaymentViews();
    }

    @Override
    public void updateCommonViews() {
        dao.updateCommonViews();
    }
}