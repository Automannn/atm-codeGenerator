package com.automannn.plugin.entity;

import lombok.Data;

import java.util.List;

/**
 * 全局配置分组
 *
 */
@Data
public class GlobalConfigGroup implements AbstractGroup<GlobalConfig> {
    /**
     * 分组名称
     */
    private String name;
    /**
     * 元素对象集合
     */
    private List<GlobalConfig> elementList;
}
