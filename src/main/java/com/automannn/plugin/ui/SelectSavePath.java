package com.automannn.plugin.ui;


import com.automannn.plugin.constants.MsgValue;
import com.automannn.plugin.constants.StrState;
import com.automannn.plugin.entity.*;
import com.automannn.plugin.service.CodeGenerateService;
import com.automannn.plugin.service.TableInfoService;
import com.automannn.plugin.tool.CacheDataUtils;
import com.automannn.plugin.tool.CurrGroupUtils;
import com.automannn.plugin.tool.ModuleUtils;
import com.automannn.plugin.tool.StringUtils;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ExceptionUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//import com.intellij.psi.PsiPackage;

/**
 * 选择保存路径
 *
 */
public class SelectSavePath extends JDialog {
    private static final String FRONT_PACKAGE= "src";

    /**
     * 后端源码选择主面板
     */
    private JPanel contentPane;
    /**
     * 前端源码选择主面板
     */
    private JPanel frontContentPane;
    /**
     * 确认按钮
     */
    private JButton buttonOK;
    /**
     * 取消按钮
     */
    private JButton buttonCancel;
    /**
     * 模型下拉框
     */
    private JComboBox<String> moduleComboBox;
    /**
     * 前端模型下拉框
     */
    private JComboBox<String> frontModuleComboBox;
    /**
     * 包字段
     */
    private JTextField packageField;
    /**
     * 路径字段
     */
    private JTextField pathField;
    /**
     * 前端路径 属性
    * */
    private  JTextField frontPathField;
    /**
     * 包选择按钮
     */
    private JButton packageChooseButton;
    /**
     * 路径选择按钮
     */
    private JButton pathChooseButton;
    /**
     * 模板全选框
     */
    private JCheckBox allCheckBox;

    /**
     * 前端模板全选框
     */
    private JCheckBox allFrontCheckBox;
    /**
     * 后端模板面板
     */
    private JPanel templatePanel;
    /**
     * 前端模板面板
     */
    private JPanel frontTemplatePanel;
    /**
     * 统一配置复选框
     */
    private JCheckBox unifiedConfig;
    /**
     * 禁止提示复选框
     */
    private JCheckBox titleConfig;
    /**
     * 前端路径选择按钮
     */
    private JButton frontPathChooseButton;
    private JCheckBox noAlert;
    /**
     * 所有后端模板复选框
     */
    private List<JCheckBox> checkBoxList = new ArrayList<>();

    /**
     * 所有前端模板复选框
     */
    private List<JCheckBox> frontCheckBoxList = new ArrayList<>();
    /**
     * 数据缓存工具类
     */
    private CacheDataUtils cacheDataUtils = CacheDataUtils.getInstance();
    /**
     * 模板组对象
     */
    private TemplateGroup templateGroup;
    /**
     * 前端模板组
     */
    private FrontTemplateGroup frontTemplateGroup;
    /**
     * 表信息服务
     */
    private TableInfoService tableInfoService;
    /**
     * 项目对象
     */
    private Project project;
    /**
     * 代码生成服务
     */
    private CodeGenerateService codeGenerateService;
    /**
     * 当前项目中的module
     */
    private List<Module> moduleList;

