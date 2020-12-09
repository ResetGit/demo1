package com.example.demo.service.Imp;

import com.example.demo.common.idworker.Sid;
import com.example.demo.mapper.ProductInfoMapper;
import com.example.demo.pojo.ProductInfo;
import com.example.demo.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ProductInfoServiceImp implements ProductInfoService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> productInfoList(String productName) {
        Example example = new Example(ProductInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("productName", "%" + productName + "%");
        if(productName==null || productName==""){
            List<ProductInfo> productInfos = this.productInfoMapper.selectAll();
            return productInfos;
        }else {
            List<ProductInfo> productInfos = this.productInfoMapper.selectByExample(example);
            return productInfos;
        }
    }

    //新增
    @Override
    public void insertProduct(ProductInfo productInfo) {
        //设置id
        productInfo.setProductId(Sid.next());
        this.productInfoMapper.insert(productInfo);
    }

    //修改
    @Transactional
    @Override
    public void updateProductInfo(ProductInfo productInfo) {
        this.productInfoMapper.updateByPrimaryKeySelective(productInfo);
    }

    //修改
    @Transactional
    @Override
    public void updatebyzt(ProductInfo productInfo) {
        this.productInfoMapper.updateByPrimaryKeySelective(productInfo);
    }

    //修改
    @Transactional
    @Override
    public void updatebybh(ProductInfo productInfo) {
        this.productInfoMapper.updateByPrimaryKeySelective(productInfo);
    }

    //删除
    @Override
    public void deleteProductInfo(String productId) {

        Example e = new Example(ProductInfo.class);
        Example.Criteria criteria = e.createCriteria();
        criteria.andEqualTo("productId",productId);

        this.productInfoMapper.deleteByExample(e);
    }

    //根据菜品id查询菜品
    @Override
    public ProductInfo queryProductInfoById(String productId) {

        Example example = new Example(ProductInfo.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("productId",productId);
        return this.productInfoMapper.selectOneByExample(example);
    }

}
