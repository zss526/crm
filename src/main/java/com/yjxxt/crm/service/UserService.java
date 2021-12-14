package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.bean.UserRole;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.mapper.UserRoleMapper;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.Md5Util;
import com.yjxxt.crm.utils.PhoneUtil;
import com.yjxxt.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 *
 */
@Service
public class UserService extends BaseService<User,Integer> {
    @Autowired
    private UserMapper userMapper;

    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;


    /**
     * 用户登录
     * @param userName  用户名
     * @param userPwd  密码
     * @return
     */
    public UserModel userLogin(String userName,String userPwd){
        //验证参数
        checkLoginParams(userName,userPwd);
        //根据用户名查询用户对象
        User temp= userMapper.selectUserByName(userName);
        System.out.println(temp);
        AssertUtil.isTrue(temp==null,"用户不存在");

        //用户对象不为空，检验密码
        checkLoginPwd(userPwd,temp.getUserPwd());
        //密码正确 ，返回相关信息
        return buildUserInfo(temp);
    }

    /**
     * 将user对象转为User Model对象
     * @param user
     * @return
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        //加密用户id
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 密码校验
     * @param userPwd
     * @param userPwd1
     */
    private void checkLoginPwd(String userPwd, String userPwd1) {
        //对面加密
        userPwd= Md5Util.encode(userPwd);
        //判定
        AssertUtil.isTrue(!userPwd.equals(userPwd1),"密码不正确");
    }

    /**
     * 判定用户名和密码是否为空
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        //判定用户名和密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
    }


    /**
     * 修改密码
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPassword){
        //根据用户id查询对象
        User user = userMapper.selectByPrimaryKey(userId);
        //密码校验
        checkPasswordParams(user,oldPassword,newPassword,confirmPassword);

        //校验通过 执行更新操作
        //设置密码为新密码
        user.setUserPwd(Md5Util.encode(newPassword));
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败");
    }

    /**
     * 密码校验
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    private void checkPasswordParams(User user, String oldPassword, String newPassword, String confirmPassword) {
        //用户对象必须存在
        AssertUtil.isTrue(user==null,"用户对象不存在");
        //旧密码非空
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"旧密码不能为空");
        //旧密码与数据库密码相同
        AssertUtil.isTrue(!Md5Util.encode(oldPassword).equals(user.getUserPwd()),"原始密码不正确");
        //新密码和确认密码非空
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"确认密码不能为空");
        //新密码和确认密码相同
        AssertUtil.isTrue(!newPassword.equals(confirmPassword),"新密码与确认密码必须相同");
        //新密码不能包括原密码
        AssertUtil.isTrue(newPassword.contains(user.getUserPwd()),"新密码不能包括原密码");
    }


    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String,Object>> queryAllSalse(){

        return userMapper.selectSales();
    }

    /**
     * 条件查询
     * @param userQuery
     * @return
     */
    public Map<String,Object> queryUsersByParams(UserQuery userQuery){
        //创建map
        HashMap<String, Object> map = new HashMap<>();
        //分页初始化
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        //查询数据
        List<User> users = userMapper.selectByParams(userQuery);

        //开始分页
        PageInfo<User> pageInfo = new PageInfo(users);

        //将数据添加到map里
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());

        //返回map
        return map;

    }

    /**
     * 添加用户
     * @param user
     */
    @Transactional
    public void saveUser(User user){
        //参数校验 用户名、邮箱、手机号非空
       checkSaveParam(user.getUserName(),user.getEmail(),user.getPhone());
       //用户名不能相同
        AssertUtil.isTrue(userMapper.selectUserByName(user.getUserName())!=null,"用户名已存在");
       //设置默认参数
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //设置默认密码123456
        user.setUserPwd(Md5Util.encode("123456"));
        //判断是否添加成功
        AssertUtil.isTrue(userMapper.insertSelective(user)<1,"添加失败");
        System.out.println(user.getId()+"--->"+user.getRoleIds());
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 修改用户
     * @param user
     */
    @Transactional
    public void updateUser(User user){
        //通过id判断用户是否存在
        User temp = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(temp==null,"修改用户不存在");
        //检验是否重名
        User temp2 = userMapper.selectUserByName(user.getUserName());
        AssertUtil.isTrue(temp2!=null && !temp2.getId().equals(user.getId()),"用户名已存在");
        //校验用户名 邮箱 手机号
        checkSaveParam(user.getUserName(),user.getEmail(),user.getPhone());
        //设置默认参数
        user.setUpdateDate(new Date());
        relationUserRole(user.getId(),user.getRoleIds());
        //判断是否修改成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败");
    }

    /**
     * 参数校验 用户名、邮箱、手机号非空
     * @param userName
     * @param email
     * @param phone
     */
    private void checkSaveParam(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"请输入正确的手机号");
    }

    /**
     * 批量删除
     * @param ids
     */
    @Transactional
    public void deleteUsersByIds(Integer[] ids){
        //校验，判断数组是否为空或长度是否为零
        AssertUtil.isTrue(ids==null|| ids.length==0,"请选择要删除的用户记录");
        int count=0;
        for (Integer id:ids){
            System.out.println(id);
            count=userRoleMapper.countRolesByUserId(id);
            if(count>0){
                 AssertUtil.isTrue(userRoleMapper.deleteRolesByUserId(id)!=count,"用户角色删除失败");
            }
        }
        //判断是否删除成功
        AssertUtil.isTrue(userMapper.deleteBatch(ids)!=ids.length,"删除失败");
    }

    /**
     * 用户角色关联
     * @param userId
     * @param roleIds
     */
    private void relationUserRole(int userId,String roleIds){
        //原始角色不存在，添加新的角色记录
        //原始角色存在，先删除，在添加新的角色到用户角色表

        //获取角色的个数
        int count=userRoleMapper.countRolesByUserId(userId);
        if(count>0){
            //清除所有的角色
            AssertUtil.isTrue(userRoleMapper.deleteRolesByUserId(userId)!=count,"用户角色分配失败");
        }
        if(StringUtils.isNotBlank(roleIds)){
            ArrayList<UserRole> uList = new ArrayList<>();
            //将角色id以，分割
            String[] roleId = roleIds.split(",");
            for(String id:roleId){
                //添加角色
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Integer.parseInt(id));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                uList.add(userRole);
            }

            //批量增加
            AssertUtil.isTrue(userRoleMapper.insertBatch(uList)!=uList.size(),"用户角色分配失败");
        }

    }
}
