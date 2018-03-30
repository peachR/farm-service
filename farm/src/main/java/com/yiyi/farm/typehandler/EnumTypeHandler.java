package com.yiyi.farm.typehandler;

import com.yiyi.farm.enumeration.BaseEnum;
import com.yiyi.farm.util.EnumUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author peach
 * @date 2018-03-30 14:24:02
 * @description 实现数据库varchar与枚举类的相互映射
 * @param <E>
 */
public class EnumTypeHandler<E extends Enum<?> & BaseEnum> extends BaseTypeHandler<BaseEnum> {

    private Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        if(type == null)
            throw new IllegalArgumentException("Class Type can not be null");
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, BaseEnum baseEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, baseEnum.getCode());
    }

    @Override
    public BaseEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String code = resultSet.getString(s);
        return resultSet.wasNull() ? null : codeOf(code);
    }

    @Override
    public BaseEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String code = resultSet.getString(i);
        return resultSet.wasNull() ? null : codeOf(code);
    }

    @Override
    public BaseEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String code = callableStatement.getString(i);
        return callableStatement.wasNull() ? null : codeOf(code);
    }

    private E codeOf(String code){
        try{
            return EnumUtil.codeOf(type, code);
        }catch(Exception ex){
            throw new IllegalArgumentException("Cannot convert " + code + " to " + type.getSimpleName() + " by code value.", ex);
        }
    }

}
