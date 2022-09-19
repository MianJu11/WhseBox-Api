package com.WhseApi.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Map;

/**
 * @author lty
 * 统一方法，基础控制器
 */

@Slf4j
public abstract class BaseController {

    protected void validate(BindingResult result) throws ValidationException {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError err : list) {
                throw new ValidationException(err.getDefaultMessage());
            }
            throw new ValidationException("参数异常");
        }
    }

    protected void checkAuth(String token, String dataprefix, RedisTemplate redisTemplate) throws ValidationException {
        UserStatus UStatus = new UserStatus();
        RedisHelp redisHelp = new RedisHelp();
        Integer uStatus = UStatus.getStatus(token, dataprefix, redisTemplate);
        if (uStatus == 0) {
            throw new ValidationException("用户未登录或Token验证失败");
        }
        Map map = redisHelp.getMapValue(dataprefix + "_" + "userInfo" + token, redisTemplate);
        String group = map.get("group").toString();
        if (!group.equals("administrator")) {
            throw new ValidationException("你没有操作权限");
        }
    }
}
