package com.automannn.plugin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intellij.database.model.DasColumn;
import lombok.Data;

import java.util.Map;

/**
 * 列信息
 *
 */
@Data
public class ColumnInfo {
    /**
     * 原始对象
     */
    @JsonIgnore
    private DasColumn obj;
    /**
     * 原始名称
     */
    private String srcName;

    /**
     * 注释
     */
    private String comment;

    /**
     * 全类型
     */
    private String type;
    /**
     * 短类型
     */
    private String shortType;
    /**
     * 标记是否为自定义附加列
     */
    private boolean custom;
    /**
     * 扩展数据
     */
    private Map<String, Object> ext;


    /*====================扩展==============*/
    /**
     * 标签
     */
    private String labelName;

    /**
     * 转换名称
     */
    private String targetName;

    /**
     * 表单数据类型
     */
    private String formItemType;

    /**
     * 表单数据源 类型
     */
    private String formItemSourceType;

    /**
     * 表单数据源
     */
    private String formItemSource;

    /**
     * 表单验证规则
     */
    private String formItemRules;

    /**
     * 是否必填
     */
    private String require;

    /**
     * 是否隐藏
     */
    private String hidden;

}
