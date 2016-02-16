package com.jhhc.baseframework.web.controller;

import com.jhhc.baseframework.web.service.Sret;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author yecq
 */
@Service
@Transactional
public class UserService {

    public Sret getUserInfo(UsernameBean bean) {
        Sret sr = new Sret();
        sr.setOk();
        sr.setData("liming");
        return sr;

    }

    public Sret add(UsernameBean bean) {
        throw new IllegalStateException("不允许插入");
    }

    public Sret detail(UsernameBean bean) {
        Sret sr = new Sret();
        sr.setOk("已获取了细节");
        return sr;
    }

    public Sret show(UsernameBean bean) {
        Sret sr = new Sret();
        sr.setOk("效果已展示");
        return sr;
    }
}
