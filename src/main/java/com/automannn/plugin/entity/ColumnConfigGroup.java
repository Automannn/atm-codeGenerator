package com.automannn.plugin.entity;

import lombok.Data;

import java.util.List;

/**
 * 列配置分组
 *
 */
@Data
public class ColumnConfigGroup implements AbstractGroup<ColumnConfig> {
    /**
     * 分组名称
     */
    private String name;
    /**
     * 元素对象
     */
    private List<ColumnConfig> elementList;
}
