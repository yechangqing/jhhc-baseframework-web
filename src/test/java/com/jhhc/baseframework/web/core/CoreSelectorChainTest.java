package com.jhhc.baseframework.web.core;

import com.jhhc.baseframework.test.Base;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author yecq
 */
public class CoreSelectorChainTest extends Base {

    private String invoke(Object o, String name) {

        Class cls = o.getClass();
        try {
            Method method = cls.getDeclaredMethod(name);
            method.setAccessible(true);
            Object ret = method.invoke(o);
            return (String) ret;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new IllegalStateException("方法" + name + "执行出错");
        }

    }

    private List getField(Object o, String name) {
        Class cls = o.getClass();
        try {
            Field field = cls.getDeclaredField(name);
            field.setAccessible(true);
            Object ret = field.get(o);
            return (List) ret;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new IllegalStateException("获取属性" + name + "出错");
        }
    }

    @Test
    public void test_CoreSelectorChain() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表名为空");
        new CoreSelectorChain(null);
    }

    @Test
    public void test_CoreSelectorChain1() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表名为空");
        new CoreSelectorChain(" ");
    }

    @Test
    public void test_CoreSelectorChain2() {
        CoreSelectorChain col = new CoreSelectorChain("info1");
        List header = getField(col, "header");
        assertThat(header.size(), is(0));
        List where = getField(col, "where");
        assertThat(where.size(), is(0));
        List order = getField(col, "order");
        assertThat(order.size(), is(0));
    }

    @Test
    public void test_select() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.select("username");
        List<String> header = getField(col, "header");
        assertThat(header.size(), is(1));
        assertThat(header.get(0), is("username"));
        col.select("passwd");
        assertThat(header.size(), is(2));
        assertThat(header.get(0), is("username"));
        assertThat(header.get(1), is("passwd"));
    }

    @Test
    public void test_generateHeaders() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.select("username");
        String sql = invoke(col, "generateHeaders");
        assertThat(sql, is("username"));

        col.select("passwd");
        sql = invoke(col, "generateHeaders");
        assertThat(sql, is("username,passwd"));
    }

    @Test
    public void test_select1() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.select(new String[]{"username", "passwd"});
        List<String> header = getField(col, "header");
        assertThat(header.size(), is(2));
        assertThat(header.get(0), is("username"));
        assertThat(header.get(1), is("passwd"));
    }

    @Test
    public void test_generateHeaders1() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.select(new String[]{"username", "passwd"});
        String sql = invoke(col, "generateHeaders");
        assertThat(sql, is("username,passwd"));
    }

    @Test
    public void test_select2() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        List<String> header = getField(col, "header");
        assertThat(header.size(), is(0));
    }

    @Test
    public void test_generateHeaders2() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        String sql = invoke(col, "generateHeaders");
        assertThat(sql, is("*"));
    }

    @Test
    public void test_where() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        List li = getField(col, "where");
        assertThat(li.size(), is(0));
    }

    @Test
    public void test_generateWhere() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        String sql = invoke(col, "generateWhere");
        assertThat(sql, is(""));
    }

    @Test
    public void test_where1() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.where("name='yecq'");
        List where = getField(col, "where");
        assertThat(where.size(), is(1));
    }

    @Test
    public void test_generateWhere1() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.where("name='yecq'");
        String sql = invoke(col, "generateWhere");
        assertThat(sql, is("name='yecq'"));
    }

    @Test
    public void test_where2() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.where("name='yecq'");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("指明是and还是or");
        col.where("passwd='1234'");
    }

    @Test
    public void test_where3() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("where语句为空");
        col.where(" ");
    }

    @Test
    public void test_whereAnd() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("where语句为空");
        col.whereAnd(" ");
    }

    @Test
    public void test_whereAnd1() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.whereAnd("username='yecq'");
        List list = getField(col, "where");
        assertThat(list.size(), is(1));
    }

    @Test
    public void test_generateWhere11() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.whereAnd("username='yecq'");
        String sql = invoke(col, "generateWhere");
        assertThat(sql, is("username='yecq'"));
    }

    @Test
    public void test_whereAnd2() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.where("username='yecq'");
        col.whereAnd("passwd='1234'");
        List li = getField(col, "where");
        assertThat(li.size(), is(2));
    }

    @Test
    public void test_generateWhere12() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.where("username='yecq'");
        col.whereAnd("passwd='1234'");
        String sql = invoke(col, "generateWhere");
        assertThat(sql, is("username='yecq' and passwd='1234'"));
    }

    @Test
    public void test_whereOr() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("where语句为空");
        col.whereOr(" ");
    }

    @Test
    public void test_whereOr1() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.whereOr("passwd='2323'");
        List li = getField(col, "where");
        assertThat(li.size(), is(1));
    }

    @Test
    public void test_whereOr2() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.where("username='yecq'");
        col.whereOr("name='叶小怜'");
        List li = getField(col, "where");
        assertThat(li.size(), is(2));
    }

    @Test
    public void test_generateWhere22() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.where("username='yecq'");
        col.whereOr("name='叶小怜'");
        String sql = invoke(col, "generateWhere");
        assertThat(sql, is("username='yecq' or name='叶小怜'"));
    }

    @Test
    public void test_orderBy() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.orderBy("username");
        List li = getField(col, "order");
        assertThat(li.size(), is(1));

        col.orderBy("passwd");
        li = getField(col, "order");
        assertThat(li.size(), is(2));
    }

    @Test
    public void test_generateOrderBy() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.orderBy("username");
        String sql = invoke(col, "generateOrderBy");
        assertThat(sql, is("username"));

        col.orderBy("name");
        sql = invoke(col, "generateOrderBy");
        assertThat(sql, is("username,name"));
    }

    @Test
    public void test_orderBy1() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.orderBy(new String[]{"name", "passwd", "id"});
        List li = getField(col, "order");
        assertThat(li.size(), is(3));
    }

    @Test
    public void test_generateOrderBy1() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.orderBy(new String[]{"name", "passwd", "id"});
        String sql = invoke(col, "generateOrderBy");
        assertThat(sql, is("name,passwd,id"));
    }

    @Test
    public void test_generateSqlAndExecute() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        String sql = invoke(col, "generateSql");
        System.out.println(sql);
        assertThat(sql, is("select * from core1"));
        col.execute();

        col = new CoreSelectorChain("core1");
        col.select("username");
        sql = invoke(col, "generateSql");
        System.out.println(sql);
        assertThat(sql, is("select username from core1"));
        col.execute();

        col = new CoreSelectorChain("core1");
        col.select("username");
        col.select("name");
        sql = invoke(col, "generateSql");
        System.out.println(sql);
        assertThat(sql, is("select username,name from core1"));
        col.execute();

        col = new CoreSelectorChain("core1");
        col.select(new String[]{"username", "id"});
        sql = invoke(col, "generateSql");
        System.out.println(sql);
        assertThat(sql, is("select username,id from core1"));
        col.execute();
    }

    @Test
    public void test_generateSqlAndExecute1() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.where("username='yecq'");
        String sql = invoke(col, "generateSql");
        System.out.println(sql);
        assertThat(sql, is("select * from core1 where username='yecq'"));
        col.execute();

        col = new CoreSelectorChain("core1");
        col.select("id");
        col.select("name");
        col.where("id>1");
        col.whereAnd("name='王小二'");
        sql = invoke(col, "generateSql");
        System.out.println(sql);
        assertThat(sql, is("select id,name from core1 where id>1 and name='王小二'"));
        col.execute();

        col = new CoreSelectorChain("core1");
        col.select(new String[]{"passwd", "username"});
        col.where("passwd!='1223'");
        col.whereOr("id>2");
        col.whereAnd("passwd!='3344'");
        sql = invoke(col, "generateSql");
        System.out.println(sql);
        assertThat(sql, is("select passwd,username from core1 where passwd!='1223' or id>2 and passwd!='3344'"));
        col.execute();
    }

    @Test
    public void test_generateSqlAndExecute2() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.orderBy("id");
        String sql = invoke(col, "generateSql");
        System.out.println(sql);
        assertThat(sql, is("select * from core1 order by id"));

        col = new CoreSelectorChain("core1");
        col.select(new String[]{"username", "name"});
        col.orderBy(new String[]{"name", "id"});
        col.where("id>2");
        col.whereAnd("id<10");
        sql = invoke(col, "generateSql");
        System.out.println(sql);
        assertThat(sql, is("select username,name from core1 where id>2 and id<10 order by name,id"));
    }

    @Test
    public void test_execute1() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.where("passwd='123456'");
        col.orderBy("id");
        List<SubCore> list = col.execute(SubCore.class);
        assertThat(list.size(), is(2));
        SubCore sc = list.get(0);
        assertThat(sc.getInfo("id") + "", is("2"));
        assertThat(sc.getInfo("email") + "", is("pkj@123.com"));
        sc = list.get(1);
        assertThat(sc.getId(), is("3"));
        assertThat(sc.getInfo("email", String.class), is("abc@123.com"));
    }

    @Test
    public void test_execute2() {
        CoreSelectorChain col = new CoreSelectorChain("core1");
        col.select("username");
        col.where("passwd='123456'");
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("没有选取id");
        List<SubCore> list = col.execute(SubCore.class);
    }
}
