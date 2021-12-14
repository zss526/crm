package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Module;
import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.mapper.ModuleMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 获取所有资源
     * @return
     */
    public List<TreeDto> finaAllModules(){
        return moduleMapper.queryAllModules();
    }

    /**
     *
     * 获取角色所拥有的资源
     */
    public List<TreeDto> findRoleModulesByRoleId(Integer roleId){
        //获取所有资源
        List<TreeDto> tList = moduleMapper.queryAllModules();
        //获取角色资源id
        List<Integer> roleHasModules=permissionMapper.queryRoleModulesByRoleId(roleId);

        //判断当前角色所拥有的资源
        for(TreeDto treeDto:tList){
            if(roleHasModules.contains(treeDto.getId())){
                treeDto.setChecked(true);
            }
        }
        return tList;
    }


    public Map<String,Object> queryAllModules(){
        HashMap<String, Object> map = new HashMap<>();
        List<Module> modules = moduleMapper.queryModules();
        map.put("code",0);
        map.put("msg",0);
        map.put("count",modules.size());
        map.put("data",modules);
        return map;
    }
}
