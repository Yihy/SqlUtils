package cc.yihy.dbutils.dbLib;

/**
 * 查询实体对应表的键值
 * 
 * @author 念去去云
 * @version 1.1
 */
public class RunTemp {
	
	//key 表内字段的名称
	//value 表内字段的名称 对应的值
	//isPK 表内字段的名称  是否为主键
	//isFK 表内字段的名称 是否为外键
	private String key;
	private Object value;
	private Boolean isPK;
	private Boolean isFK;

	public RunTemp() {
		super();
	}

	

	public RunTemp(String key, Object value, Boolean isFK, Boolean isPK) {
		super();
		this.key = key;
		this.value = value;
		this.isFK = isFK;
		this.isPK = isPK;
	}



	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Boolean getIsFK() {
		return isFK;
	}

	public void setIsFK(Boolean isFK) {
		this.isFK = isFK;
	}



	public Boolean getIsPK() {
		return isPK;
	}



	public void setIsPK(Boolean isPK) {
		this.isPK = isPK;
	}

}
