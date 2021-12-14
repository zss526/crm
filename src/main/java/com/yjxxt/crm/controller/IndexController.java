package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.service.PermissionService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
@Controller
public class IndexController extends BaseController {

   @Autowired
    private UserService userService;
   @Autowired
   private PermissionService permissionService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    @RequestMapping("/main")
    public String main(HttpServletRequest req){
        //通过工具类LoginUserUtil从cookie中获取userId
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //查询用户对象
        User user = userService.selectByPrimaryKey(userId);
        //将user存储到request作用域中
        req.setAttribute("user",user);

        //将用户资源吗存储到session作用域
        List<String> permissions = permissionService.queryUserHasRolesHasPermissions(userId);
        req.getSession().setAttribute("permissions",permissions);
        return "main";
    }


}
