package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.service.SaleChanceService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/sale_chance")
public class SaleChanceController extends BaseController {

    @Autowired
    private SaleChanceService saleChanceService;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    //跳转首页
    @RequestMapping("/index")
    public String index(){
        return "/saleChance/sale_chance";
    }


    //跳转添加销售机会对话框
    @RequestMapping("/addOrUpdateDialog")
    public String addOrUpdateSaleChancePage(Integer id, Model model){
        if (id != null) {
            //查询用户信息
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            //存储
            model.addAttribute("saleChance",saleChance);
        }
        return "/saleChance/add_update";
    }


    /**
     * 添加数据
     * @param req
     * @param saleChance
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResultInfo saveSaleChance(HttpServletRequest req,SaleChance saleChance){
        //设置创建人
        //获取当前用户的id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //根据id查找用户名
        String trueName = userMapper.selectByPrimaryKey(userId).getTrueName();
        saleChance.setCreateMan(trueName);

        //添加营销机会数据
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功");

    }

    /**
     * 修改数据
     * @param saleChance
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo update(SaleChance saleChance){
        saleChanceService.updateSaleChance(saleChance);
        return success("修改成功");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/dels")
    @ResponseBody
    public ResultInfo delete(Integer[] ids){
        saleChanceService.removeSaleChanceByIds(ids);
        return success("批量删除成功");
    }



}
