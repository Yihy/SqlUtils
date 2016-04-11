package cc.yihy.dbutils.dbLib;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cc.yihy.annotation.TableColumn;
import cc.yihy.dbutils.DbUtils;
import cc.yihy.dbutils.dbLib.RunTemp;

/**
 * 解析实体类的注解，生成表内键值信息
 * 
 * @author 念去去云
 * @version 1.0
 */
public class GetAnnotation {
	/**
	 * 解析实体类的注解，生成表内键值信息 传入实体类的对象，或者实体类的字节码（Class）。
	 * 当为字节码时，返回的结果List<RunTemp>内的RunTemp对象的value没有值，为null
	 * 
	 * @param t
	 *            实体类的对象，或者实体类的字节码（Class）。
	 * @return List<RunTemp> 实体对应数据库表内的字段信息。
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static <T> List<RunTemp> getData(T t)
			throws IllegalArgumentException, IllegalAccessException {
		List<RunTemp> runtemp = new ArrayList<RunTemp>();
		// 获得需要插入的数据的列名和外键关系。
		Field[] fi;
		boolean b = false;
		if (t instanceof Class) {
			fi = ((Class) t).getDeclaredFields();
			b = false;
		} else {
			fi = t.getClass().getDeclaredFields();
			b = true;
		}
		
		for (Field field : fi) {

			RunTemp temp = new RunTemp();
			TableColumn annotation = field.getAnnotation(TableColumn.class);

			temp.setKey(annotation != null ? (annotation.columnName()
					.equals("") ? field.getName() : annotation.columnName())
					: field.getName());
			temp.setIsFK(annotation != null ? annotation.isFK() : false);
			temp.setIsPK(annotation != null ? annotation.isPK() : false);

			if (b) {
				field.setAccessible(true);
				temp.setValue(field.get(t));
			}
			runtemp.add(temp);
		}	
		return runtemp;
	}
}
