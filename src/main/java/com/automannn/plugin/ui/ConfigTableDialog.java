package com.automannn.plugin.ui;

import com.automannn.plugin.constants.MsgValue;
import com.automannn.plugin.entity.*;
import com.automannn.plugin.service.TableInfoService;
import com.automannn.plugin.tool.CacheDataUtils;
import com.automannn.plugin.tool.CurrGroupUtils;
import com.automannn.plugin.tool.StringUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.BooleanTableCellEditor;
import com.intellij.util.ui.ComboBoxCellEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

/**
 * 表配置窗口
 *
 */
public class ConfigTableDialog extends JDialog {
    /**
     * 主面板
     */
    private JPanel contentPane;
    /**
     * 确认按钮
     */
    private JButton buttonOK;
    /**
     * 取消按钮
     */
    private JButton buttonCancel;
    /**
     * 表展示
     */
    private JTable table;
    /**
     * 添加按钮
     */
    private JButton addButton;
    /**
     * 缓存数据工具类
     */
    private CacheDataUtils cacheDataUtils = CacheDataUtils.getInstance();
    /**
     * 表信息服务
     */
    private TableInfoService tableInfoService;
    /**
     * 默认的表模型
     */
    private DefaultTableModel tableModel;
    /**
     * 列配置
     */
    private List<ColumnConfig> columnConfigList;
    /**
     * 表信息对象
     */
    private TableInfo tableInfo;
    /**
     * 初始化标记
     */
    private boolean initFlag;

    /**
     * 构造方法
     */
    public ConfigTableDialog(Project project) {
        this.tableInfoService = TableInfoService.getInstance(project);
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

        init();
        initEvent();
    }

    /**
     * 确认按钮回调
     */
    private void onOK() {
        tableInfoService.save(tableInfo);
        // add your code here
        dispose();
    }

    /**
     * 取消按钮回调
     */
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    /**
     * 初始化方法
     */
    private void init() {
        initFlag = false;
        ColumnConfigGroup columnConfigGroup = CurrGroupUtils.getCurrColumnConfigGroup();
        // 拿到列配置信息
        columnConfigList = getInitColumn(columnConfigGroup.getElementList());
        //读取表配置信息（一次只能配置一张表）
        tableInfo = tableInfoService.getTableInfoAndConfig(cacheDataUtils.getSelectDbTable());

        refresh();

        initFlag = true;
    }

