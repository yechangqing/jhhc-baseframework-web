package com.jhhc.baseframework.web.core;

import java.util.Map;

/**
 *
 * @author yecq
 */
public interface Core {

    // 获得id，作为主键
    String getId();

    // 增加
    String add();

    // 删除
    void remove();

    // 修改
    void modify(Map<String, Object> hv1);

    // 查询信息
    Map<String, Object> getInfo();

    // 查询信息
    Object getInfo(String key);

    // 查询信息
    <T> T getInfo(String key, Class<T> cls);

    // 返回底层数据库元数据
    CoreMetaData getMetaData();
}
