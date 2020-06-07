package com.automannn.plugin.entity;

import com.automannn.plugin.tool.FileUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import lombok.Data;

/**
 * 需要保存的文件
 *
 */
@Data
public class SaveFile {
    private static final Logger LOG = Logger.getInstance(SaveFile.class);
    /**
     * 所属项目
     */
    private Project project;
    /**
     * 文件保存目录
     */
    private String path;
    /**
     * 需要保存的文件
     */
    private PsiFile file;
    /**
     * 是否需要重新格式化代码
     */
    private boolean reformat;
    /**
     * 文件
     */
    private PsiFile psiFile;
    /**
     * 是否显示操作提示
     */
    private boolean operateTitle;

    /**
     * 构建对象
     *
     * @param path     路径
     * @param fileName 文件名
     * @param reformat 是否重新格式化代码
     */
    public SaveFile(Project project, String path, String fileName, String content, boolean reformat, boolean operateTitle) {
        this.path = path;
        this.project = project;
        // 构建文件对象
        PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
        LOG.assertTrue(content != null);
        // 换行符统一使用\n
        this.file = psiFileFactory.createFileFromText(fileName, FileTypes.UNKNOWN, content.replace("\r", ""));
        this.reformat = reformat;
        this.operateTitle = operateTitle;
    }

    /**
     * 写入文件
     */
    public void write() {
        FileUtils.getInstance().write(this);
    }
}
