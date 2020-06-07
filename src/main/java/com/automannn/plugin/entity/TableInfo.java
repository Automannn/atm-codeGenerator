package com.automannn.plugin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intellij.database.psi.DbTable;
import lombok.Data;

import java.util.List;

/**
 * 表信息
 *
 */
@Data
public class TableInfo {
    /**
     * 原始对象
     */
    @JsonIgnore
    private DbTable obj;
    /**
     * 表名（首字母大写）
     */
    private String name;
    /**
     * 注释
     */
    private String comment;
    /**
     * 所有列
     */
    private List<ColumnInfo> fullColumn;
    /**
     * 主键列
     */
    private List<ColumnInfo> pkColumn;
    /**
     * 其他列
     */
    private List<ColumnInfo> otherColumn;
    /**
     * 保存的包名称
     */
    private String savePackageName;
    /**
     * 保存路径
     */
    private String savePath;
    /**
     * 保存的module名称
     */
    private String saveModuleName;
}
