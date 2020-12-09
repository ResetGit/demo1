package com.example.demo.service.Imp;

import com.example.demo.mapper.ProductCategoryMapper;
import com.example.demo.mapper.ProductInfoMapper;
import com.example.demo.pojo.ProductCategory;
import com.example.demo.pojo.ProductInfo;
import com.example.demo.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ProductCategoryServiceImp implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductCategory> categoryList(String categoryName) {
        Example example = new Example(ProductCategory.class);
        Example.Criteria criteria = example.createCriteria();


        //模糊查询
        if(categoryName==null || categoryName=="" ){
            List<ProductCategory> productCategories = this.productCategoryMapper.selectAll();
            return productCategories;
        }else{
            criteria.andLike("categoryName", "%" + categoryName + "%");
            List<ProductCategory> productCategories = this.productCategoryMapper.selectByExample(example);
//            if(productCategories==null){
//                List<ProductCategory> productCategories1 = this.productCategoryMapper.selectAll();
//                return productCategories1;
//            }
            return productCategories;
        }

    }

    @Override
    public List<ProductInfo> queryProductInfoByCategory(int categoryType) {

        Example example = new Example(ProductInfo.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("categoryType",categoryType);

        List<ProductInfo> selects = this.productInfoMapper.selectByExample(example);

        return selects;
    }



    //新增
    @Override
    public void insertCategory(ProductCategory productCategory) {
        this.productCategoryMapper.insert(productCategory);
    }

    //删除
    @Override
    public void delCategory(String categoryId) {

        Example e = new Example(ProductCategory.class);
        Example.Criteria criteria = e.createCriteria();
        criteria.andEqualTo("categoryId",categoryId);

        this.productCategoryMapper.deleteByExample(e);
    }

    //修改
    @Override
    public void editCategory(ProductCategory productCategory) {

        Example e = new Example(ProductCategory.class);
        Example.Criteria criteria = e.createCriteria();
        criteria.andEqualTo("categoryId",productCategory.getCategoryId());

        this.productCategoryMapper.updateByExample(productCategory,e);
    }
}
