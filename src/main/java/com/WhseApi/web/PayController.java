package com.WhseApi.web;

import com.WhseApi.common.*;
import com.WhseApi.entity.*;
import com.WhseApi.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/pay")
public class PayController {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TypechoUsersService usersService;


    @Autowired
    private TypechoPaylogService paylogService;

    @Autowired
    private TypechoPaykeyService paykeyService;

    @Autowired(required = false)
    private WxPayService wxpayService;

    @Autowired
    private TypechoApiconfigService apiconfigService;

    @Value("${web.prefix}")
    private String dataprefix;

    RedisHelp redisHelp =new RedisHelp();
    ResultAll Result = new ResultAll();
    HttpClient HttpClient = new HttpClient();
    UserStatus UStatus = new UserStatus();


    /**
     * ?????????????????????
     * @return ??????????????????????????????
     */
    @RequestMapping(value = "/scancodePay")
    @ResponseBody
    public String scancodepay(@RequestParam(value = "num", required = false) String num, @RequestParam(value = "token", required = false) String  token) throws AlipayApiException {

        Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
        if(uStatus==0){
            return ResultAll.getResultJson(0,"??????????????????Token????????????",null);
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!pattern.matcher(num).matches()){
            return ResultAll.getResultJson(0,"??????????????????????????????",null);
        }
        if(Integer.parseInt(num) <= 0){
            return ResultAll.getResultJson(0,"?????????????????????",null);
        }
        TypechoApiconfig apiconfig = apiconfigService.selectByKey(1);

        final String APPID = apiconfig.getAlipayAppId();
        String RSA2_PRIVATE = apiconfig.getAlipayPrivateKey();
        String ALIPAY_PUBLIC_KEY = apiconfig.getAlipayPublicKey();

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");//?????????????????????????????????
        String timeID = dateFormat.format(now);
        String order_no=timeID+"scancodealipay";
        String body = "";


        String total_fee=num;  //????????????

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APPID, RSA2_PRIVATE, "json",
                "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2"); //??????????????????AlipayClient
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//??????API?????????request???
        request.setBizContent("{" +
                "    \"out_trade_no\":\"" + order_no + "\"," +
                "    \"total_amount\":\"" + total_fee + "\"," +
                "    \"body\":\"" + body + "\"," +
                "    \"subject\":\"????????????\"," +
                "    \"timeout_express\":\"90m\"}");//??????????????????
        request.setNotifyUrl(apiconfig.getAlipayNotifyUrl());
        AlipayTradePrecreateResponse response = alipayClient.execute(request);//??????alipayClient??????API??????????????????response???
        System.out.print(response.getBody());

