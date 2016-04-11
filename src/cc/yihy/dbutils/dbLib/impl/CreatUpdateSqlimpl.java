package cc.yihy.dbutils.dbLib.impl;

import java.lang.reflect.Field;
import java.util.List;

import cc.yihy.annotation.DbTable;
import cc.yihy.dbutils.dbLib.CreatSqlable;
import cc.yihy.dbutils.dbLib.RunTemp;
import cc.yihy.dbutils.dbLib.SqlConn;

/**
 * 创建SQL的实现类 更新
 * 
 * @author 念去去云
 * @version 1.0
 *
 */
public class CreatUpdateSqlimpl implements CreatSqlable {

	@Override
	public <T> String getSql(Class<T> c, List<RunTemp> temp, Object... sele) {

		DbTable annotation = c.getAnnotation(DbTable.class);
		String tableName = annotation != null ? annotation.tableName() : c
				.getSimpleName();
		StringBuffer sql = new StringBuffer("UPDATE ");
		sql.append(tableName);
		sql.append(" SET ");

		Field[] fi = c.getDeclaredFields();

		for (int i = 1; i < temp.size(); i++) {

			sql.append(temp.get(i).getKey());
			sql.append("=?");
			if (i != temp.size() - 1) {
				sql.append(",");
			} else {
				sql.append(" WHERE ");
			}
		}
		// 应改为迭代temp，获得其中的主键
		int i = 1;
		boolean b = true;
		for (RunTemp r : temp) {
			System.out.println(i + "----" + r.getIsPK());
			i++;
			if (r.getIsPK()) {
				b = false;
				sql.append(r.getKey());
				break;
			}
		}
		if (b)
			sql.append(temp.get(0).getKey());
		sql.append("=?;");
		if ("true".equals(SqlConn.sqlShow))
			System.out.println(sql.toString());
		return sql.toString();
	}

}
