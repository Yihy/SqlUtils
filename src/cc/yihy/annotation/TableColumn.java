package cc.yihy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表内字段的注解
 * @author 念去去云
 *
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableColumn {
	public String columnName() default "";

	public boolean isPK() default false;
	public boolean isFK() default false;
}
