package com.automannn.plugin.entity;

import com.automannn.plugin.ui.base.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模板信息类
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Template implements Item {
    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板代码
     */
    private String code;
}
