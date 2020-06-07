package com.automannn.plugin.constants;

/**
 * @author chenkh
 * @time 2020/6/7 15:10
 */
public enum FormItemType {
    INPUT("1"),
    INPUT_NUMBER("2"),
    SELECT("3"),
    DATETIME("4"),
    RADIO("5"),
    CHECK("6");

    private String value;

    FormItemType(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
