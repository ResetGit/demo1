package com.example.demo.mapper;


import com.example.demo.pojo.BaseEntity;
import com.example.demo.util.UniqueId;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class BaseMapper<T extends BaseEntity, ID extends Serializable> extends SqlSessionDaoSupport {

    public static final String SQLNAME_SEPARATOR = ".";

    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    /**
     * 获取默认SqlMapping命名空间。
     * 使用泛型参数中业务实体类型的全限定名作为默认的命名空间。
     * 如果实际应用中需要特殊的命名空间，可由子类重写该方法实现自己的命名空间规则。
     * @return 返回命名空间字符串
     */
    protected String getDefaultSqlNamespace() {
        String nameSpace = this.getClass().getName();
        return nameSpace;
    }

    /**
     * 将SqlMapping命名空间与给定的SqlMapping名组合在一起。
     * @param sqlName SqlMapping名
     * @return 组合了SqlMapping命名空间后的完整SqlMapping名
     */
    protected String getSqlName(String sqlName) {
        return sqlNamespace + SQLNAME_SEPARATOR + sqlName;
    }

    /**
     * SqlMapping命名空间
     */
    private String sqlNamespace = getDefaultSqlNamespace();

    /**
     * 获取SqlMapping命名空间
     * @return SqlMapping命名空间
     */
    public String getSqlNamespace() {
        return sqlNamespace;
    }

    /**
     * 设置SqlMapping命名空间。
     * 此方法只用于注入SqlMapping命名空间，以改变默认的SqlMapping命名空间，
     * 不能滥用此方法随意改变SqlMapping命名空间。
     * @param sqlNamespace SqlMapping命名空间
     */
    public void setSqlNamespace(String sqlNamespace) {
        this.sqlNamespace = sqlNamespace;
    }



    ///////////////////////////////////////////////////通用操作//////////////////////////////////////////////
    /**
     * 通用增加
     */
    public String addByObject(String statement,Object obj, boolean isAutoKey) {
        String key = "";
        if(isAutoKey){
            if(obj instanceof BaseEntity){
                key = UniqueId.getGUID2();
                ((BaseEntity) obj).setId(key);
            }
        }else {
            if(obj instanceof BaseEntity){
                key = ((BaseEntity) obj).getId();
            }
        }

        this.getSqlSession().insert(getSqlName(statement), obj);
        return key;
    }

    /**
     * 通用删除
     */
    public Integer deleteByObject(String statement,Object obj){
        return this.getSqlSession().delete(
                getSqlName(statement), obj);
    }

    /**
     * 通用修改
     */
    public Integer updateByObject(String statement,Object ob) {
        return this.getSqlSession().update(
                getSqlName(statement), ob);
    }

    /**
     * 通用查单
     */
    public Object getByObject(String statement, Object param) {
        return this.getSqlSession().selectOne(
                getSqlName(statement), param);
    }

    /**
     * 通用查多
     */
    public List<T> getListByObject(String statement, Object param){
        return this.getSqlSession().selectList(getSqlName(statement), param);
    }

    public<P>  List<P> getListByObject(P p, String statement,Object param){
        return this.getSqlSession().selectList(getSqlName(statement), param);
    }

    public  List<Map<String, Object>> getMapListByObject(String statement, Object param){
        return this.getSqlSession().selectList(getSqlName(statement), param);
    }

}
