package com.automannn.plugin.entity;

import lombok.Data;

import java.util.List;

/**
 * 模板分组类
 *
 */
@Data
public class TemplateGroup implements AbstractGroup<Template> {
    /**
     * 分组名称
     */
    private String name;
    /**
     * 元素对象
     */
    private List<Template> elementList;
}
