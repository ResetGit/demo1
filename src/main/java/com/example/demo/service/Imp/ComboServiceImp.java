package com.example.demo.service.Imp;

import com.example.demo.service.ComboService;
import com.example.demo.common.idworker.Sid;
import com.example.demo.mapper.ComboMapper;
import com.example.demo.pojo.Combo;
import com.example.demo.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ComboServiceImp implements ComboService {

    @Autowired
    private ComboMapper comboMapper;

    @Override
    public List<Combo> ComboList() {
        List<Combo> list = comboMapper.selectAll();
        return list;
    }

    @Override
    public void addCombo(Map map) {
        String data =new SimpleDateFormat("yyMMdd").format(new Date());
        Combo combo = new Combo();
        combo.setId(Sid.next());
        combo.setComboname((String) map.get("combname"));
        combo.setComboprice((String) map.get("comboprice"));
        combo.setCreateTime(data);
        combo.setUpdateTime(data);
    }

    @Override
    public String editCombo(String id) {
        return null;
    }

    @Override
    public String delCombo(String id) {
        return null;
    }
}
