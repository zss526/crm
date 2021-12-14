package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.UserRole;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    //查询用户的角色个数
    int countRolesByUserId(Integer userId);

    //清楚用户的所有角色
    int deleteRolesByUserId(Integer userId);

}