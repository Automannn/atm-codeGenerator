package com.automannn.plugin.entity;

import lombok.Data;

import java.util.List;

/**
 * @author automannn@163.com
 * @time 2020/5/24 19:41
 */
@Data
public class FrontTemplateGroup {
    /**
     * 分组名称
     */
    private String name;

    /**
     * 元素对象
     */
    private List<FrontTemplate> elementList;
}
