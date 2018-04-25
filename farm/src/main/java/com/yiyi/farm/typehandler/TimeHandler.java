package com.yiyi.farm.typehandler;

import com.yiyi.farm.util.TimeUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

/**
 * @author  peach
 * @date 2018-03-30 15:44:13
 * @description 处理数据库int毫秒与TimeStamps的转换
 */
public class TimeHandler extends BaseTypeHandler<Timestamp> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Timestamp timestamp, JdbcType jdbcType) throws SQLException {
        preparedStatement.setLong(i, timestamp.getTime());
    }

    @Override
    public Timestamp getNullableResult(ResultSet resultSet, String s) throws SQLException {
        long millisecond = resultSet.getLong(s);
        return resultSet.wasNull() ? null : timestampOf(millisecond);
    }

    @Override
    public Timestamp getNullableResult(ResultSet resultSet, int i) throws SQLException {
        long millisecond = resultSet.getLong(i);
        return resultSet.wasNull() ? null : timestampOf(millisecond);
    }

    @Override
    public Timestamp getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        long millisecond = callableStatement.getLong(i);
        return callableStatement.wasNull() ? null : timestampOf(millisecond);
    }

    private Timestamp timestampOf(long millisecond){
        try{
            return TimeUtil.getTimestamp(millisecond);
        }catch (Exception ex){
            throw new IllegalArgumentException(millisecond + " can not convert to Timestamp", ex);
        }
    }
}
