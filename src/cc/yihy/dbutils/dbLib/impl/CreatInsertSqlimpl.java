package cc.yihy.dbutils.dbLib.impl;

import java.lang.reflect.Field;
import java.util.List;

import cc.yihy.annotation.DbTable;
import cc.yihy.annotation.TableColumn;
import cc.yihy.dbutils.dbLib.CreatSqlable;
import cc.yihy.dbutils.dbLib.RunTemp;
import cc.yihy.dbutils.dbLib.SqlConn;

/**
 * 创建SQL的实现类 插入
 * 
 * @author 念去去云
 * @version 1.0
 *
 */
public class CreatInsertSqlimpl implements CreatSqlable {

	@Override
	public <T> String getSql( Class<T> c, List<RunTemp> temp,
			Object... sele) {
		DbTable annotation = c.getAnnotation(DbTable.class);
		String tableName = annotation != null ? annotation.tableName() : c
				.getSimpleName();

		StringBuffer sql = new StringBuffer("INSERT INTO  ");
		sql.append(tableName);
		
		
		sql.append(" (");
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < temp.size(); i++) {
				
			sql.append(temp.get(i).getKey());
			sb.append("?");
			if (i != temp.size() - 1) {
				sql.append(",");
				sb.append(",");
			} else {
				sql.append(") VALUES (");
				sb.append(");");
			}
		}
		
		sql.append(sb);
		
		if ("true".equals(SqlConn.sqlShow))
			System.out.println(sql.toString());
		return sql.toString();
	}

}
