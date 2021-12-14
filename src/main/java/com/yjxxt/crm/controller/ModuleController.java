package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/module")
public class ModuleController extends BaseController {
    @Autowired
    private ModuleService moduleService;

    /**
     * 跳转页面
     * @param
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "module/module";
    }

    @RequestMapping("/findModules")
    @ResponseBody
    public List<TreeDto> queryAllModules(Integer roleId){
        return moduleService.findRoleModulesByRoleId(roleId);
    }


    /*
        查询所有资源
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryAllModules(){
        return moduleService.queryAllModules();
    }
}
