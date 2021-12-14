package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Module;
import com.yjxxt.crm.dto.TreeDto;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {

//    查找所有资源
    List<TreeDto> queryAllModules();

    List<Module> queryModules();
}