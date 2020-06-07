package com.automannn.plugin.entity;

import lombok.Data;

import java.util.List;

/**
 * 类型映射分组
 *
 */
@Data
public class TypeMapperGroup implements AbstractGroup<TypeMapper> {
    /**
     * 分组名称
     */
    private String name;
    /**
     * 元素对象
     */
    private List<TypeMapper> elementList;
}
