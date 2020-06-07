package com.automannn.plugin.entity;

import lombok.Data;

/**
 * 回调实体类
 *
 */
@Data
public class Callback {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 保存路径
     */
    private String savePath;
    /**
     * 是否重新格式化代码
     */
    private boolean reformat = true;
}
