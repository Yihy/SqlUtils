package cc.yihy.dbutils.dbLib;

import java.util.List;

/**
 * 创建SQL的接口
 * 
 * @author 念去去云
 * @version 1.0
 *
 */
public interface CreatSqlable {
	<T> String getSql(Class<T> c, List<RunTemp> temp, Object... sele);
}
