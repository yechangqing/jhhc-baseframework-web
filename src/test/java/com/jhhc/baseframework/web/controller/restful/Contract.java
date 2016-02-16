package com.jhhc.baseframework.web.controller.restful;

/**
 * 某种资源
 *
 * @author yecq
 */
public class Contract {

    private String id;
    private String code;
    private String name;
    private double margin;
    private int unit;
    private String exchange;

    public Contract() {
    }

    public Contract(String id, String code, String name, double margin, int unit, String exchange) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.margin = margin;
        this.unit = unit;
        this.exchange = exchange;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
