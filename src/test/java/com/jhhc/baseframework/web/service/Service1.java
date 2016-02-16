package com.jhhc.baseframework.web.service;

import com.jhhc.baseframework.web.core.CoreTable;
import com.jhhc.baseframework.web.core.SubCore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author yecq
 */
@Service
@Transactional
public class Service1 {

    public Sret getInfo(String id, String key) {
        CoreTable core = new SubCore(id);
        String v = core.getInfo(key) + "";
        Sret sr = new Sret();
        sr.setOk();
        sr.setData(v);
        return sr;
    }

    public Sret exception1() {
        throw new IllegalStateException("状态错误");
    }

    public Sret exception2() {
        throw new IllegalArgumentException("参数错误");
    }

    public Sret exception3() {
        throw new UnsupportedOperationException("操作错误");
    }
}
