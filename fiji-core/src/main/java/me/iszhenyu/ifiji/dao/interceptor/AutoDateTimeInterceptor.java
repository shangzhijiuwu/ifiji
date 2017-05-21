package me.iszhenyu.ifiji.dao.interceptor;

import me.iszhenyu.ifiji.model.BaseModel;
import me.iszhenyu.ifiji.util.ReflectUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
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
		StatementHandler delegate = (StatementHandler) ReflectUtils.getFieldValue(handler.getClass(), "delegate");
		BoundSql boundSql = delegate.getBoundSql();
		Object obj = boundSql.getParameterObject();
		if (obj instanceof BaseModel) {

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