    /**
     * 刷新界面
     */
    private void refresh() {
        tableModel = new DefaultTableModel();
        columnConfigList.forEach(columnConfig -> tableModel.addColumn(columnConfig.getTitle()));
        //追加数据
        tableInfo.getFullColumn().forEach(columnInfo -> {
            List<Object> dataList = new ArrayList<>();
            dataList.add(columnInfo.getSrcName());
            dataList.add(columnInfo.getTargetName());
            dataList.add(columnInfo.getType());
            dataList.add(columnInfo.getComment());
            dataList.add(columnInfo.getLabelName());
            dataList.add(columnInfo.getFormItemType());
            dataList.add(columnInfo.getFormItemSourceType());
            dataList.add(columnInfo.getFormItemSource());
            dataList.add(columnInfo.getFormItemRules());
            dataList.add(columnInfo.getRequire());
            dataList.add(columnInfo.getHidden());
            //渲染附加数据
            if (columnInfo.getExt() != null && !columnInfo.getExt().isEmpty()) {
                // 跳过默认的3条数据
                for (int i = 3; i < tableModel.getColumnCount(); i++) {
                    dataList.add(columnInfo.getExt().get(tableModel.getColumnName(i)));
                }
            }
            tableModel.addRow(dataList.toArray());
        });
        table.setModel(tableModel);
        //刷新列编辑器
        refreshColumnEditor(columnConfigList);

        //添加数据修改事件
        tableModel.addTableModelListener(e -> {
            // 一键编辑多行时不处理。
            if (e.getFirstRow() != e.getLastRow()) {
                return;
            }
            int row = e.getFirstRow();
            int column = e.getColumn();
            Object val = tableModel.getValueAt(row, column);
            ColumnInfo columnInfo = tableInfo.getFullColumn().get(row);
            if (column == 0) {
                for (ColumnInfo info : tableInfo.getFullColumn()) {
                    if (info.getSrcName().equals(val) && !info.getSrcName().equals(columnInfo.getSrcName())) {
                        Messages.showWarningDialog("Column Name Already exist!", MsgValue.TITLE_INFO);
                        // 输入的名称已经存在时，直接还原
                        tableModel.setValueAt(columnInfo.getSrcName(), row, column);
                        return;
                    }
                }
            }
            switch (column) {
                case 0:
                    columnInfo.setSrcName((String) val);
                    break;
                case 1:
                    columnInfo.setTargetName((String) val);
                case 2:
                    columnInfo.setType((String) val);
                    break;
                case 3:
                    columnInfo.setComment((String) val);
                    break;
                case 4:
                    columnInfo.setLabelName((String) val);
                    break;
                case 5:
                    columnInfo.setFormItemType((String) val);
                    break;
                case 6:
                    columnInfo.setFormItemSourceType((String) val);
                    break;
                case 7:
                    columnInfo.setFormItemSource((String) val);
                    break;
                case 8:
                    columnInfo.setFormItemRules((String) val);
                    break;
                case 9:
                    columnInfo.setRequire((String) val);
                    break;

                case 10:
                    columnInfo.setHidden((String) val);
                    break;
                default:
                    if (columnInfo.getExt() == null) {
                        columnInfo.setExt(new HashMap<>(10));
                    }
                    String title = tableModel.getColumnName(column);
                    columnInfo.getExt().put(title, val);
                    break;
            }
        });
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //添加元素事件
        addButton.addActionListener(e -> {
            if (!initFlag) {
                return;
            }
            //输入列名
            String value = Messages.showInputDialog("Column Name:", "Input Column Name:", Messages.getQuestionIcon(), "Demo", new InputValidator() {
                @Override
                public boolean checkInput(String inputString) {
                    if (StringUtils.isEmpty(inputString)) {
                        return false;
                    }
                    for (ColumnInfo columnInfo : tableInfo.getFullColumn()) {
                        if (columnInfo.getSrcName().equals(inputString)) {
                            return false;
                        }
                    }
                    return true;
                }

                @Override
                public boolean canClose(String inputString) {
                    return this.checkInput(inputString);
                }
            });
            //取消输入
            if (value == null) {
                return;
            }

            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setSrcName(value);
            columnInfo.setType("java.lang.String");
            // 标记为自定义列
            columnInfo.setCustom(true);
            tableInfo.getFullColumn().add(columnInfo);
            refresh();
        });
    }

    /**
     * 刷新列编辑器
     *
     * @param columnConfigList 列配置集合
     */
    private void refreshColumnEditor(List<ColumnConfig> columnConfigList) {
        columnConfigList.forEach(columnConfig -> {
            TableColumn tableColumn = table.getColumn(columnConfig.getTitle());
            int index = tableColumn.getModelIndex();
            switch (columnConfig.getType()) {
                case TEXT:
                    break;
                case SELECT:
                    tableColumn.setCellEditor(new ComboBoxCellEditor() {
                        @Override
                        protected List<String> getComboBoxItems() {
                            String selectValue = columnConfig.getSelectValue();
                            if (StringUtils.isEmpty(selectValue)) {
                                //noinspection unchecked
                                return Collections.EMPTY_LIST;
                            }
                            return Arrays.asList(columnConfig.getSelectValue().split(","));
                        }
                    });
                    break;
                case BOOLEAN:
                    //给空列赋初始值
                    for (int row = 0; row < tableModel.getRowCount(); row++) {
                        if (tableModel.getValueAt(row, index) == null) {
                            tableModel.setValueAt(false, row, index);
                        }
                    }
                    tableColumn.setCellEditor(new BooleanTableCellEditor());
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 获取初始列
     *
     * @param columnConfigList 列配置集合
     * @return 初始列信息
     */
    private List<ColumnConfig> getInitColumn(List<ColumnConfig> columnConfigList) {
        List<ColumnConfig> result = new ArrayList<>();
        result.add(new ColumnConfig("srcName", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("targetName", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("type", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("comment", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("labelName", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("formItemType", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("formItemSourceType", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("formItemSource", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("formItemRules", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("require", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("hidden", ColumnConfigType.TEXT));
        result.addAll(columnConfigList);
        return result;
    }

    /**
     * 打开窗口
     */
    public void open() {
        setTitle("Config Table " + cacheDataUtils.getSelectDbTable().getName());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
