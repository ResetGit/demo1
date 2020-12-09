package com.example.demo.service.Imp;


import com.example.demo.common.idworker.Sid;
import com.example.demo.mapper.SellerInfoMapper;
import com.example.demo.pojo.SellerInfo;
import com.example.demo.service.SellerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SellerInfoServiceImp implements SellerInfoService {

    @Autowired
    private SellerInfoMapper sellerInfoMapper;

    @Override
    public List<SellerInfo> sellerList() {

        List<SellerInfo> sellerInfos = this.sellerInfoMapper.selectAll();

        return sellerInfos;
    }

    @Override
    public SellerInfo querySellerByOppenid(String openid) {

        Example example = new Example(SellerInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openid",openid);

        SellerInfo sellerInfo = this.sellerInfoMapper.selectOneByExample(example);

        return sellerInfo;
    }

    @Override
    public void saveSeller(SellerInfo seller) {

        seller.setId(Sid.next());

        this.sellerInfoMapper.insert(seller);

    }
}
