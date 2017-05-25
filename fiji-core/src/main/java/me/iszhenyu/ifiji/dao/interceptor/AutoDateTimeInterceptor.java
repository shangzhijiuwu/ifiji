package me.iszhenyu.ifiji.dao.interceptor;

import me.iszhenyu.ifiji.model.BaseDO;
import me.iszhenyu.ifiji.util.ReflectUtils;
import me.iszhenyu.ifiji.util.TimeUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * Created by xiaoz on 2017/5/21.
 */
@Intercepts({
	@Signature(
			type = StatementHandler.class,
			method = "prepare",
			args = {
				Connection.class, Integer.class
			})
})
public class AutoDateTimeInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
		StatementHandler delegate = (StatementHandler) ReflectUtils.getFieldValue(handler, "delegate");
		if (delegate != null) {
			BoundSql boundSql = delegate.getBoundSql();
			String sql = boundSql.getSql().toLowerCase();
			Object obj = boundSql.getParameterObject();
			if (obj instanceof BaseDO) {
				Date rightNow = TimeUtils.gmtNow();
				if (sql.indexOf("insert") == 0) {
					((BaseDO) obj).setGmtCreate(rightNow);
					((BaseDO) obj).setGmtModified(rightNow);
				} else if (sql.indexOf("update") == 0) {
					((BaseDO) obj).setGmtModified(rightNow);
				}
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object o) {
		return (o instanceof StatementHandler) ? Plugin.wrap(o, this) : o;
	}

	@Override
	public void setProperties(Properties properties) {

	}
}
