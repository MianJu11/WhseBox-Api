package com.WhseApi.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.WhseApi.entity.*;
import com.WhseApi.common.*;
import com.WhseApi.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 控制层
 * TypechoShopController
 * @author buxia97
 * @date 2022/01/27
 */
@Controller
@RequestMapping(value = "/typechoShop")
public class TypechoShopController {

    @Autowired
    TypechoShopService service;

    @Autowired
    private TypechoUsersService usersService;

    @Autowired
    private TypechoUserlogService userlogService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MailService MailService;

    @Autowired
    private TypechoPaylogService paylogService;

    @Autowired
    private TypechoApiconfigService apiconfigService;

    @Autowired
    private ConsumeHelper consumeHelper;

    @Value("${web.prefix}")
    private String dataprefix;

    RedisHelp redisHelp =new RedisHelp();
    UserStatus UStatus = new UserStatus();

    /***
     * 商品列表
     */
    @RequestMapping(value = "/shopList")
    @ResponseBody
    public String shopList (@RequestParam(value = "searchParams", required = false) String  searchParams,
                            @RequestParam(value = "page"        , required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "searchKey"        , required = false, defaultValue = "") String searchKey,
                            @RequestParam(value = "limit"       , required = false, defaultValue = "15") Integer limit) {
        TypechoShop query = new TypechoShop();
        if(limit>50){
            limit = 50;
        }
        if (StringUtils.isNotBlank(searchParams)) {
            JSONObject object = JSON.parseObject(searchParams);
            query = object.toJavaObject(TypechoShop.class);
        }

        PageList<TypechoShop> pageList = service.selectPage(query, page, limit,searchKey);
        List jsonList = new ArrayList();
        List list = pageList.getList();
        for (int i = 0; i < list.size(); i++) {
            Map json = JSONObject.parseObject(JSONObject.toJSONString(list.get(i)), Map.class);
            json.remove("value");
            jsonList.add(json);
        }
        JSONObject response = new JSONObject();
        response.put("code" , 1);
        response.put("msg"  , "");
        response.put("data" , null != jsonList ? jsonList : new JSONArray());
        response.put("count", jsonList.size());
        return response.toString();
    }

    /**
     * 查询商品详情
     */
    @RequestMapping(value = "/shopInfo")
    @ResponseBody
    public String shopInfo(@RequestParam(value = "key", required = false) String  key,@RequestParam(value = "token", required = false) String  token) {
        TypechoShop info =  service.selectByKey(key);
        Map shopinfo = JSONObject.parseObject(JSONObject.toJSONString(info), Map.class);
        Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);

