package com.WhseApi.web;

import com.WhseApi.common.BaseController;
import com.WhseApi.common.ResultAll;
import com.WhseApi.entity.TypechoStandardFee;
import com.WhseApi.service.TypechoStandardFeeService;
import com.WhseApi.validateGroup.AddGroup;
import com.WhseApi.validateGroup.DeleteGroup;
import com.WhseApi.validateGroup.UpdateGroup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;


/**
 * @author lty
 */
@Controller
@RequestMapping(value = "/typechoStandardFee")
@ResponseBody
@Slf4j
public class TypechoStandardFeeController extends BaseController {

    @Autowired
    private TypechoStandardFeeService service;

    @Value("${web.prefix}")
    private String dataprefix;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/getStandard/{type}")
    public String getStandard(@PathVariable Integer type) {
        List<TypechoStandardFee> list = service.getStandard(type);
        return ResultAll.getListResult(1, "查询成功", list);
    }

    @PostMapping("/add")
    public String add(@RequestHeader("token") String token,
                      @RequestBody @Validated({AddGroup.class}) TypechoStandardFee standardFee,
                      BindingResult result) throws ValidationException {
        super.validate(result);
        super.checkAuth(token, this.dataprefix, this.redisTemplate);
        service.add(standardFee);
        return ResultAll.getListResult(1, "新增成功", null);
    }

    @PostMapping("/update")
    public String update(@RequestHeader("token") String token,
                         @RequestBody @Validated({UpdateGroup.class}) TypechoStandardFee standardFee,
                         BindingResult result) throws ValidationException {
        super.validate(result);
        super.checkAuth(token, this.dataprefix, this.redisTemplate);
        service.update(standardFee);
        return ResultAll.getListResult(1, "更新成功", null);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestHeader("token") String token,
                         @RequestBody @Validated({DeleteGroup.class}) TypechoStandardFee standardFee,
                         BindingResult result) throws ValidationException {
        super.validate(result);
        super.checkAuth(token, this.dataprefix, this.redisTemplate);
        service.delete(standardFee.getId());
        return ResultAll.getListResult(1, "删除成功", null);
    }

    @GetMapping("/page")
    public String page(@RequestHeader("token") String token,
                       @RequestParam("page") Integer page,
                       @RequestParam("limit") Integer limit) throws ValidationException {
        super.checkAuth(token, this.dataprefix, this.redisTemplate);
        List<TypechoStandardFee> list = service.page(page, limit);
        int count = service.total();
        return ResultAll.getListResult(1, "查询成功", list, count);
    }
}
