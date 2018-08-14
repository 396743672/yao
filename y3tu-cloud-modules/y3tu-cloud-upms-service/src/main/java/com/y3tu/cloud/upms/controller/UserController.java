package com.y3tu.cloud.upms.controller;


import com.y3tu.cloud.upms.entity.User;
import com.y3tu.cloud.upms.service.UserService;
import com.y3tu.tool.web.base.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author y3tu
 * @date  2018-08-05
 */
@RestController
@RequestMapping("/upms/user")
public class UserController extends BaseController<UserService, User> {

}
