package com.jhhc.baseframework.web.controller.restful;

import com.jhhc.baseframework.web.service.Sret;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author yecq
 */
public abstract class RestfulControllerBase {

    // 判断是否未带有参数
    protected boolean isNoParam(HttpServletRequest request) {
        return request.getParameterMap().isEmpty();
    }

    // 判断是否含有某个参数
    protected boolean hasParam(HttpServletRequest request, String name) {
        return request.getParameterMap().containsKey(name);
    }

    // 判断是否含有一些列参数
    protected boolean hasParam(HttpServletRequest request, String[] names) {
        if (names == null) {
            return false;
        }
        Map map = request.getParameterMap();
        for (int i = 0; i < names.length; i++) {
            if (!map.containsKey(names[i])) {
                return false;
            }
        }
        return true;
    }

    // 获得所有的参数，这里就不考虑重复了
    protected Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String> ret = new HashMap();
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement().trim();
            ret.put(name, request.getParameter(name));
        }
        return ret;
    }

    protected ResponseEntity getSretEntityRet(Sret sr, Map<String, String> param) {
        // 暂时只返回OK
        HttpHeaders header = new HttpHeaders();
        header.add("status", sr.getStatus());
        header.add("message", sr.getMessage());
        // 加上其余属性
        Iterator<Entry<String, String>> ite = param.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, String> ent = ite.next();
            header.add(ent.getKey(), ent.getValue());
        }
        return new ResponseEntity(sr.getData(), header, HttpStatus.OK);
    }

    protected ResponseEntity getObjectEntityRet(Object o, Map<String, String> param) {
        Sret sr = new Sret();
        sr.setOk();
        sr.setData(o);
        return getSretEntityRet(sr, param);
    }
}
