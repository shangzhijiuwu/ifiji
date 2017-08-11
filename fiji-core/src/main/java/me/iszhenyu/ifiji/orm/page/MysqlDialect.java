package me.iszhenyu.ifiji.orm.page;

/**
 * @author zhen.yu
 * @since 2017/8/11
 */
public class MysqlDialect implements Dialect {
    protected static final String SQL_END_DELIMITER = ";";

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String getLimitSqlString(String sql, int offset, int limit) {
        sql = sql.trim();
        boolean isForUpdate = false;
        if (sql.toLowerCase().endsWith(" for update")) {
            sql = sql.substring(0, sql.length() - 11);
            isForUpdate = true;
        }

        if (offset < 0) {
            offset = 0;
        }

        String finalString = sql + " limit " + offset + "," + limit;
        if (isForUpdate) {
            finalString += " for update";
        }

        return finalString;
    }

    @Override
    public String getCountSqlString(String sql) {
        sql = trim(sql);
        StringBuffer sb = new StringBuffer(sql.length() + 10);
        sb.append("SELECT COUNT(1) AS " + RS_COLUMN + " FROM  ( ");
        sb.append(sql);
        sb.append(")a");
        return sb.toString();
    }

    private static String trim(String sql) {
        sql = sql.trim();
        if (sql.endsWith(SQL_END_DELIMITER)) {
            sql = sql.substring(0, sql.length() - 1
                    - SQL_END_DELIMITER.length());
        }
        return sql;
    }
}
