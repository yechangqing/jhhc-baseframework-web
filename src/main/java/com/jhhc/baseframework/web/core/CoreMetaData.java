package com.jhhc.baseframework.web.core;

/**
 * 查看底层数据库的细节
 *
 * @author yecq
 */
public interface CoreMetaData {

    String getType();   // 是表还是视图

    String getTableName();  // 返回表名或者视图名

    String[] getHeaders();  // 返回表头

    String[] getTablesOfView(); // 返回视图的各个表

}
