package com.jhhc.baseframework.web.core;

import com.jhhc.baseframework.record.Record;
import com.jhhc.baseframework.record.RecordView;
import com.jhhc.baseframework.record.SqlOperator;
import javax.annotation.PostConstruct;
import javax.management.relation.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author yecq
 */
@Component
public class Root {

    private static Root single = null;

    public static Root getInstance() {
        synchronized (Root.class) {
            if (single == null) {
                throw new IllegalArgumentException("Root对象没有被注入");
            }
        }
        return single;
    }

    private Root() {
    }

    @Autowired
    private ApplicationContext ctx;

    // 每次初始化后执行下面的操作
    @PostConstruct
    public void init() {
        single = this;
    }

    public ApplicationContext getApplicationContext() {
        return ctx;
    }

    public SqlOperator getSqlOperator() {
        return getBean(SqlOperator.class);
    }

    public Record getRecord() {
        return getBean(Record.class);
    }

    public RecordView getRecordView() {
        return getBean(RecordView.class);
    }

    public Relation getRelation() {
        return getBean(Relation.class);
    }

    public <T> T getBean(String name, Class<T> cls) {
        return ctx.getBean(name, cls);
    }

    public <T> T getBean(Class<T> cls) {
        return ctx.getBean(cls);
    }

    public Object getBean(String name, Object... os) {
        return ctx.getBean(name, os);
    }
}