    /**
     * 构造方法
     */
    public SelectSavePath(Project project) {
        this.project = project;
        this.tableInfoService = TableInfoService.getInstance(project);
        this.codeGenerateService = CodeGenerateService.getInstance(project);
        this.templateGroup = CurrGroupUtils.getCurrTemplateGroup();
        this.frontTemplateGroup = CurrGroupUtils.getCurrFrontTemplateGroup();
        // 初始化module，存在资源路径的排前面
        this.moduleList = new LinkedList<>();
        for (Module module : ModuleManager.getInstance(project).getModules()) {
            // 存在源代码文件夹放前面，否则放后面
            if (ModuleUtils.existsSourcePath(module)) {
                this.moduleList.add(0, module);
            } else {
                this.moduleList.add(module);
            }
        }
        init();
        setTitle(MsgValue.TITLE_INFO);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * 获取已经选中的模板
     *
     * @return 模板对象集合
     */
    private List<Template> getSelectTemplate() {
        // 获取到已选择的复选框
        List<String> selectTemplateNameList = new ArrayList<>();
        checkBoxList.forEach(jCheckBox -> {
            if (jCheckBox.isSelected()) {
                selectTemplateNameList.add(jCheckBox.getText());
            }
        });
        List<Template> selectTemplateList = new ArrayList<>(selectTemplateNameList.size());
        if (selectTemplateNameList.isEmpty()) {
            return selectTemplateList;
        }
        // 将复选框转换成对应的模板对象
        templateGroup.getElementList().forEach(template -> {
            if (selectTemplateNameList.contains(template.getName())) {
                selectTemplateList.add(template);
            }
        });
        return selectTemplateList;
    }

    /**
     * 获取已经选中的前端模板
     *
     * @return 前端模板对象集合
     */
    private List<FrontTemplate> getSelectFrontTemplate() {
        // 获取到已选择的复选框
        List<String> selectFrontTemplateNameList = new ArrayList<>();
        frontCheckBoxList.forEach(jCheckBox -> {
            if (jCheckBox.isSelected()) {
                selectFrontTemplateNameList.add(jCheckBox.getText());
            }
        });
        List<FrontTemplate> selectFrontTemplateList = new ArrayList<>(selectFrontTemplateNameList.size());
        if (selectFrontTemplateNameList.isEmpty()) {
            return selectFrontTemplateList;
        }
        // 将复选框转换成对应的模板对象
        frontTemplateGroup.getElementList().forEach(template -> {
            if (selectFrontTemplateNameList.contains(template.getName())) {
                selectFrontTemplateList.add(template);
            }
        });
        return selectFrontTemplateList;
    }

    /**
     * 确认按钮回调事件
     */
    private void onOK() {
        List<Template> selectTemplateList = getSelectTemplate();

        List<FrontTemplate> selectFrontTemplateList = getSelectFrontTemplate();

        // 如果选择的模板是空的
        //要求单次任务，前后端模板不能同时为空
        if (selectTemplateList.isEmpty()&&selectFrontTemplateList.isEmpty()) {
            Messages.showWarningDialog("Can't Select Template!", MsgValue.TITLE_INFO);
            return;
        }
        String savePath = pathField.getText();
        String saveFrontPath = frontPathField.getText();
        if (StringUtils.isEmpty(savePath)&& StringUtils.isEmpty(saveFrontPath)) {
            Messages.showWarningDialog("Can't Select Save Path!", MsgValue.TITLE_INFO);
            return;
        }
        // 针对Linux系统路径做处理
        savePath = savePath.replace("\\", "/");
        saveFrontPath = saveFrontPath.replace("\\", "/");
        // 保存路径使用相对路径
        String basePath = project.getBasePath();
        if (!StringUtils.isEmpty(basePath) && savePath.startsWith(basePath)) {
            savePath = savePath.replace(basePath, ".");
        }
        if (!StringUtils.isEmpty(basePath) && saveFrontPath.startsWith(basePath)) {
            saveFrontPath = saveFrontPath.replace(basePath, ".");
        }

        TableInfo tableInfo = tableInfoService.getTableInfoAndConfig(cacheDataUtils.getSelectDbTable());
        //存在 后端代码保存路径，则 生成后端代码
        if(!".".equals(savePath)){
            // 保存配置
            tableInfo.setSavePath(savePath);
            tableInfo.setSavePackageName(packageField.getText());
            Module module = getSelectModule();
            if (module != null) {
                tableInfo.setSaveModuleName(module.getName());
            }
            tableInfoService.save(tableInfo);
            // 生成代码
            codeGenerateService.generateByUnifiedConfig(getSelectTemplate(), unifiedConfig.isSelected(), !titleConfig.isSelected());
        }

        //存在 前端代码保存路径，则  生成 前端代码
        if(!".".equals(saveFrontPath)){
            /**
             * 生成前端代码
             */
            tableInfo.setSavePath(saveFrontPath);
            Module frontModule = getFrontSelectModule();
            if (frontModule != null) {
                tableInfo.setSaveModuleName(frontModule.getName());
            }
            tableInfoService.save(tableInfo);
            codeGenerateService.generateFrontByUnifiedConfig(getSelectFrontTemplate(), unifiedConfig.isSelected(), !noAlert.isSelected());
        }

        // 关闭窗口
        dispose();
    }

    /**
     * 取消按钮回调事件
     */
    private void onCancel() {
        dispose();
    }

    /**
     * 初始化方法
     */
    private void init() {
        //添加模板组
        checkBoxList.clear();
        frontCheckBoxList.clear();

        templatePanel.setLayout(new GridLayout(6, 2));
        templateGroup.getElementList().forEach(template -> {
            JCheckBox checkBox = new JCheckBox(template.getName());
            checkBoxList.add(checkBox);
            templatePanel.add(checkBox);
        });

        frontTemplatePanel.setLayout(new GridLayout(6, 2));
        frontTemplateGroup.getElementList().forEach(frontTemplate->{
            JCheckBox checkBox = new JCheckBox(frontTemplate.getName());
            frontCheckBoxList.add(checkBox);
            frontTemplatePanel.add(checkBox);
        });

        //添加全选事件
        allCheckBox.addActionListener(e -> checkBoxList.forEach(jCheckBox -> jCheckBox.setSelected(allCheckBox.isSelected())));
        allFrontCheckBox.addActionListener(e->frontCheckBoxList.forEach(jCheckBox ->jCheckBox.setSelected(allFrontCheckBox.isSelected()) ));

        //初始化Module选择
        for (Module module : this.moduleList) {
            moduleComboBox.addItem(module.getName());
            //模块名称中，必须包括front，才会被识别为前端模块
            if (module.getName().toLowerCase().contains("front"))frontModuleComboBox.addItem(module.getName());
        }

        //监听module选择事件
        moduleComboBox.addActionListener(e -> {
            // 刷新路径
            refreshPath();
        });
        //监听module选择事件
        frontModuleComboBox.addActionListener(e -> {
            // 刷新前端路径
            refreshFrontPath();
        });

        //添加包选择事件
        packageChooseButton.addActionListener(e -> {
            // 这里不知道发生了什么，明明类是存在的但是编译时就是找不到，只用通过反射来使用
            try {
                // 构建dialog
                Class<?> cls = Class.forName("com.intellij.ide.util.PackageChooserDialog");
                Constructor<?> constructor = cls.getConstructor(String.class, Project.class);
                Object dialog = constructor.newInstance("Package Chooser", project);
                // 打开dialog窗口
                Method show = dialog.getClass().getMethod("show");
                show.invoke(dialog);
                // 获取选中的包信息
                Method getSelectedPackage = dialog.getClass().getMethod("getSelectedPackage");
                Object psiPackage = getSelectedPackage.invoke(dialog);
                // 获取名字
                if (psiPackage != null) {
                    Method getQualifiedName = psiPackage.getClass().getMethod("getQualifiedName");
                    String packageName = (String) getQualifiedName.invoke(psiPackage);
                    packageField.setText(packageName);
                    // 刷新路径
                    refreshPath();
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException | ClassNotFoundException e1) {
                // 抛出异常信息
                ExceptionUtil.rethrow(e1);
            }
        });

        // 添加包编辑框失去焦点事件
        packageField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // 刷新路径
                refreshPath();
            }
        });

