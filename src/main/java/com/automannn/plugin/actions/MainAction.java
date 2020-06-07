package com.automannn.plugin.actions;

import com.automannn.plugin.service.TableInfoService;
import com.automannn.plugin.tool.CacheDataUtils;
import com.automannn.plugin.ui.SelectSavePath;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public class MainAction extends AnAction {
    /**
     * 构造方法
     *
     * @param text 菜单名称
     */
    MainAction(@Nullable String text) {
        super(text);
    }

    /**
     * 处理方法
     *
     * @param event 事件对象
     */
    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }

        // 校验类型映射
        if (!TableInfoService.getInstance(project).typeValidator(CacheDataUtils.getInstance().getSelectDbTable())) {
            // 没通过不打开窗口
            return;
        }
        //开始处理
        new SelectSavePath(event.getProject()).open();
    }
}
