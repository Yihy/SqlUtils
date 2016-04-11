package cc.yihy.dbutils.dbLib;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cc.yihy.annotation.TableColumn;
import cc.yihy.dbutils.DbUtils;

/**
 * ResultSet 结果集的转换
 * 
 * @author 念去去云
 * @version 1.0
 *
 */
public class CastData {
	public static <T> List<T> CastList(Class<T> c, ResultSet re)
			throws Exception {
		List<T> result = new ArrayList<T>();

//		List<RunTemp> data = GetAnnotation.getData(c);
		
		Field[] fi = c.getDeclaredFields();
		while (re.next()) {
			// 获得c的实例t
			T t = c.newInstance();
			// 获得c中的属性
			// 迭代c中的属性，通过暴力破解为属性赋值，可选通过setXXX赋值
			for (Field field : fi) {
				// 获得表中列名的注解
				TableColumn annoColumnName = field
						.getAnnotation(TableColumn.class);
				// 判断注解是否为空，不为空时取出注解的内容作为列名，为空时取该属性名为作为列名
				String ColumnName = annoColumnName != null ? (annoColumnName.columnName()
						.equals("") ? field.getName() : annoColumnName.columnName())
						: field.getName();
				// 获得是否是外键
				boolean isFK = annoColumnName != null ? annoColumnName.isFK()
						: false;

				// 设置暴力破解
				field.setAccessible(true);

				// 转换JAVA中的boolean类型到数据库中的数字1,0表示
				if (field.getType() == boolean.class) {
					field.set(t, re.getInt(ColumnName) == 0 ? false : true);
				} else {
					// 判断是否为外键
					if (isFK) {
						DbUtils d = new DbUtils();
						field.set(
								t,
								d.getListById(field.getType(),
										re.getObject(ColumnName)));
					} else {
						field.set(t, re.getObject(ColumnName));
					}
				}
			}
			result.add(t);
		   }

		return result; 
	}

	public static <T> List<Object[]> CastList(Class<T> c, ResultSet re,
			Object... objects) {

		List<Object[]> list = new ArrayList<Object[]>();

		try {
			while (re.next()) {
				Object[] o = new Object[objects.length];
				for (int n = 0; n < objects.length; n++) {
					o[n] = re.getObject(n + 1);
				}
				list.add(o);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

}