        if(uStatus==0){

            shopinfo.remove("value");
        }else{
            Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
            Integer uid  = Integer.parseInt(map.get("uid").toString());
            //如果登陆，判断是否购买过
            TypechoUserlog log = new TypechoUserlog();
            log.setType("buy");
            log.setUid(uid);
            log.setCid(Integer.parseInt(key));
            Integer isBuy = userlogService.total(log);
            //判断自己是不是发布者
            Integer aid = info.getUid();
            if(!uid.equals(aid)&&isBuy < 1){
                shopinfo.remove("value");
            }
        }
        JSONObject JsonMap = JSON.parseObject(JSON.toJSONString(shopinfo),JSONObject.class);
        return JsonMap.toJSONString();

    }

    /***
     * 添加软件
     */
    @RequestMapping(value = "/addShop")
    @ResponseBody
    public String addShop(@RequestParam(value = "params", required = false) String  params,@RequestParam(value = "token", required = false) String  token) {
        Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
        if(uStatus==0){
            return ResultAll.getResultJson(0,"请先注册或登录",null);
        }
        Map jsonToMap =null;
        TypechoShop insert = null;
        if (StringUtils.isNotBlank(params)) {
            jsonToMap =  JSONObject.parseObject(JSON.parseObject(params).toString());
            Integer price = 0;
            if(jsonToMap.get("price")!=null){
                price = Integer.parseInt(jsonToMap.get("price").toString());
                if(price < 0){
                    return ResultAll.getResultJson(0,"请输入积分",null);
                }
            }
            jsonToMap.put("status","0");
            //生成typecho数据库格式的创建时间戳
            Long date = System.currentTimeMillis();
            String userTime = String.valueOf(date).substring(0,10);


            jsonToMap.put("created",userTime);

            //如果用户不设置VIP折扣，则调用系统设置
            TypechoApiconfig apiconfig = apiconfigService.selectByKey(1);
            Double vipDiscount = Double.valueOf(apiconfig.getVipDiscount());
            if(jsonToMap.get("vipDiscount")==null){
                jsonToMap.put("vipDiscount",vipDiscount);
            }

            //除管理员外，默认待审核
            Map Map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
            String group = Map.get("group").toString();
            if(!group.equals("administrator")){
                jsonToMap.put("status","1"); //设置为 0 除管理员外，默认待审核
            }else{
                jsonToMap.put("status","1");
            }

            insert = JSON.parseObject(JSON.toJSONString(jsonToMap), TypechoShop.class);
            Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
            Integer uid  = Integer.parseInt(map.get("uid").toString());
            //判断用户是否绑定了邮箱
//            TypechoUsers users = usersService.selectByKey(uid);
//            if(users.getMail()==null){
//                return ResultAll.getResultJson(0,"发布软件前，请先绑定邮箱",null);
//            }

            insert.setUid(uid);
        }

        int rows = service.insert(insert);

        JSONObject response = new JSONObject();
        response.put("code" , rows);
        response.put("msg"  , rows > 0 ? "资源创建成功\n请继续下一步" : "创建失败");
        return response.toString();
    }
    /***
     * 修改商品
     */
    @RequestMapping(value = "/editShop")
    @ResponseBody
    public String editShop(@RequestParam(value = "params", required = false) String  params,@RequestParam(value = "token", required = false) String  token) {
        Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
        if(uStatus==0){
            return ResultAll.getResultJson(0,"用户未登录或Token验证失败",null);
        }
        TypechoShop update = null;
        Map jsonToMap =null;
        if (StringUtils.isNotBlank(params)) {
            jsonToMap =  JSONObject.parseObject(JSON.parseObject(params).toString());
            Integer price = 0;
            if(jsonToMap.get("price")!=null){
                price = Integer.parseInt(jsonToMap.get("price").toString());
                if(price < 0){
                    return ResultAll.getResultJson(0,"请输入正确的参数",null);
                }
            }
            // 查询发布者是不是自己，如果是管理员则跳过
            Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
            Integer uid  = Integer.parseInt(map.get("uid").toString());
            String group = map.get("group").toString();
            if(!group.equals("administrator")&&!group.equals("editor")){
                Integer sid = Integer.parseInt(jsonToMap.get("id").toString());
                TypechoShop info = service.selectByKey(sid);
                Integer aid = info.getUid();
                if(!aid.equals(uid)){
                    return ResultAll.getResultJson(0,"你无权进行此操作",null);
                }
                jsonToMap.put("status","0");
            }else{
                jsonToMap.put("status","1");
            }

            jsonToMap.remove("created");
            update = JSON.parseObject(JSON.toJSONString(jsonToMap), TypechoShop.class);
        }

        int rows = service.update(update);

        JSONObject response = new JSONObject();
        response.put("code" , rows);
        response.put("msg"  , rows > 0 ? "修改成功" : "修改失败");
        return response.toString();
    }

    /***
     * 删除商品
     */
    @RequestMapping(value = "/deleteShop")
    @ResponseBody
    public String deleteShop(@RequestParam(value = "key", required = false) String  key,@RequestParam(value = "token", required = false) String  token) {

        Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
        if(uStatus==0){
            return ResultAll.getResultJson(0,"用户未登录或Token验证失败",null);
        }
        // 查询发布者是不是自己，如果是管理员则跳过
        Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
        Integer uid  = Integer.parseInt(map.get("uid").toString());
        String group = map.get("group").toString();
        if(!group.equals("administrator")){
            Integer sid = Integer.parseInt(key);
            TypechoShop info = service.selectByKey(sid);
            Integer aid = info.getUid();
            if(!aid.equals(uid)){
                return ResultAll.getResultJson(0,"你无权进行此操作",null);
            }
        }

        int rows =  service.delete(key);
        JSONObject response = new JSONObject();
        response.put("code" , rows);
        response.put("msg"  , rows > 0 ? "操作成功" : "操作失败");
        return response.toString();
    }
    /***
     * 审核商品
     */
    @RequestMapping(value = "/auditShop")
    @ResponseBody
    public String auditShop(@RequestParam(value = "key", required = false) String  key,@RequestParam(value = "token", required = false) String  token) {

        Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
        if(uStatus==0){
            return ResultAll.getResultJson(0,"用户未登录或Token验证失败",null);
        }
        // 查询发布者是不是自己，如果是管理员则跳过
        Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
        Integer uid  = Integer.parseInt(map.get("uid").toString());
        String group = map.get("group").toString();
        if(!group.equals("administrator")&&!group.equals("editor")){
            Integer sid = Integer.parseInt(key);
            TypechoShop info = service.selectByKey(sid);
            Integer aid = info.getUid();
            if(!aid.equals(uid)){
                return ResultAll.getResultJson(0,"你无权进行此操作",null);
            }
        }
        TypechoShop shop = new TypechoShop();
        shop.setId(Integer.parseInt(key));
        shop.setStatus(1);
        Integer rows = service.update(shop);

        JSONObject response = new JSONObject();
        response.put("code" , rows);
        response.put("msg"  , rows > 0 ? "操作成功" : "操作失败");
        return response.toString();
    }
    /***
     * 购买商品
     */
    @RequestMapping(value = "/buyShop")
    @ResponseBody
    public String buyShop(@RequestParam(value = "sid", required = false) String  sid,@RequestParam(value = "token", required = false) String  token) {
        try {
            Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
            if(uStatus==0){
                return ResultAll.getResultJson(0,"用户未登录或Token验证失败",null);
            }
            Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
            Integer uid  = Integer.parseInt(map.get("uid").toString());
            TypechoShop shopinfo = service.selectByKey(sid);
            Integer aid = shopinfo.getUid();

//            if(uid.equals(aid)){
//                return ResultAll.getResultJson(0,"你不可以买自己的商品",null);
//            }
            Double vipDiscount = Double.valueOf(shopinfo.getVipDiscount());

            TypechoUsers usersinfo =usersService.selectByKey(uid.toString());
            Integer price = shopinfo.getPrice();
            //判断是否为VIP，是VIP则乘以折扣
            Long date = System.currentTimeMillis();
            String curTime = String.valueOf(date).substring(0, 10);
            Integer viptime  = usersinfo.getVip();
            if(viptime>Integer.parseInt(curTime)||viptime.equals(1)){
                double newPrice = price;
                newPrice = newPrice * vipDiscount;
                price =(int)newPrice;
            }
            Integer oldAssets =usersinfo.getAssets();
            if(price>oldAssets){
                return ResultAll.getResultJson(0,"积分余额不足",null);
            }

            Integer status = shopinfo.getStatus();
            if(!status.equals(1)){
                return ResultAll.getResultJson(0,"该商品已下架",null);
            }
            Integer num = shopinfo.getNum();
            if(num<1){
                return ResultAll.getResultJson(0,"该商品已售完",null);
            }
            Integer Assets = oldAssets - price;
            usersinfo.setAssets(Assets);
            //生成用户日志，这里的cid用于商品id
            TypechoUserlog log = new TypechoUserlog();
            log.setType("buy");
            log.setUid(uid);
            log.setCid(Integer.parseInt(sid));
            //判断商品类型，如果是实体商品需要设置收货地址
            Integer type = shopinfo.getType();
            String address = usersinfo.getAddress();
            if(type.equals(1)){
//                if(address==null||address==""){
//                    return ResultAll.getResultJson(0,"购买实体商品前，需要先设置收货地址",null);
//                }
            }else {
                //判断是否购买，非实体商品不能多次购买
                Integer isBuy = userlogService.total(log);
                if(isBuy > 0){
                    return ResultAll.getResultJson(0,"你已经兑换过了",null);
                }
            }



            log.setNum(Assets);
            log.setToid(aid);
            log.setCreated(Integer.parseInt(curTime));

            userlogService.insert(log);
            //生成购买者资产日志
            TypechoPaylog paylog = new TypechoPaylog();
            paylog.setStatus(1);
            paylog.setCreated(Integer.parseInt(curTime));
            paylog.setUid(uid);
            paylog.setOutTradeNo(curTime+"buyshop");
            paylog.setTotalAmount("-"+price);
            paylog.setPaytype("buyshop");
            paylog.setSubject("购买商品");
            paylogService.insert(paylog);

            consumeHelper.cashBack(uid,price);

            //修改用户账户
            usersService.update(usersinfo);
            //修改商品剩余数量
            Integer shopnum = shopinfo.getNum();
            shopnum = shopnum - 1;
            shopinfo.setNum(shopnum);
            service.update(shopinfo);


            //修改店家资产
            TypechoUsers minfo = usersService.selectByKey(aid);
            Integer mAssets = minfo.getAssets();
            mAssets = mAssets + price;
            minfo.setAssets(mAssets);
            usersService.update(minfo);
            //生成店家资产日志

            TypechoPaylog paylogB = new TypechoPaylog();
            paylogB.setStatus(1);
            paylogB.setCreated(Integer.parseInt(curTime));
            paylogB.setUid(aid);
            paylogB.setOutTradeNo(curTime+"sellshop");
            paylogB.setTotalAmount(price.toString());
            paylogB.setPaytype("sellshop");
            paylogB.setSubject("出售商品收益");
            paylogService.insert(paylogB);

//            //给店家发送邮件
//            try{
//                String email = minfo.getMail();
//                String name = minfo.getName();
//                String title = shopinfo.getTitle();
//                Integer bid = usersinfo.getUid();
//                MailService.send("您有新的商品订单，用户ID"+bid, "<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title></title><meta charset=\"utf-8\" /><style>*{padding:0px;margin:0px;box-sizing:border-box;}html{box-sizing:border-box;}body{font-size:15px;background:#fff}.main{margin:20px auto;max-width:500px;border:solid 1px #2299dd;overflow:hidden;}.main h1{display:block;width:100%;background:#2299dd;font-size:18px;color:#fff;text-align:center;padding:15px;}.text{padding:30px;}.text p{margin:10px 0px;line-height:25px;}.text p span{color:#2299dd;font-weight:bold;font-size:22px;margin-left:5px;}</style></head><body><div class=\"main\"><h1>商品订单</h1><div class=\"text\"><p>用户 "+name+"，你的商品<"+title+">有一个新的订单。</p><p>请及时打开APP进行处理！</p></div></div></body></html>",
//                        new String[] {email}, new String[] {});
//            }catch (Exception e){
//                System.out.println("邮箱发信配置错误："+e);
//            }



            JSONObject response = new JSONObject();
            response.put("code" , 1);
            response.put("msg"  , "兑换成功");
            return response.toString();
        }catch (Exception e){
            JSONObject response = new JSONObject();
            response.put("code" , 0);
            response.put("msg"  , "兑换失败");
            return response.toString();
        }


    }
    /***
     * 购买VIP
     */
    @RequestMapping(value = "/buyVIP")
    @ResponseBody
    public String buyVIP(@RequestParam(value = "daynum", required = false) Integer  daynum,@RequestParam(value = "day", required = false) Integer  day,@RequestParam(value = "token", required = false) String  token) {
        try {
            Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
            if(uStatus==0){
                return ResultAll.getResultJson(0,"用户未登录或Token验证失败",null);
            }
            Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
            Integer uid  = Integer.parseInt(map.get("uid").toString());
            TypechoApiconfig apiconfig = apiconfigService.selectByKey(1);

            Long date = System.currentTimeMillis();
            String curTime = String.valueOf(date).substring(0, 10);
            Integer days = 86400;
            TypechoUsers users = usersService.selectByKey(uid);
            Integer assets = users.getAssets();
            //判断用户是否为VIP，决定是续期还是从当前时间开始计算
            Integer vip = users.getVip();
            //默认是从当前时间开始相加
            Integer vipTime = Integer.parseInt(curTime) + days*day;
            if(vip.equals(1)){
                return ResultAll.getResultJson(0,"您已经是永久VIP，无需购买",null);
            }
            //如果已经是vip，走续期逻辑。
            if(vip>Integer.parseInt(curTime)){
                vipTime = vip+ days*day;
            }

            Integer AllPrice =  daynum;
            if(AllPrice>assets){
                return ResultAll.getResultJson(0,"当前资产不足，请充值",null);
            }


            if(day >= apiconfig.getVipDay()){
                //如果时间戳为1就是永久会员
                vipTime = 1;
            }
            Integer newassets = assets - AllPrice;
            //更新用户资产与登录状态
            users.setAssets(newassets);
            users.setVip(vipTime);

            int rows =  usersService.update(users);
            String created = String.valueOf(date).substring(0,10);
            TypechoPaylog paylog = new TypechoPaylog();
            paylog.setStatus(1);
            paylog.setCreated(Integer.parseInt(created));
            paylog.setUid(uid);
            paylog.setOutTradeNo(created+"buyvip");
            paylog.setTotalAmount("-"+AllPrice);
            paylog.setPaytype("buyvip");
            paylog.setSubject("兑换" +day+ "天VIP");
            paylogService.insert(paylog);

            consumeHelper.cashBack(uid,AllPrice);

            JSONObject response = new JSONObject();
            response.put("code" , rows);
            response.put("msg"  , rows > 0 ? "成功兑换"+day+"天VIP！" : "兑换失败");
            return response.toString();
        }catch (Exception e){
            JSONObject response = new JSONObject();
            response.put("code" , 0);
            response.put("msg"  , "操作失败");
            return response.toString();
        }


    }
    /***
     * VIP信息
     */
    @RequestMapping(value = "/vipInfo")
    @ResponseBody
    public String vipInfo() {
        JSONObject data = new JSONObject();
        TypechoApiconfig apiconfig = apiconfigService.selectByKey(1);
        data.put("vipDiscount",apiconfig.getVipDiscount());
        data.put("vipPrice",apiconfig.getVipPrice());
        data.put("scale",apiconfig.getScale());
		data.put("mianappa",apiconfig.getMianappa());
		data.put("adsxili",apiconfig.getAdsxili());
		data.put("mianadszl",apiconfig.getMianadszl());
		data.put("miangg",apiconfig.getMiangg());
		data.put("miandhlname",apiconfig.getMiandhlname());
		data.put("miandhlurl",apiconfig.getMiandhlurl());
		data.put("mianqq",apiconfig.getMianqq());
		data.put("miankime",apiconfig.getMiankime());
		data.put("mianappc",apiconfig.getMianappc());
		data.put("mianappb",apiconfig.getMianappb());

        data.put("versionCode",apiconfig.getVersionCode());
        data.put("versionUrl",apiconfig.getVersionUrl());
        data.put("version",apiconfig.getVersion());
        data.put("versionIntro",apiconfig.getVersionIntro());


        data.put("adAwardPoint",apiconfig.getAdAwardPoint());
		data.put("webinfoTitle",apiconfig.getWebinfoTitle());
		data.put("webinfoUrl",apiconfig.getWebinfoUrl());
		

        JSONObject response = new JSONObject();
        response.put("code" , 1);
        response.put("data" , data);
        response.put("msg"  , "");
        return response.toString();
    }
    /**
     * 文章挂载商品
     * */
    @RequestMapping(value = "/mountShop")
    @ResponseBody
    public String mountShop(@RequestParam(value = "cid", required = false) String  cid,@RequestParam(value = "sid", required = false) String  sid,@RequestParam(value = "token", required = false) String  token) {

        Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
        if(uStatus==0){
            return ResultAll.getResultJson(0,"用户未登录或Token验证失败",null);
        }
        Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
        Integer uid  = Integer.parseInt(map.get("uid").toString());
        //判断商品是不是自己的
        TypechoShop shop = new TypechoShop();
        shop.setUid(uid);
        shop.setId(Integer.parseInt(sid));
        Integer num  = service.total(shop);
        if(num < 1){
            return ResultAll.getResultJson(0,"你无权限添加他人的商品",null);
        }
        shop.setCid(Integer.parseInt(cid));
        int rows =  service.update(shop);
        JSONObject response = new JSONObject();
        response.put("code" , rows);
        response.put("msg"  , rows > 0 ? "操作成功" : "操作失败");
        return response.toString();
    }
    /***
     * 查询商品是否已经购买过
     */
    @RequestMapping(value = "/isBuyShop")
    @ResponseBody
    public String isBuyShop(@RequestParam(value = "sid", required = false) String  sid,@RequestParam(value = "token", required = false) String  token) {

        Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
        if(uStatus==0){
            return ResultAll.getResultJson(0,"用户未登录或Token验证失败",null);
        }
        Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
        Integer uid  = Integer.parseInt(map.get("uid").toString());

        TypechoUserlog log = new TypechoUserlog();
        log.setType("buy");
        log.setUid(uid);
        log.setCid(Integer.parseInt(sid));
        int rows =  userlogService.total(log);
        JSONObject response = new JSONObject();
        response.put("code" , rows > 0 ? 1 : 0);
        response.put("msg"  , rows > 0 ? "已购买" : "未购买");
        return response.toString();
    }

    /***
     * app检测 酱爆whseApp主程序检测更新
     */
    @RequestMapping(value = "/appCheck")
    @ResponseBody
    public String appCheck() {
        JSONObject data = new JSONObject();
        TypechoApiconfig apiconfig = apiconfigService.selectByKey(1);

        data.put("bm0shifou0action",apiconfig.getBm0shifou0action());
        data.put("bm0shifou0pname",apiconfig.getBm0shifou0pname());
        data.put("bm0jiance0action",apiconfig.getBm0jiance0action());
        data.put("bm0jiance0pname",apiconfig.getBm0jiance0pname());
        data.put("bm0xiazai",apiconfig.getBm0xiazai());
        data.put("bm0neirong",apiconfig.getBm0neirong());

        data.put("gg0cishu",apiconfig.getGg0cishu());
        data.put("gg0lianjie",apiconfig.getGg0lianjie());
        data.put("gg0neirong",apiconfig.getGg0neirong());

		data.put("whseApp",apiconfig.getWhseApp());
		data.put("whseApi",apiconfig.getWhseApi());
		data.put("whseApphttp",apiconfig.getWhseApphttp());

        JSONObject response = new JSONObject();
        response.put("code" , 1);
        response.put("data" , data);
        response.put("msg"  , "");
        return response.toString();
    }
}
