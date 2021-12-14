package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Permission;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.mapper.ModuleMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import com.yjxxt.crm.mapper.RoleMapper;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 */
@Service
public class RoleService extends BaseService<Role,Integer> {
    @Autowired(required = false)
    private RoleMapper roleMapper;
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 查询所有角色
     * @param userId
     * @return
     */
    public List<Map<String,Object>> finAllRoles(Integer userId){

        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 条件查询
     * @param roleQuery
     * @return
     */
    public Map<String,Object> findRolesByRoleName(RoleQuery roleQuery){
        HashMap<String, Object> map = new HashMap<>();
        //初始化分页
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        //准备数据
        List<Role> rList = roleMapper.selectByParams(roleQuery);
        //开始分页
        PageInfo<Role> rolePageInfo = new PageInfo<>(rList);
        map.put("code","0");
        map.put("msg","success");
        map.put("count",rolePageInfo.getTotal());
        map.put("data",rolePageInfo.getList());
        return map;
    }

    /**
     * 添加角色
     * @param role
     */
    @Transactional
    public void addRole(Role role){
        //校验 角色名必须存在 且不重复
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空");
        AssertUtil.isTrue(roleMapper.selectByName(role.getRoleName())!=null,"角色名已存在");

        //添加默认
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        //判断是否添加成功
        AssertUtil.isTrue(roleMapper.insertSelective(role)<1,"添加失败");
    }


    /**
     * 修改角色
     */
    @Transactional
    public void changeRole(Role role){
        //判断修改记录是否存在
        AssertUtil.isTrue(role.getId() ==null || roleMapper.selectByPrimaryKey(role.getId())==null,"待修改的记录不存在");
        //角色非空且不能相同
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空");
        Role temp=roleMapper.selectByName(role.getRoleName());
        AssertUtil.isTrue(temp!=null && !temp.getId().equals(role.getId()),"角色已经存在");

        //判断是否修改成功
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"角色修改失败");
    }

    /**
     * 删除角色
     * @param id
     */
    @Transactional
    public void deleteRole(Integer id){
        Role temp = roleMapper.selectByPrimaryKey(id);
        //id不能为空 且能查询到角色
        AssertUtil.isTrue(id==null || temp==null,"待删除记录不存在");
        //判断是否删除成功
        AssertUtil.isTrue(roleMapper.deleteByRoleId(id)<1,"删除失败");
    }

    /**
     * 角色授权
     */
    @Transactional
    public void addGrant(Integer roleId,Integer[] mids){
        //判断
        Role temp = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(roleId==null||temp==null,"待授权的角色不存在");
        //记录资源个数
        int count= permissionMapper.countRoleModulesByRoleId(roleId);
        //若有资源个数则全部删除
        if(count>0){
            AssertUtil.isTrue(permissionMapper.deleteRoleModuleByRoleId(roleId)!=count,"权限分配失败");
        }
        ArrayList<Permission> plist = new ArrayList<>();
        if(null!=mids && mids.length!=0){
            for(Integer mid:mids){
                Permission permission = new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mid);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                plist.add(permission);
            }
        }
        AssertUtil.isTrue(permissionMapper.insertBatch(plist)!=plist.size(),"授权失败");
    }

}