        //??????response????????????????????????????????????
        if (response.getMsg().equals("Success")) {
            //???????????????
            Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
            Integer uid  = Integer.parseInt(map.get("uid").toString());
            Long date = System.currentTimeMillis();
            String created = String.valueOf(date).substring(0,10);
            TypechoPaylog paylog = new TypechoPaylog();
            Integer TotalAmount = Integer.parseInt(total_fee) * apiconfig.getScale();
            paylog.setStatus(0);
            paylog.setCreated(Integer.parseInt(created));
            paylog.setUid(uid);
            paylog.setOutTradeNo(order_no);
            paylog.setTotalAmount(TotalAmount.toString());
            paylog.setPaytype("scancodePay");
            paylog.setSubject("????????????");
            paylogService.insert(paylog);
            //??????????????????
            String qrcode = response.getQrCode();
            JSONObject toResponse = new JSONObject();
            toResponse.put("code" ,1);
            toResponse.put("data" , qrcode);
            toResponse.put("msg"  , "????????????");
            return toResponse.toString();
        } else{
            JSONObject toResponse = new JSONObject();
            toResponse.put("code" ,0);
            toResponse.put("data" , "");
            toResponse.put("msg"  , "????????????");
            return toResponse.toString();
        }

    }

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    @ResponseBody
    public String notify(HttpServletRequest request,
                         HttpServletResponse response) throws AlipayApiException {
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        System.err.println(params);
        TypechoApiconfig apiconfig = apiconfigService.selectByKey(1);
        String CHARSET = "UTF-8";
        //???????????????
        String ALIPAY_PUBLIC_KEY = apiconfig.getAlipayPublicKey();

        String tradeStatus = request.getParameter("trade_status");
        boolean flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, "RSA2");

        if (flag) {//????????????

            if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                //????????????????????????????????????
                String trade_no = params.get("trade_no");
                String out_trade_no = params.get("out_trade_no");
                String total_amount = params.get("total_amount");
                Integer scale = apiconfig.getScale();
                Integer integral = Double.valueOf(total_amount).intValue() * scale;

                Long date = System.currentTimeMillis();
                String created = String.valueOf(date).substring(0,10);
                TypechoPaylog paylog = new TypechoPaylog();
                //???????????????????????????????????????????????????????????????????????????
                paylog.setOutTradeNo(out_trade_no);
                paylog.setStatus(0);
                List<TypechoPaylog> logList= paylogService.selectList(paylog);
                if(logList.size() > 0){
                    Integer pid = logList.get(0).getPid();
                    Integer uid = logList.get(0).getUid();
                    paylog.setStatus(1);
                    paylog.setTradeNo(trade_no);
                    paylog.setPid(pid);
                    paylog.setCreated(Integer.parseInt(created));
                    paylogService.update(paylog);
                    //?????????????????????????????????
                    TypechoUsers users = usersService.selectByKey(uid);
                    Integer oldAssets = users.getAssets();
                    Integer assets = oldAssets + integral;
                    users.setAssets(assets);
                    usersService.update(users);
                }else{
                    System.out.println("????????????????????????");
                    return "fail";
                }
            }
            return "success";
        } else {//????????????
            return "fail";
        }
    }
    /**
     * ???????????????
     * */
    @RequestMapping(value = "/qrCode")
    @ResponseBody
    public void getQRCode(String codeContent,@RequestParam(value = "token", required = false) String  token, HttpServletResponse response) {
        Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
        if(uStatus==0){
            System.out.println("??????????????????");
        }
        System.out.println("codeContent=" + codeContent);
        try {
            /*
             * ??????????????????????????????????????????????????????
             */
            QRCodeUtil.createCodeToOutputStream(codeContent, response.getOutputStream());
            System.out.println("?????????????????????!");
        } catch (IOException e) {
            System.out.println("????????????");
        }
    }
    /**
     * ????????????
     * */
    @RequestMapping(value = "/payLogList")
    @ResponseBody
    public String orderSellList (@RequestParam(value = "token", required = false) String  token) {

        String page = "1";
        String limit = "30";
        Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
        if(uStatus==0){
            return ResultAll.getResultJson(0,"??????????????????Token????????????",null);
        }
        Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
        Integer uid =Integer.parseInt(map.get("uid").toString());
        Integer total = 0;
        TypechoPaylog query = new TypechoPaylog();
        query.setUid(uid);
        total = paylogService.total(query);
        List<TypechoPaylog> list = new ArrayList();
        List cacheList = redisHelp.getList(this.dataprefix+"_"+"payLogList_"+page+"_"+limit+"_"+uid,redisTemplate);
        try{
            if(cacheList.size()>0){
                list = cacheList;
            }else {
                PageList<TypechoPaylog> pageList = paylogService.selectPage(query, Integer.parseInt(page), Integer.parseInt(limit));
                list = pageList.getList();
                redisHelp.delete(this.dataprefix+"_"+"payLogList_"+page+"_"+limit+"_"+uid, redisTemplate);
                redisHelp.setList(this.dataprefix+"_"+"payLogList_"+page+"_"+limit+"_"+uid, list, 5, redisTemplate);
            }
        }catch (Exception e){
            if(cacheList.size()>0){
                list = cacheList;
            }
        }
        JSONObject response = new JSONObject();
        response.put("code" , 1);
        response.put("msg"  , "");
        response.put("data" , null != list ? list : new JSONArray());
        response.put("count", list.size());
        response.put("total", total);
        return response.toString();
    }
    /**
     * ????????????
     * */
    @RequestMapping(value = "/WxPay")
    @ResponseBody
    public String wxAdd(HttpServletRequest request,@RequestParam(value = "price", required = false) Integer price,@RequestParam(value = "token", required = false) String  token) throws Exception {
        Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
        if(uStatus==0){
            return ResultAll.getResultJson(0,"??????????????????Token????????????",null);
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        if(!pattern.matcher(price.toString()).matches()){
            return ResultAll.getResultJson(0,"??????????????????????????????",null);
        }
        if(price <= 0){
            return ResultAll.getResultJson(0,"?????????????????????",null);
        }
        TypechoApiconfig apiconfig = apiconfigService.selectByKey(1);
        WXConfigUtil config = new WXConfigUtil();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        //????????????????????????????????????
        data.put("appid", apiconfig.getWxpayAppId());
        data.put("mch_id", apiconfig.getWxpayMchId());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        String body = "????????????";
        data.put("body", body);
        data.put("out_trade_no", System.currentTimeMillis()+ "");
        data.put("total_fee", String.valueOf((int)(price)));
        //??????????????????IP??????
        data.put("spbill_create_ip", request.getRemoteAddr());
        //????????????????????????????????????????????????
        data.put("notify_url", apiconfig.getWxpayNotifyUrl());
        //????????????
        data.put("trade_type", "APP");
        //????????????????????????API??????????????????????????????????????????????????????????????????????????????????????????
        data.put("attach", "shop");
        data.put("sign", WXPayUtil.generateSignature(data, apiconfig.getWxpayKey(),
                WXPayConstants.SignType.MD5));
        //????????????API??????????????????
        Map<String, String> response = wxpay.unifiedOrder(data);
        System.out.println(response);
        if ("SUCCESS".equals(response.get("return_code"))) {
            //??????????????????5?????????
            Map<String, String> param = new HashMap<>();
            param.put("appid",apiconfig.getWxpayAppId());
            param.put("partnerid", response.get("mch_id"));
            param.put("prepayid", response.get("prepay_id"));
            param.put("package", "Sign=WXPay");
            param.put("noncestr", WXPayUtil.generateNonceStr());
            param.put("timestamp", System.currentTimeMillis() / 1000 + "");
            param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                    WXPayConstants.SignType.MD5));
            System.out.println(param);
            //???????????????
            Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
            Integer uid  = Integer.parseInt(map.get("uid").toString());
            Long date = System.currentTimeMillis();
            String created = String.valueOf(date).substring(0,10);
            Integer TotalAmount = price * apiconfig.getScale();
            TypechoPaylog paylog = new TypechoPaylog();
            paylog.setStatus(0);
            paylog.setCreated(Integer.parseInt(created));
            paylog.setUid(uid);
            paylog.setOutTradeNo(response.get("out_trade_no"));
            paylog.setTotalAmount(TotalAmount.toString());
            paylog.setPaytype("WXPay");
            paylog.setSubject("??????APP??????");
            paylogService.insert(paylog);


            JSONObject res = new JSONObject();
            res.put("code" , 1);
            res.put("msg"  , "");
            res.put("data" , param);
            return response.toString();
        }else {
            return ResultAll.getResultJson(0,"????????????",null);
        }
    }
    /**
     * ????????????
     * */
    @RequestMapping(value = "/wxPayNotify")
    @ResponseBody
    public String wxPayNotify(HttpServletRequest request) {
        TypechoApiconfig apiconfig = apiconfigService.selectByKey(1);
        String resXml = "";
        try {

            InputStream inputStream = request.getInputStream();
            //???InputStream?????????xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {

                while ((line = reader.readLine()) != null) {

                    sb.append(line + "\n");
                }
            } catch (IOException e) {

                System.out.println(e.getMessage());
            } finally {

                try {

                    inputStream.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            resXml = sb.toString();

            //????????????????????????????????????
            String out_trade_no = request.getParameter("out_trade_no");
            String total_amount = request.getParameter("total_fee");
            Integer scale = apiconfig.getScale();
            Integer integral = Double.valueOf(total_amount).intValue() * scale;

            Long date = System.currentTimeMillis();
            String created = String.valueOf(date).substring(0,10);
            TypechoPaylog paylog = new TypechoPaylog();
            //???????????????????????????????????????????????????????????????????????????
            paylog.setOutTradeNo(out_trade_no);
            List<TypechoPaylog> logList= paylogService.selectList(paylog);
            if(logList.size() > 0){
                Integer pid = logList.get(0).getPid();
                Integer uid = logList.get(0).getUid();
                paylog.setStatus(1);
                paylog.setTradeNo(out_trade_no);
                paylog.setPid(pid);
                paylog.setCreated(Integer.parseInt(created));
                paylogService.update(paylog);
                //?????????????????????????????????
                TypechoUsers users = usersService.selectByKey(uid);
                Integer oldAssets = users.getAssets();
                Integer assets = oldAssets + integral;
                users.setAssets(assets);
                usersService.update(users);
            }else{
                System.out.println("????????????????????????");
                String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[????????????]]></return_msg>" + "</xml> ";
                return result;
            }

            //??????????????????
            String result = wxpayService.payBack(resXml);
            return result;
        } catch (Exception e) {

            System.out.println("????????????????????????:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[????????????]]></return_msg>" + "</xml> ";
            return result;
        }
    }

    /**
     * ??????????????????
     * **/

    /**
     * ????????????
     * **/
    @RequestMapping(value = "/madetoken")
    @ResponseBody
    public String madetoken(@RequestParam(value = "price", required = false) Integer  price,@RequestParam(value = "num", required = false) Integer  num,@RequestParam(value = "token", required = false) String  token) {
        try{
            Integer uStatus = UStatus.getStatus(token, this.dataprefix, redisTemplate);
            if (uStatus == 0) {
                return ResultAll.getResultJson(0, "??????????????????Token????????????", null);
            }
            Map map = redisHelp.getMapValue(this.dataprefix + "_" + "userInfo" + token, redisTemplate);
            String group = map.get("group").toString();
            if (!group.equals("administrator")) {
                return ResultAll.getResultJson(0, "?????????????????????", null);
            }
            if(num>100){
                num = 100;
            }
            if(price<1){
                return ResultAll.getResultJson(0, "???????????????????????????1", null);
            }
            Long date = System.currentTimeMillis();
            String curTime = String.valueOf(date).substring(0, 10);
            //??????????????????
            for (int i = 0; i < num; i++) {
                TypechoPaykey paykey = new TypechoPaykey();
                String value = UUID.randomUUID()+"";
                paykey.setValue(value);
                paykey.setStatus(0);
                paykey.setPrice(price);
                paykey.setCreated(Integer.parseInt(curTime));
                paykey.setUid(-1);
                paykeyService.insert(paykey);
            }
            JSONObject response = new JSONObject();
            response.put("code" , 1);
            response.put("msg"  , "??????????????????");
            return response.toString();
        }catch (Exception e){
            JSONObject response = new JSONObject();
            response.put("code" , 1);
            response.put("msg"  , "??????????????????");
            return response.toString();
        }
    }
    /***
     * ????????????
     *
     */
    @RequestMapping(value = "/tokenPayList")
    @ResponseBody
    public String tokenPayList (@RequestParam(value = "searchParams", required = false) String  searchParams,
                            @RequestParam(value = "page"        , required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "limit"       , required = false, defaultValue = "15") Integer limit,
                                @RequestParam(value = "searchKey"        , required = false, defaultValue = "") String searchKey,
                            @RequestParam(value = "token", required = false) String  token) {
        Integer uStatus = UStatus.getStatus(token, this.dataprefix, redisTemplate);
        if (uStatus == 0) {
            return ResultAll.getResultJson(0, "??????????????????Token????????????", null);
        }
        Integer total = 0;
        Map map = redisHelp.getMapValue(this.dataprefix + "_" + "userInfo" + token, redisTemplate);
        String group = map.get("group").toString();
        if (!group.equals("administrator")) {
            return ResultAll.getResultJson(0, "?????????????????????", null);
        }
        TypechoPaykey query = new TypechoPaykey();
        if (StringUtils.isNotBlank(searchParams)) {
            JSONObject object = JSON.parseObject(searchParams);
            query = object.toJavaObject(TypechoPaykey.class);
            total = paykeyService.total(query);
        }

        PageList<TypechoPaykey> pageList = paykeyService.selectPage(query, page, limit,searchKey);
        JSONObject response = new JSONObject();
        response.put("code" , 1);
        response.put("msg"  , "");
        response.put("data" , null != pageList.getList() ? pageList.getList() : new JSONArray());
        response.put("count", pageList.getTotalCount());
        response.put("total", total);
        return response.toString();
    }
    @RequestMapping(value = "/tokenPayExcel")
    @ResponseBody
    public void tokenPayExcel(@RequestParam(value = "limit" , required = false, defaultValue = "15") Integer limit,@RequestParam(value = "token", required = false) String  token,HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("???????????????");

        Integer uStatus = UStatus.getStatus(token, this.dataprefix, redisTemplate);
        if (uStatus == 0) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=nodata.xls");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        }
        Map map = redisHelp.getMapValue(this.dataprefix + "_" + "userInfo" + token, redisTemplate);
        String group = map.get("group").toString();
        if (!group.equals("administrator")) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=nodata.xls");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        }
        TypechoPaykey query = new TypechoPaykey();
        PageList<TypechoPaykey> pageList = paykeyService.selectPage(query, 1, limit,"");
        List<TypechoPaykey> list = pageList.getList();




        String fileName = "tokenPayExcel"  + ".xls";//?????????????????????????????????
        //?????????????????????????????????????????????

        int rowNum = 1;

        String[] headers = { "ID", "?????????", "????????????", "0=?????????", "????????????"};
        //headers??????excel????????????????????????

        HSSFRow row = sheet.createRow(0);
        //???excel??????????????????

        for(int i=0;i<headers.length;i++){
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        for (TypechoPaykey paykey : list) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(paykey.getId());
            row1.createCell(1).setCellValue(paykey.getValue());
            row1.createCell(2).setCellValue(paykey.getPrice());
            row1.createCell(3).setCellValue(paykey.getStatus());
            row1.createCell(4).setCellValue(paykey.getCreated());
            rowNum++;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }
    /**
     * ????????????
     * **/
    @RequestMapping(value = "/tokenPay")
    @ResponseBody
    public String tokenPay(@RequestParam(value = "key", required = false) String key,@RequestParam(value = "token", required = false) String  token) {
        try {
            Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
            if(uStatus==0){
                return ResultAll.getResultJson(0,"??????????????????Token????????????",null);
            }
            Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
            Integer uid =Integer.parseInt(map.get("uid").toString());

            TypechoPaykey paykey = paykeyService.selectByKey(key);
            if(paykey==null){
                return ResultAll.getResultJson(0,"???????????????",null);
            }
            Integer pirce = paykey.getPrice();

            TypechoUsers users = usersService.selectByKey(uid);
            Integer assets = users.getAssets();

            if(!paykey.getStatus().equals(0)){
                return ResultAll.getResultJson(0,"???????????????",null);
            }

            //??????????????????
            Integer newassets = assets + pirce;
            users.setAssets(newassets);
            usersService.update(users);

            //??????????????????
            paykey.setStatus(1);
            paykey.setUid(uid);
            paykeyService.update(paykey);

            //??????????????????
            Long date = System.currentTimeMillis();
            String curTime = String.valueOf(date).substring(0,10);
            TypechoPaylog paylog = new TypechoPaylog();
            paylog.setStatus(1);
            paylog.setCreated(Integer.parseInt(curTime));
            paylog.setUid(uid);
            paylog.setOutTradeNo(curTime+"tokenPay");
            paylog.setTotalAmount(pirce.toString());
            paylog.setPaytype("tokenPay");
            paylog.setSubject("????????????");
            paylogService.insert(paylog);

            JSONObject response = new JSONObject();
            response.put("code" , 1);
            response.put("msg"  , "??????????????????");
            return response.toString();
        }catch (Exception e){
            JSONObject response = new JSONObject();
            response.put("code" , 0);
            response.put("msg"  , "??????????????????");
            return response.toString();
        }

    }

    /**
     * ?????????????????????
     * **/
    @RequestMapping(value = "/EPay")
    @ResponseBody
    public String EPay(@RequestParam(value = "type", required = false) String type,@RequestParam(value = "money", required = false) Integer money,@RequestParam(value = "device", required = false) String device,@RequestParam(value = "token", required = false) String  token,HttpServletRequest request) {
        if(type==null&&money==null&&money==null&&device==null){
            return Result.getResultJson(0,"???????????????",null);
        }
        try{
            Integer uStatus = UStatus.getStatus(token,this.dataprefix,redisTemplate);
            if(uStatus==0){
                return Result.getResultJson(0,"??????????????????Token????????????",null);
            }
            TypechoApiconfig apiconfig = apiconfigService.selectByKey(1);
            String url = apiconfig.getEpayUrl();
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");//?????????????????????????????????
            String timeID = dateFormat.format(now);
            String outTradeNo=timeID+"Epay_"+type;
            String  clientip = BaseFull.getIpAddr(request);
            Map<String,String> sign = new HashMap<>();
            sign.put("pid",apiconfig.getEpayPid().toString());
            sign.put("type",type.toString());
            sign.put("out_trade_no",outTradeNo);
            sign.put("notify_url",apiconfig.getEpayNotifyUrl());
            sign.put("clientip",clientip);
            sign.put("name","??????????????????");
            sign.put("money",money.toString());
            sign = sortByKey(sign);
            String signStr = "";
            for(Map.Entry<String,String> m :sign.entrySet()){
                signStr += m.getKey() + "=" +m.getValue()+"&";
            }
            signStr = signStr.substring(0,signStr.length()-1);
            signStr += apiconfig.getEpayKey();
            signStr = DigestUtils.md5DigestAsHex(signStr.getBytes());
            sign.put("sign_type","MD5");
            sign.put("sign",signStr);

                String param = "";
                for(Map.Entry<String,String> m :sign.entrySet()){
                    param += m.getKey() + "=" +m.getValue()+"&";
                }
                param = param.substring(0,param.length()-1);
                String data = HttpClient.doPost(url+"mapi.php",param);
                if(data==null){
                    return Result.getResultJson(0,"?????????????????????????????????????????????",null);
                }
                HashMap  jsonMap = JSON.parseObject(data, HashMap.class);
                if(jsonMap.get("code").toString().equals("1")){
                    //???????????????
                    Map map =redisHelp.getMapValue(this.dataprefix+"_"+"userInfo"+token,redisTemplate);
                    Integer uid  = Integer.parseInt(map.get("uid").toString());
                    Long date = System.currentTimeMillis();
                    String created = String.valueOf(date).substring(0,10);
                    TypechoPaylog paylog = new TypechoPaylog();
                    Integer TotalAmount = money * apiconfig.getScale();
                    paylog.setStatus(0);
                    paylog.setCreated(Integer.parseInt(created));
                    paylog.setUid(uid);
                    paylog.setOutTradeNo(outTradeNo);
                    paylog.setTotalAmount(TotalAmount.toString());
                    paylog.setPaytype("ePay_"+type);
                    paylog.setSubject("????????????");
                    paylogService.insert(paylog);
                    //???????????????
                    JSONObject toResponse = new JSONObject();
                    toResponse.put("code" ,1);
                    toResponse.put("payapi" ,apiconfig.getEpayUrl());
                    toResponse.put("data" , jsonMap);
                    toResponse.put("msg"  , "????????????");
                    return toResponse.toString();
                }else {
                    return Result.getResultJson(0,jsonMap.get("msg").toString(),null);
                }
            }catch (Exception e){
                System.out.println(e);
                return Result.getResultJson(0,"??????????????????????????????",null);
            }


        }
        public static <K extends Comparable<? super K>, V > Map<K, V> sortByKey(Map<K, V> map) {
            Map<K, V> result = new LinkedHashMap<>();

            map.entrySet().stream()
                    .sorted(Map.Entry.<K, V>comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
            return result;
        }
        @RequestMapping(value = "/EPayNotify", method = RequestMethod.POST)
        @ResponseBody
        public String EPayNotify(HttpServletRequest request,
                HttpServletResponse response) throws AlipayApiException {
            Map<String, String> params = new HashMap<String, String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }

            System.err.println(params);
            try{
                if(params.get("trade_status").equals("TRADE_SUCCESS")){
                    TypechoApiconfig apiconfig = apiconfigService.selectByKey(1);
                    //????????????????????????????????????
                    String trade_no = params.get("trade_no");
                    String out_trade_no = params.get("out_trade_no");
                    String total_amount = params.get("money");
                    Integer scale = apiconfig.getScale();
                    Integer integral = Double.valueOf(total_amount).intValue() * scale;

                    Long date = System.currentTimeMillis();
                    String created = String.valueOf(date).substring(0,10);
                    TypechoPaylog paylog = new TypechoPaylog();
                    //???????????????????????????????????????????????????????????????????????????
                    paylog.setOutTradeNo(out_trade_no);
                    paylog.setStatus(0);
                    List<TypechoPaylog> logList= paylogService.selectList(paylog);
                    if(logList.size() > 0){
                        Integer pid = logList.get(0).getPid();
                        Integer uid = logList.get(0).getUid();
                        paylog.setStatus(1);
                        paylog.setTradeNo(trade_no);
                        paylog.setPid(pid);
                        paylog.setCreated(Integer.parseInt(created));
                        paylogService.update(paylog);
                        //?????????????????????????????????
                        TypechoUsers users = usersService.selectByKey(uid);
                        Integer oldAssets = users.getAssets();
                        Integer assets = oldAssets + integral;
                        users.setAssets(assets);
                        usersService.update(users);
                        return "success";
                    }else{
                        System.out.println("????????????????????????");
                        return "fail";
                    }
                }else{
                    return "fail";
                }
            }catch (Exception e){
                System.out.println(e);
                return "fail";
            }
        }

}
