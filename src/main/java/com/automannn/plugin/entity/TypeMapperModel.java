package com.automannn.plugin.entity;


import com.automannn.plugin.comm.AbstractTableModel;

/**
 * 类型映射模型
 *
 */
public class TypeMapperModel extends AbstractTableModel<TypeMapper> {
    @Override
    protected String[] initColumnName() {
        return new String[]{"columnType", "javaType"};
    }

    @Override
    protected Object[] toObj(TypeMapper entity) {
        return new Object[]{entity.getColumnType(), entity.getJavaType()};
    }

    @Override
    protected void setVal(TypeMapper obj, int columnIndex, Object val) {
        if (columnIndex==0){
            obj.setColumnType((String) val);
        }else{
            obj.setJavaType((String) val);
        }
    }
}