        //初始化路径
        refreshPath();
        refreshFrontPath();

        //选择路径
        pathChooseButton.addActionListener(e -> {
            //将当前选中的model设置为基础路径
            VirtualFile path = LocalFileSystem.getInstance().findFileByPath(project.getBasePath());
            Module module = getSelectModule();
            if (module != null) {
                path = ModuleUtils.getSourcePath(module);
            }
            VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project, path);
            if (virtualFile != null) {
                pathField.setText(virtualFile.getPath());
            }
        });

        //选择前端路径
        frontPathChooseButton.addActionListener(e -> {
            //将当前选中的model设置为基础路径
            VirtualFile path = LocalFileSystem.getInstance().findFileByPath(project.getBasePath());
            Module module = getFrontSelectModule();
            if (module != null) {
                path = ModuleUtils.getSourcePath(module);
            }
            VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project, path);
            if (virtualFile != null) {
                frontPathField.setText(virtualFile.getPath());
            }
        });

        // 获取选中的表信息（鼠标右键的那张表），并提示未知类型
        TableInfo tableInfo = tableInfoService.getTableInfoAndConfig(cacheDataUtils.getSelectDbTable());
        // 设置默认配置信息
        if (!StringUtils.isEmpty(tableInfo.getSaveModuleName())) {
            moduleComboBox.setSelectedItem(tableInfo.getSaveModuleName());
        }
        if (!StringUtils.isEmpty(tableInfo.getSavePackageName())) {
            packageField.setText(tableInfo.getSavePackageName());
        }
        String savePath = tableInfo.getSavePath();
        if (!StringUtils.isEmpty(savePath)) {
            // 判断是否需要拼接项目路径
            if (savePath.startsWith(StrState.RELATIVE_PATH)) {
                String projectPath = project.getBasePath();
                savePath = projectPath + savePath.substring(1);
            }
            pathField.setText(savePath);
            //设置前端保存路径
            frontPathField.setText(savePath);
        }
    }

    /**
     * 获取选中的Module
     *
     * @return 选中的Module
     */
    private Module getSelectModule() {
        String name = (String) moduleComboBox.getSelectedItem();
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return ModuleManager.getInstance(project).findModuleByName(name);
    }


    /**
     * 获取前端选中的Module
     *
     * @return 选中的前端Module
     */
    private Module getFrontSelectModule() {
        String name = (String) frontModuleComboBox.getSelectedItem();
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return ModuleManager.getInstance(project).findModuleByName(name);
    }

    /**
     * 获取基本路径
     *
     * @return 基本路径
     */
    private String getBasePath() {
        Module module = getSelectModule();
        String baseDir = project.getBasePath();
        if (module != null) {
            baseDir = ModuleUtils.getSourcePath(module).getPath();
        }
        return baseDir;
    }

    /**
     * 刷新目录
     */
    private void refreshPath() {
        String packageName = packageField.getText();
        // 获取基本路径
        String path = getBasePath();
        // 兼容Linux路径
        path = path.replace("\\", "/");
        // 如果存在包路径，添加包路径
        if (!StringUtils.isEmpty(packageName)) {
            path += "/" + packageName.replace(".", "/");
        }
        pathField.setText(path);
    }

    /**
     * 刷新目录
     */
    private void refreshFrontPath() {
        String packageName = FRONT_PACKAGE;
        // 获取基本路径
        String path = getBasePath();
        // 兼容Linux路径
        path = path.replace("\\", "/");
        // 如果存在包路径，添加包路径
        if (!StringUtils.isEmpty(packageName)) {
            path += "/" + packageName.replace(".", "/");
        }
        frontPathField.setText(path);
    }

    /**
     * 打开窗口
     */
    public void open() {
        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
