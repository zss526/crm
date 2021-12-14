package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.mapper.SaleChanceMapper;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 条件查询
     * @param saleChanceQuery
     * @return
     */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){

        HashMap<String, Object> map = new HashMap<>();
        //实例化分页单位
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        //开始分页
        //查询数据
        List<SaleChance> saleChances = saleChanceMapper.selectByParams(saleChanceQuery);
        PageInfo<SaleChance> pageInfo = new PageInfo(saleChances);
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加营销机会
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        //校验客户名称，联系人，联系电话
        checkAddParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //设置相关参数默认值
        saleChance.setState(0);//默认未分配
        saleChance.setDevResult(0);//默认未开发
        saleChance.setIsValid(1);//默认有效数据
        saleChance.setCreateDate(new Date());//默认当前系统时间
        saleChance.setUpdateDate(new Date());//默认当前系统时间

        if(!StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(1);
        }
        //校验是否添加成功
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"添加失败");

    }


    /**
     * 修改营销机会
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        //校验数据 id记录必须存在
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(null==temp,"记录必须存在");
        //参数校验
        checkAddParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //设置相关参数值
        saleChance.setUpdateDate(new Date());
        //未分配 原始记录未分配，修改改为已分配
        if(StringUtils.isBlank(temp.getAssignMan())&&StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(0);
        }

        //已分配
        if(StringUtils.isNotBlank(temp.getAssignMan()) && StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setAssignTime(null);
            saleChance.setAssignMan("");
            saleChance.setDevResult(0);
        }

        //判断是否修改成功
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"修改失败");

    }

    /**
     * 批量删除数据
     * @param ids
     */
    public void removeSaleChanceByIds(Integer[] ids){
        //判断数组是否有值
        AssertUtil.isTrue(ids==null||ids.length==0,"请选择需要删除的数据");
        System.out.println(ids.length);
        //判断是否删除成功
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)<1,"删除失败");
    }

    /**
     * 检验客户名称，联系人，联系电话
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkAddParams(String customerName, String linkMan, String linkPhone) {
        //客户名称，联系人，联系电话不能为空
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空");
        //电话要符合电话的格式
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"请输入正确的电话");
    }

}
