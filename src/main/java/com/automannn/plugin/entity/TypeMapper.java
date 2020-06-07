package com.automannn.plugin.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类型映射信息
 *
 */
@Data
@NoArgsConstructor
public class TypeMapper {
    /**
     * 列类型
     */
    private String columnType;
    /**
     * java类型
     */
    private String javaType;

    public TypeMapper(String columnType, String javaType) {
        this.columnType = columnType;
        this.javaType = javaType;
    }
}
