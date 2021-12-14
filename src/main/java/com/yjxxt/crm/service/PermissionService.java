package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Permission;
import com.yjxxt.crm.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Resource
    private PermissionMapper permissionMapper;


    /**
     * 通过用户id查询用户资源码
     * @param userId
     * @return
     */
    public List<String> queryUserHasRolesHasPermissions(Integer userId){
        return permissionMapper.selectUserHasRolesHasPermissions(userId);
    }
}
