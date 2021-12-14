package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;



    @RequestMapping("/index")
    public String index(){
        return "/user/user";
    }



    @RequestMapping("/login")
    @ResponseBody
    public ResultInfo login(String userName,String userPwd){
        ResultInfo resultInfo = new ResultInfo();
            UserModel userModel = userService.userLogin(userName, userPwd);
            resultInfo.setResult(userModel);

        return resultInfo;

    }

    @RequestMapping("/updatePassword")
    @ResponseBody
    public ResultInfo updateUserPwd(HttpServletRequest req,String oldPassword,String newPassword,String confirmPassword){
        ResultInfo resultInfo = new ResultInfo();

            //获取userId
            int userId = LoginUserUtil.releaseUserIdFromCookie(req);
            System.out.println(userId);
            userService.updateUserPassword(userId,oldPassword,newPassword,confirmPassword);

        return success("修改成功");
    }


    //修改密码页面
    @RequestMapping("/toPasswordPage")
    public String updatePwd(){
        return "/user/password";
    }

    //基本信息页面
    @RequestMapping("/toSettingPage")
    public String info(HttpServletRequest req){
        //获取当前id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //查询当前对象
        User user = userService.selectByPrimaryKey(userId);
        //存储到作用域
        req.setAttribute("user",user);
        return "/user/setting";
    }


    @RequestMapping("/updateInfo")
    @ResponseBody
    public ResultInfo updateInfo(User user){
        ResultInfo resultInfo = new ResultInfo();
        //修改
        userService.updateByPrimaryKeySelective(user);

        return resultInfo;
    }


    @RequestMapping("/sales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSalse();
    }

    //条件查询
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryUsersByParams(UserQuery userQuery){
        return userService.queryUsersByParams(userQuery);
    }


    //添加用户
    @RequestMapping("/save")
    @ResponseBody
    public ResultInfo saveUser(User user){
        userService.saveUser(user);
        return success("添加成功");
    }


    //修改用户
    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("修改成功");
    }

    @RequestMapping("/addOrUpdatePage")
    public String addOrUpdateUserPage(Integer id, Model model){
        if(id!=null){
            User user = userService.selectByPrimaryKey(id);
            model.addAttribute("user",user);
        }
        return "user/add_update";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo deleteUsers(Integer[] ids){
        userService.deleteUsersByIds(ids);
        return success("删除成功");
    }

}
