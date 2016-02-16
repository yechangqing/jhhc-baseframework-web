package com.jhhc.baseframework.web.user;

import java.util.Map;

/**
 *
 * @author yecq
 */
public interface User {

    // 哪些字段必须是唯一的
    String[] unique();

    // 检查用户是否存在
    boolean exist();

    // 验证用户名密码，正确返回id，错误返回null
    String check();

    // 增加用户
    String add();

    // 以下为已有用户功能
    // 删除用户
    void remove();

    // 修改用户信息
    void modify(Map<String, Object> hv);

    // 获取用户信息
    Object getInfo(String key);

    // 验证密码
    boolean verify(String passwd1);

    // 修改密码
    void modifyPasswd(String newPasswd);
}
