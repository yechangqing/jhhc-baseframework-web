package com.jhhc.baseframework.web.service;

import com.jhhc.baseframework.test.Base;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yecq
 */
public class Serivce1Test extends Base {

    @Autowired
    private Service1 sv;

    @Test
    public void test_getInfo() {
        Sret sr = sv.getInfo("1", "name");
        assertThat(sr.isOk(), is(true));
        String data = (String) sr.getData();
        assertThat(data, is("叶小怜"));
    }

    @Test
    public void test1_getInfo() {
        Sret sr = sv.getInfo("10", "name");
        assertThat(sr.isFail(), is(true));
        assertThat(sr.getData(), is(nullValue()));
    }

    @Test
    public void test_exception1() {
        Sret sr = sv.exception1();
        assertThat(sr.isFail(), is(true));
        assertThat(sr.getMessage(), is("状态错误"));
    }

    @Test
    public void test_exception2() {
        Sret sr = sv.exception2();
        assertThat(sr.isFail(), is(true));
        assertThat(sr.getMessage(), is("参数错误"));
    }

    @Test
    public void test_exception3() {
        Sret sr = sv.exception3();
        assertThat(sr.isFail(), is(true));
        assertThat(sr.getMessage(), is("操作错误"));
    }
}
