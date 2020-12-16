package com.example.demo.service.Imp;

import com.example.demo.service.ComboService;
import com.example.demo.common.idworker.Sid;
import com.example.demo.mapper.ComboMapper;
import com.example.demo.pojo.Combo;
import com.example.demo.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ComboServiceImp implements ComboService {

    @Autowired
    private ComboMapper comboMapper;

    @Override
    public List<Combo> ComboList(String name) {
        Example example = new Example(Combo.class);
        Example.Criteria criteria = example.createCriteria();
        if(name == null || name == ""){
            List<Combo> list = comboMapper.selectAll();
            return list;
        }else {
            criteria.andLike("comboname", "%" + name + "%");
            List<Combo> list = comboMapper.selectByExample(example);
            return list;

        }
    }

    @Override
    public Integer addCombo(Map map) {
        String data =new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date());
        Combo combo = new Combo();
        combo.setId(Sid.next());
        combo.setComboname((String) map.get("comboname"));
        combo.setComboprice((String) map.get("comboprice"));
        combo.setDay((String)map.get("day"));
        combo.setCreateTime(data);
        combo.setUpdateTime(data);
        int i = comboMapper.insert(combo);
        return i;
    }

    @Override
    public String editCombo(Combo combo) {
        String date = new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date());
        combo.setUpdateTime(date);
        Example example = new Example(Combo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",combo.getId());
        int i = comboMapper.updateByExampleSelective(combo,example);
        return String.valueOf(i);
    }

    @Override
    public String delCombo(String id) {
        Example example = new Example(Combo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",id);
        int i = comboMapper.deleteByExample(example);
        return String.valueOf(i);
    }

}
