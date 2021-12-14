package com.yjxxt.crm.controller;

import com.yjxxt.crm.annotation.RequirePermission;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;


    @RequestMapping("/index")
    public String index(){
        return "/role/role";
    }



    @RequestMapping("/findRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.finAllRoles(userId);
    }

    @RequestMapping("/list")
    @ResponseBody
//    @RequirePermission(code = "60")
    public Map<String,Object> queryRoleByParams(RoleQuery roleQuery){
        return  roleService.findRolesByRoleName(roleQuery);
    }

    @RequestMapping("/save")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("添加成功");
    }


    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.changeRole(role);
        return success("修改成功");
    }

    /**
     * 跳转添加修改页面
     * @param roleId
     * @param model
     * @return
     */
    @RequestMapping("/toAddOrUpdate")
    public String toAddOrUpdate(Integer roleId, Model model){
        if(roleId!=null){
            Role role = roleService.selectByPrimaryKey(roleId);
            model.addAttribute("role",role);
        }
        return "role/add_update";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo delete(Integer id){
        roleService.deleteRole(id);
        return success("删除成功");
    }

    /**
     * 授权页面跳转
     * @return
     */
    @RequestMapping("/toRoleGrantPage")
    public String toRoleGrantPage(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    @RequestMapping("/addGrant")
    public ResultInfo addGrant(Integer roleId,Integer[] mids){
            roleService.addGrant(roleId,mids);
            return success("授权成功");
    }
}
