package com.automannn.plugin.constants;

/**
 * @author chenkh
 * @time 2020/6/7 15:15
 */
public enum FormItemSourceType {
    STATIC("1"),
    NORMAL_DICT("2"),
    SQL_DICT("3");


    private String value;

    FormItemSourceType(String value){
        this.value = value; }

    public String getValue() {
        return value;
    }
}
