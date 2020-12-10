package com.example.demo.controller;

import com.example.demo.pojo.SellerInfo;
import com.example.demo.service.SellerInfoService;
import com.example.demo.util.IMoocJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SellerController {

    @Autowired
    private SellerInfoService sellerInfoService;
    @PostMapping("/sellerList")
    public IMoocJSONResult sellerList(){
        List<SellerInfo> sellerInfos = this.sellerInfoService.sellerList();
        return IMoocJSONResult.ok(sellerInfos);
    }
}
