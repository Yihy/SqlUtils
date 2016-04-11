package cc.yihy.dbutils.dbLib.impl;

import java.util.List;

import cc.yihy.annotation.DbTable;
import cc.yihy.dbutils.dbLib.CreatSqlable;
import cc.yihy.dbutils.dbLib.RunTemp;
import cc.yihy.dbutils.dbLib.SqlConn;

/**
 * 创建SQL的实现类 删除
 * 
 * @author 念去去云
 * @version 1.0
 *
 */
public class CreatDeleteSqlimpl implements CreatSqlable {

	@Override
	public <T> String getSql(Class<T> c, List<RunTemp> temp, Object... sele) {

		DbTable annotation = c.getAnnotation(DbTable.class);
		String tableName = annotation != null ? annotation.tableName() : c
				.getSimpleName();
		StringBuffer sql = new StringBuffer("Delete FROM  ");
		sql.append(tableName);

		sql.append(" where ");
		sql.append(sele[0]);
		sql.append("=?;");
		if ("true".equals(SqlConn.sqlShow))
			System.out.println(sql.toString());
		return sql.toString();
	}

}
