package com.jhhc.baseframework.web.controller.restful;

import com.jhhc.baseframework.web.service.Sret;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author yecq
 */
@Service
@Transactional
public class RestfulService {

    private Map<String, Contract> data;

    public RestfulService() {
        this.data = new HashMap();
    }

    public void init() {
        this.data.put("1", new Contract("1", "rb1605", "螺纹钢", 0.09, 10, "上海"));
        this.data.put("2", new Contract("2", "m1609", "豆粕", 0.08, 10, "大连"));
        this.data.put("3", new Contract("3", "ta1610", "甲酸", 0.12, 5, "郑州"));
    }

    public void clear() {
        this.data.clear();
    }

    // 返回所有合约
    public Sret getAllContract() {
        Sret sr = new Sret();
        List<Contract> list = new LinkedList(this.data.values());
        sr.setData(list);
        return sr;
    }

    // 返回某个id的合约
    public Sret getContract(String id) {
        Sret sr = new Sret();
        Contract con = this.data.get(id);
        if (con == null) {
            throw new IllegalStateException("不存在此对象id=" + id);
        }
        sr.setData(this.data.get(id));
        return sr;
    }

    // 创建一个对象
    public Sret add(Contract con) {
        // ...
        Sret sr = new Sret();
        sr.setOk("创建成功");
        sr.setData("4");
        return sr;
    }

    // 修改一个对象
    public Sret modify(String id, Contract con) {
        Sret sr = new Sret();
        Contract c = this.data.get(id);
        c.setMargin(con.getMargin());
        sr.setOk("修改成功");
        return sr;
    }

    // 删除一个对象
    public Sret remove(String id) {
        this.data.remove(id);
        Sret sr = new Sret();
        sr.setOk("删除成功");
        return sr;
    }
}
