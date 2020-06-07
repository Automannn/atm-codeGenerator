package com.automannn.plugin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author automannn@163.com
 * @time 2020/5/24 19:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrontTemplate {
    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板代码
     */
    private String code;
}
