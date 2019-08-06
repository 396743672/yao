package com.y3tu.yao.authorization.service.fallback;

import com.y3tu.yao.authorization.service.UserService;
import com.y3tu.yao.common.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author y3tu
 */
@Slf4j
@Service
public class UserServiceFallback implements UserService {

    @Override
    public UserVO loadUserByUsername(String username) {
        log.error("调用loadUserByUsername方法异常，参数：{}", username);
        throw new RuntimeException();
    }

    @Override
    public UserVO loadUserByMobile(String mobile) {
        log.error("调用loadUserByMobile方法异常，参数：{}", mobile);
        return null;
    }
}
