package com.example.demo.service;

import com.example.demo.pojo.BaseEntity;
import com.example.demo.mapper.BaseMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class BaseService<T extends BaseEntity, ID extends Serializable> {

    public abstract BaseMapper<T,ID> getMapper();


    /////////////////////////////////////////通用处理/////////////////////
    //增
    public String addByObject(String statement,Object param, boolean isAutoKey) {
        return this.getMapper().addByObject(statement, param, isAutoKey);
    }

    //改
    public Integer updateByObject(String statement,Object param) {
        return this.getMapper().updateByObject(statement, param);
    }

    //删
    public Integer deleteByObject(String statement, Object param){
        return this.getMapper().deleteByObject(statement, param);
    }

    //查单
    public Object getByObject(String statement, Object param) {
        return this.getMapper().getByObject(statement, param);
    }

    //查多
    public List<T> getListByObject(String statement, Object param){
        return this.getMapper().getListByObject(statement,param);
    }

    public<P> List<P> getListByObject(P p, String statement,Object param){
        return this.getMapper().getListByObject(p,statement,param);
    }

    public List<Map<String, Object>> getMapListByObject(String statement, Object param){
        return this.getMapper().getMapListByObject(statement,param);
    }

}
