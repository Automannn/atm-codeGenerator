package com.automannn.plugin.constants;

/**
 * @author chenkh
 * @time 2020/6/7 15:25
 */
public enum FormItemRule {
    NONE("-1"),
    MOBILE("1"),
    EMAIL("2");


    private String value;

    FormItemRule(String value){
        this.value = value; }

    public String getValue() {
        return value;
    }
}
