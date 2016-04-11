package cc.yihy.dbutils.dbLib;

/**
 * SQL工厂的枚举信息
 * 
 * @author 念去去云
 * @version 1.0
 *
 */
public enum CreatSqlEnum {
	SELECT("cc.yihy.dbutils.dbLib.impl.CreatSelectSqlimpl"), UPDATE(
			"cc.yihy.dbutils.dbLib.impl.CreatUpdateSqlimpl"), INSERT(
			"cc.yihy.dbutils.dbLib.impl.CreatInsertSqlimpl"), DELETE(
			"cc.yihy.dbutils.dbLib.impl.CreatDeleteSqlimpl");

	private String values;

	private CreatSqlEnum(String values) {
		this.values = values;
	}

	public String Value() {
		return values;
	}
}
