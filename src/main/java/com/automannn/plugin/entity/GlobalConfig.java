package com.automannn.plugin.entity;

import com.automannn.plugin.ui.base.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局配置实体类
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalConfig implements Item {
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;
}
