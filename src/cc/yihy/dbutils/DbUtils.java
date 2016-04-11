package cc.yihy.dbutils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cc.yihy.annotation.TableColumn;
import cc.yihy.dbutils.dbLib.CastData;
import cc.yihy.dbutils.dbLib.CreatSqlEnum;
import cc.yihy.dbutils.dbLib.CreatSqlFactory;
import cc.yihy.dbutils.dbLib.GetAnnotation;
import cc.yihy.dbutils.dbLib.RunTemp;
import cc.yihy.dbutils.dbLib.SqlConn;

/**
 * 
 * @author 念去去云
 * @version 1.3
 */
public class DbUtils {
	// 操作名，类名

	private  Connection conn ;
	private  PreparedStatement pstmt;

	public DbUtils(Connection conn) {
		this.conn = conn;
	}

	public DbUtils()  {
		try {
			conn =SqlConn.getConnection() ;
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 查询符合JavaBean规范实体对象的数据库表的所有数据
	 * 
	 * @param c
	 *            实体的字节码
	 * @return 返回List<T>集合
	 * @throws Exception
	 */
	public <T> List<T> getList(Class<T> c) throws Exception {
		return getListBySome(c, "", null);
	}

	/**
	 * 按对象查询id
	 * 
	 * @param t
	 *            对象
	 * @return 返回对象的id
	 * @throws Exception
	 */
	public <T> String getId(T t) throws Exception {

		// 构建生成SQL方法的Map参数
		Map<String, Object> mapWhere = new HashMap<String, Object>();
		// 获得对象表List
		List<RunTemp> data = GetAnnotation.getData(t);

		Object id = data.get(0).getKey();
		// 向Map里添加数据
		for (int i = 1; i < data.size(); i++) {
			if (!data.get(i).getIsFK())
				mapWhere.put(data.get(i).getKey(), data.get(i).getValue());
		}
		// 调用getListBySome()方法生成
		List<Object[]> listBySome = getListBySome(t.getClass(), mapWhere, id);
		if (listBySome != null && listBySome.size() != 0) {
			Object[] result = listBySome.get(0);
			return result[0] != null ? result[0].toString() : "-1";
		}

		return "-1";

	}

	/**
	 * 查询符合JavaBean规范实体对象的数据库表的主键（id）的数据
	 * 
	 * @param c
	 *            实体的字节码
	 * @param value
	 *            id 的编号
	 * @return 返回List<T>集合
	 * @throws Exception
	 */
	public <T> T getListById(Class<T> c, Object value) throws Exception {
		String id = null;

		List<RunTemp> data = GetAnnotation.getData(c);

		for (RunTemp runTemp : data) {
			if (runTemp.getIsPK()) {
				id = runTemp.getKey();
				break;
			}
		}
	
		List<T> listBySome = getListBySome(c, id, value);
		return listBySome != null ? listBySome.get(0) : null;
	}

	/**
	 * 查询符合JavaBean规范实体对象的数据库表的指定字段的数据
	 * 
	 * @param c
	 *            实体类对象
	 * @param mapWhere
	 *            需要查询的 参数
	 * @param sele
	 *            需要查询的字段
	 * @return List<Object[]> 字段的数组
	 * @throws Exception
	 */
	public <T> List<Object[]> getListBySome(Class<T> c,
			Map<String, Object> mapWhere, Object... sele) throws Exception {

		Set<Entry<String, Object>> entrySet = mapWhere.entrySet();
		Iterator<Entry<String, Object>> iterator = entrySet.iterator();
		List<RunTemp> list = new ArrayList<RunTemp>();

		for (int i = 0; i < mapWhere.size(); i++) {
			Entry<String, Object> entry = iterator.next();
			RunTemp runTemp = new RunTemp();
			runTemp.setKey(entry.getKey());
			runTemp.setValue(entry.getValue());
			list.add(runTemp);
		}

		// 创建SQL语句
		String sql = new CreatSqlFactory(CreatSqlEnum.SELECT).getSql(c, list,
				sele);
		ResultSet re = null;

		try {
			// 获得查询结果
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				pstmt.setObject(i + 1, list.get(i).getValue());
			}
			re = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return CastData.CastList(c, re, sele);
	}

	/**
	 * 查询符合JavaBean规范实体对象的数据库表的指定字段的数据
	 * 
	 * @param c
	 *            实体类对象
	 * @param name
	 *            查询条件的key
	 * @param value
	 *            查询条件的value
	 * @return List<T>
	 * @throws Exception
	 */
	public <T> List<T> getListBySome(Class<T> c, String name, Object value)
			throws Exception {

		ResultSet re = null;
		List<RunTemp> r = new ArrayList<RunTemp>();

		if (!(name == null || name.isEmpty())) {
			RunTemp temp = new RunTemp();
			temp.setKey(name);
			temp.setValue(value);
			r.add(temp);
		}

		String sql = new CreatSqlFactory(CreatSqlEnum.SELECT).getSql(c,
				r.isEmpty() ? null : r);

		// 处理查询结果

		// 预编译sql语句
		try {
			pstmt = conn.prepareStatement(sql);
			boolean b = value != null && !"".equals(value);
			if (b)
				pstmt.setObject(1, value);
			re = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 执行查询，获得结果集

		return CastData.CastList(c, re);
	}
/**
 * 
 * @param sql    需要查询的sql语句
 * @param args     sql语句参数化值
 * @return     ResultSet 结果集
 * @throws SQLException
 */
	public ResultSet getResultSetBySql(String sql,Object...args ) throws SQLException{
		pstmt = conn.prepareStatement(sql);
		for (int i = 1; i < args.length; i++) {
			pstmt.setObject(i,args[i-1]);
		}
		return pstmt.executeQuery();			
	}
	
	
	
	
	
	/**
	 * 插入一条符合JavaBean规范实体对象的数据库表的数据
	 * 
	 * @param t
	 *            需要插入的实体对象
	 * @return 插入成功返回true，失败返回false。
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> boolean insert(T t) throws Exception {

		// INSERT INTO tb_spuser` ( `name`, `password`, `isDelete`) VALUES (
		// 'ad', '123', '1');

		List<RunTemp> runtemp = new ArrayList<RunTemp>();

		runtemp = GetAnnotation.getData(t);

		for (RunTemp temp : runtemp) {
			String id;
			if (temp.getIsFK()) {
				id = getId(temp.getValue());
				if ("-1".equals(id)) {
					// 后期优化为抛出异常
					return false;
				}
				temp.setValue(id);
			}
		}

		String sql = new CreatSqlFactory(CreatSqlEnum.INSERT).getSql(
				t.getClass(), runtemp);
		try {
			pstmt = conn.prepareStatement(sql.toString());
			for (int i = 1; i < runtemp.size(); i++) {
				pstmt.setObject(i, runtemp.get(i).getValue());
			}
			return pstmt.executeUpdate() > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 更新一条符合JavaBean规范实体对象的数据库表的数据
	 * 
	 * @param t
	 *            需要更新的实体对象
	 * @return 更新成功返回true，失败返回false。
	 * @throws Exception
	 */
	public <T> boolean update(T t) throws Exception {

		List<RunTemp> data = GetAnnotation.getData(t);
		String[] key = new String[data.size()];

		for (int i = 0; i < data.size(); i++) {
			key[i] = data.get(i).getKey();
		}
		for (int i = 1; i < data.size(); i++) {

			if (data.get(i).getIsFK()) {
				String id = getId(data.get(i).getValue());
				if ("-1".equals(id)) {
					return false;
				}
				data.get(i).setValue(id);
			}
		}
		// 获得更新的语句
		String sql = new CreatSqlFactory(CreatSqlEnum.UPDATE).getSql(
				t.getClass(), data);

		try {

			pstmt = conn.prepareStatement(sql);

			for (int i = 1; i < data.size(); i++) {
				pstmt.setObject(i, data.get(i).getValue());
			}

			pstmt.setObject(data.size(), data.get(0).getValue());

			return pstmt.executeUpdate() == 0 ? false : true;
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除一条符合JavaBean规范实体对象的数据库表的数据
	 * 
	 * @param t
	 * 
	 * @return 删除成功返回true，失败返回false。
	 */
	public <T> boolean delete(T t) {

		try {
			Field[] fi = t.getClass().getDeclaredFields();
			fi[0].setAccessible(true);

			return deleteById(t.getClass(), fi[0].getInt(t));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除一条符合JavaBean规范实体对象的数据库表的数据
	 * 
	 * @param c
	 *            字节码对象
	 * @param id
	 *            主键（id）编号
	 * @return 删除成功返回true，失败返回false。
	 * @throws Exception
	 */
	public <T> boolean deleteById(Class<T> c, int id) throws Exception {
	
		Field[] declaredFields = c.getDeclaredFields();
		TableColumn getid = declaredFields[0].getAnnotation(TableColumn.class);
		String id1 = getid != null ? getid.columnName() : declaredFields[0]
				.getName();
		String sql = new CreatSqlFactory(CreatSqlEnum.DELETE).getSql(c, null,
				id1);

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setObject(1, id);
			return pstmt.executeUpdate() != 0 ? true : false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭Connection连接
	 */
	public void CloseConn() {

		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
