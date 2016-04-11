package cc.yihy.dbutils.dbLib;

import java.util.List;

public class CreatSqlFactory {
	/**
	 * 创建SQL语句工厂
	 * 
	 * @author 念去去云
	 * @version 1.0
	 */
	private CreatSqlable creatSql;

	public CreatSqlFactory(CreatSqlEnum creatSql) throws Exception {
		this.creatSql = (CreatSqlable) Class.forName(creatSql.Value())
				.newInstance();
	}

	public <T> String getSql(Class<T> c, List<RunTemp> temp, Object... sele) {
		return creatSql.getSql(c, temp, sele);
	}

}
