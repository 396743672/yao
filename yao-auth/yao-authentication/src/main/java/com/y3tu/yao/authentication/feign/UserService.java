package com.y3tu.yao.authentication.feign;

import com.y3tu.yao.common.constants.ServerNameConstants;
import com.y3tu.yao.common.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 * feign 调用服务
 * </p>
 *
 * @author y3tu
 */
@FeignClient(name = ServerNameConstants.BACK_SERVER)
public interface UserService {

    /**
     * 通过用户名查找用户
     *
     * @param username
     * @return
     */
    @GetMapping("/user/findUserByUsername/{username}")
    UserVO loadUserByUsername(@PathVariable(value = "username") String username);

    /**
     * 通过mobile查找用户
     *
     * @param mobile
     * @return
     */
    @GetMapping("/user/loadUserByMobile/{mobile}")
    UserVO loadUserByMobile(@PathVariable(value = "mobile") String mobile);

}
