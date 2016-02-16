package com.jhhc.baseframework.web.controller.restful;

import com.jhhc.baseframework.test.IntegrateRestfulBase;
import com.jhhc.baseframework.test.TestReturn;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author yecq
 */
public class JspControllerTest extends IntegrateRestfulBase {

    @Test
    public void test_getJsp0() {
        TestReturn ret = doPost("/jsp");
    }

    @Test
    public void test_getJsp00() {
        TestReturn ret = doPost("/jsp/3");
    }

    @Test
    public void test_getJsp1() {
        TestReturn ret = doPost("/jsp/jsp1");
    }

    @Test
    public void test_getJsp12() {
        Map map = new HashMap();
        map.put("name", "ABCD");
        TestReturn ret = doPost("/jsp/jsp1/4", map);
    }

    @Test
    public void test_doJsp10() {
        TestReturn ret = doPost("/jsp10");
    }

    @Test
    public void test_getJsp1000() {
        Map map = new HashMap();
        map.put("name", "QWER");
        TestReturn ret = doPost("/function/2/branch/5", map);
    }
}
