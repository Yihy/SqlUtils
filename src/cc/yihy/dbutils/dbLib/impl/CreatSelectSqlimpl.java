package cc.yihy.dbutils.dbLib.impl;

import java.util.List;

import cc.yihy.annotation.DbTable;
import cc.yihy.dbutils.dbLib.CreatSqlable;
import cc.yihy.dbutils.dbLib.RunTemp;
import cc.yihy.dbutils.dbLib.SqlConn;

/**
 * 创建SQL的实现类 查询
 * 
 * @author 念去去云
 * @version 1.0
 *
 */
public class CreatSelectSqlimpl implements CreatSqlable {

	public <T> String getSql(Class<T> c, List<RunTemp> temp, Object... sele) {

		DbTable annotation = c.getAnnotation(DbTable.class);
		String tableName = annotation != null ? annotation.tableName() : c
				.getSimpleName();
		StringBuffer sql = new StringBuffer("select  ");
		if (sele == null || sele.length == 0) {
			sql.append(" * ");
		} else {
			for (int i = 0; i < sele.length; i++) {
				sql.append(sele[i]);
				if (i != sele.length - 1)
					sql.append(", ");
			}
		}
		sql.append("  from ");
		sql.append(tableName);

		if (!(temp == null || temp.isEmpty())) {
			sql.append(" where ");
			for (int n = 0; n < temp.size(); n++) {
				sql.append(temp.get(n).getKey());
				sql.append("= ?");				
				if (n != temp.size() - 1)
					sql.append(" and ");
			}
		}

		sql.append(";");
		if ("true".equals(SqlConn.sqlShow))
			System.out.println(sql.toString());
		return sql.toString();
	}
}
